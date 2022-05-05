using API.Foodie.DTOs;
using API.Foodie.Helpers.QueryParams;
using API.Foodie.Interfaces.Data;
using API.Foodie.Model;

namespace API.Foodie.Data.Repositories;

public class OrderRepository : IOrderRepository
{
    private readonly SqlConnection _connection;
    
    public OrderRepository(SqlConnection connection)
    {
        _connection = connection;
    }


    public async Task<decimal> CalculateOrderTotalPriceAsync(List<OrderDish> orderDishes)
    {
        var command = new SqlCommand()
        {
            CommandText = "dbo.CalculateOrderTotalPrice",
            Connection = _connection,
            CommandType = CommandType.StoredProcedure
        };

        var orderDishData = new DataTable();
        orderDishData.Columns.Add(new DataColumn("DishId", typeof(int)));
        orderDishData.Columns.Add(new DataColumn("DishesCount", typeof(int)));

        foreach(var d in orderDishes)
        {
            var row = orderDishData.NewRow();

            row["DishId"] = d.DishId;
            row["DishesCount"] = d.DishesCount;

            orderDishData.Rows.Add(row);
        }

        var orderDishDataParam = command.Parameters.AddWithValue("@orderDishes", orderDishData);
        orderDishDataParam.SqlDbType = SqlDbType.Structured;
        orderDishDataParam.TypeName = "dbo.OrderDishes";

        await _connection.OpenAsync();

        decimal totalPrice = (decimal)await command.ExecuteScalarAsync();

        await _connection.CloseAsync();

        return totalPrice;
    }

    public async Task<bool> AddOrderAsync(Order order)
    {
        var command = new SqlCommand()
        {
            CommandText = "dbo.AddOrder",
            Connection = _connection,
            CommandType = CommandType.StoredProcedure
        };

        command.Parameters.AddWithValue("@orderDate", order.OrderDate.ToString("yyyy-MM-ddTHH:mm:ss"));
        command.Parameters.AddWithValue("@deliveryDate", order.DeliveryDate.ToString("yyyy-MM-ddTHH:mm:ss"));
        command.Parameters.AddWithValue("@totalPrice", order.TotalPrice);
        command.Parameters.AddWithValue("@status", order.Status);
        command.Parameters.AddWithValue("@address", order.Address);
        command.Parameters.AddWithValue("@appUserId", order.AppUserId);

        var orderDishData = new DataTable();
        orderDishData.Columns.Add(new DataColumn("DishId", typeof(int)));
        orderDishData.Columns.Add(new DataColumn("DishesCount", typeof(int)));

        foreach (var d in order.Dishes)
        {
            var row = orderDishData.NewRow();

            row["DishId"] = d.DishId;
            row["DishesCount"] = d.DishesCount;

            orderDishData.Rows.Add(row);
        }

        var orderDishDataParam = command.Parameters.AddWithValue("@dishes", orderDishData);
        orderDishDataParam.SqlDbType = SqlDbType.Structured;
        orderDishDataParam.TypeName = "dbo.OrderDishes";

        await _connection.OpenAsync();

        int executeResult = (int)await command.ExecuteScalarAsync();

        await _connection.CloseAsync();

        return executeResult == 1 ? true : false;
    }

    public async Task<Order> GetOrderAsync(int id)
    {
        var command = new SqlCommand()
        {
            CommandText = "dbo.GetOrderById",
            Connection = _connection,
            CommandType = CommandType.StoredProcedure

        };

        command.Parameters.AddWithValue("@orderId", id);

        await _connection.OpenAsync();

        using SqlDataReader reader = await command.ExecuteReaderAsync();

        if (!reader.HasRows || !await reader.ReadAsync())
        {
            return null;
        }

        var order = new Order()
        {
            Id = (int)reader["Id"],
            OrderDate = (DateTime)reader["OrderDate"],
            DeliveryDate = (DateTime)reader["DeliveryDate"],
            TotalPrice = (decimal)reader["TotalPrice"],
            Status = (string)reader["Status"],
            Address = (string)reader["Address"],
            AppUser = new AppUser()
            {
                FirstName = (string)reader["FirstName"],
                LastName = (string)reader["LastName"],
                Email = (string)reader["Email"],
                PhoneNumber = (string)reader["PhoneNumber"]
            },
            Dishes = new List<OrderDish>()
            {
                new OrderDish()
                {
                    DishesCount = (int)reader["DishesCount"],
                    Dish = new Dish()
                    {
                        Name = (string)reader["Name"],
                        CookingTime = (TimeSpan)reader["CookingTime"],
                        YouWillNeed = (string)reader["YouWillNeed"],
                        DishWeight = (int)reader["DishWeight"],
                        Price = (decimal)reader["Price"],
                        Ingredients = (string)reader["Ingredients"]
                    }
                }
            }
        };

        while(await reader.ReadAsync())
        {
            order.Dishes.Add(new OrderDish()
            {
                DishesCount = (int)reader["DishesCount"],
                Dish = new Dish()
                {
                    Name = (string)reader["Name"],
                    CookingTime = (TimeSpan)reader["CookingTime"],
                    YouWillNeed = (string)reader["YouWillNeed"],
                    DishWeight = (int)reader["DishWeight"],
                    Price = (decimal)reader["Price"],
                    Ingredients = (string)reader["Ingredients"]
                }
            });
        }

        await _connection.CloseAsync();

        return order;
    }

