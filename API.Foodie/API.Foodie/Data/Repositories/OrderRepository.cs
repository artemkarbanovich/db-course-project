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
}