    public async Task<bool> UpdateOrderStatusAsync(int orderId, string status)
    {
        var command = new SqlCommand() 
        { 
            Connection = _connection,
            CommandText =
            "UPDATE Orders SET Status = @status WHERE Id = @id;"
        };

        command.Parameters.AddWithValue("@status", status);
        command.Parameters.AddWithValue("@id", orderId);

        await _connection.OpenAsync();

        int updatedRows = await command.ExecuteNonQueryAsync();

        await _connection.CloseAsync();

        return updatedRows > 0;
    }

    public async Task<List<OrderAdminListDto>> GetAdminListAsync(OrderAdminListParams queryParams)
    {
        var command = new SqlCommand()
        {
            CommandText = "dbo.GetOrderAdminList",
            Connection = _connection,
            CommandType = CommandType.StoredProcedure
        };

        command.Parameters.AddWithValue("@status", queryParams.Status);

        await _connection.OpenAsync();

        using var reader = await command.ExecuteReaderAsync();

        var orderAdminListDto = new List<OrderAdminListDto>();

        if (!reader.HasRows)
        {
            return orderAdminListDto;
        }

        while(await reader.ReadAsync())
        {
            var order = new OrderAdminListDto()
            {
                Id = (int)reader["Id"],
                OrderDate = (DateTime)reader["OrderDate"],
                DeliveryDate = (DateTime)reader["DeliveryDate"],
                TotalPrice = (decimal)reader["TotalPrice"],
                Status = (string)reader["Status"],
                Address = (string)reader["Address"],

                Email = (string)reader["Email"],
                FirstName = (string)reader["FirstName"],
                LastName = (string)reader["LastName"],
                PhoneNumber = (string)reader["PhoneNumber"],

                Dishes = new List<OrderDishAdminListDto>()
                {
                    new OrderDishAdminListDto()
                    {
                        DishesCount = (int)reader["DishesCount"],
                        Name = (string)reader["Name"],
                        CookingTime = (TimeSpan)reader["CookingTime"],
                        DishWeight = (int)reader["DishWeight"],
                        Price = (decimal)reader["Price"],
                        Ingredients = (string)reader["Ingredients"]
                    }
                }
            };

            var orderById = orderAdminListDto.SingleOrDefault(o => o.Id == order.Id);

            if (orderById == null)
            {
                orderAdminListDto.Add(order);
            }
            else
            {
                orderById.Dishes.AddRange(order.Dishes);
            }
        }

        await _connection.CloseAsync();

        return orderAdminListDto;
    }

    public async Task<List<OrderUserListDto>> GetUserListAsync(OrderUserListParams queryParams, int userId)
    {
        var command = new SqlCommand()
        {
            Connection = _connection,
            CommandText =
                " SELECT O.OrderDate, O.DeliveryDate, O.TotalPrice, O.Status, O.Address" +
                " FROM Orders AS O" +
                " WHERE O.AppUserId = @userId"
        };

        command.Parameters.AddWithValue("@userId", userId);

        if (queryParams.OrderDateFrom != null)
        {
            command.CommandText += " AND (CAST(O.OrderDate AS DATE) >= CAST(@orderDateFrom AS DATE))";
            command.Parameters.AddWithValue("@orderDateFrom", queryParams.OrderDateFrom);
        }
        if (queryParams.OrderDateTo != null)
        {
            command.CommandText += " AND (CAST(O.OrderDate AS DATE) <= CAST(@orderDateTo AS DATE))";
            command.Parameters.AddWithValue("@orderDateTo", queryParams.OrderDateTo);
        }

        command.CommandText += " ORDER BY O.OrderDate DESC;";

        await _connection.OpenAsync();

        using var reader = await command.ExecuteReaderAsync();

        var orderUserListDto = new List<OrderUserListDto>();

        if (reader == null || !reader.HasRows)
        {
            return orderUserListDto;
        }

        while (await reader.ReadAsync())
        {
            orderUserListDto.Add(new OrderUserListDto()
            {
                OrderDate = (DateTime)reader["OrderDate"],
                DeliveryDate = (DateTime)reader["DeliveryDate"],
                TotalPrice = (decimal)reader["TotalPrice"],
                Status = (string)reader["Status"],
                Address = (string)reader["Address"]
            });
        }

        await _connection.CloseAsync();

        return orderUserListDto;
    }
}
