using API.Foodie.DTOs;
using API.Foodie.Interfaces.Data;

namespace API.Foodie.Data.Repositories;

public class StatRepository : IStatRepository
{
    private readonly SqlConnection _connection;

    public StatRepository(SqlConnection connection)
    {
        _connection = connection;
    }


    public async Task<StatAdminDto> GetAdminStatAsync()
    {
        var command = new SqlCommand()
        {
            CommandText = "dbo.GetAdminStatistics",
            Connection = _connection,
            CommandType = CommandType.StoredProcedure
        };

        var outputParams = new[]
        {
            new SqlParameter
            {
                ParameterName = "@totalUsersCount",
                SqlDbType = SqlDbType.Int
            },
            new SqlParameter
            {
                ParameterName = "@activeUsersCount",
                SqlDbType = SqlDbType.Int
            },
            new SqlParameter
            {
                ParameterName = "@totalRevenue",
                SqlDbType = SqlDbType.Money
            },
            new SqlParameter
            {
                ParameterName = "@acceptedOrdersCount",
                SqlDbType = SqlDbType.Int
            },
            new SqlParameter
            {
                ParameterName = "@inWayOrdersCount",
                SqlDbType = SqlDbType.Int
            },
            new SqlParameter
            {
                ParameterName = "@deliveredOrdersCount",
                SqlDbType = SqlDbType.Int,
            },
            new SqlParameter
            {
                ParameterName = "@canceledOrdersCount",
                SqlDbType = SqlDbType.Int
            },
            new SqlParameter
            {
                ParameterName = "@dishesCount",
                SqlDbType = SqlDbType.Int
            },
            new SqlParameter
            {
                ParameterName = "@visibleDishesCount",
                SqlDbType = SqlDbType.Int
            },
            new SqlParameter
            {
                ParameterName = "@avgDishPrice",
                SqlDbType = SqlDbType.Money
            }
        };

        foreach (var p in outputParams)
            p.Direction = ParameterDirection.Output;

        command.Parameters.AddRange(outputParams);

        await _connection.OpenAsync();
        
        await command.ExecuteNonQueryAsync();

        var statAdminDto = new StatAdminDto()
        {
            TotalUsersCount = (int)command.Parameters["@totalUsersCount"].Value,
            ActiveUsersCount = (int)command.Parameters["@activeUsersCount"].Value,
            TotalRevenue = (decimal)command.Parameters["@totalRevenue"].Value,
            AcceptedOrdersCount = (int)command.Parameters["@acceptedOrdersCount"].Value,
            InWayOrdersCount = (int)command.Parameters["@inWayOrdersCount"].Value,
            DeliveredOrdersCount = (int)command.Parameters["@deliveredOrdersCount"].Value,
            CanceledOrdersCount = (int)command.Parameters["@canceledOrdersCount"].Value,
            DishesCount = (int)command.Parameters["@dishesCount"].Value,
            VisibleDishesCount = (int)command.Parameters["@visibleDishesCount"].Value,
            AvgDishPrice = (decimal)command.Parameters["@avgDishPrice"].Value
        };

        await _connection.CloseAsync();

        return statAdminDto;
    }

    public async Task<StatUserDto> GetUserStatAsync(int userId)
    {
        var command = new SqlCommand()
        {
            CommandText = "dbo.GetUserStatistics",
            Connection = _connection,
            CommandType = CommandType.StoredProcedure
        };

        command.Parameters.AddRange(new[]
        {
            new SqlParameter
            {
                ParameterName = "@userId",
                Value = userId
            },
            new SqlParameter
            {
                ParameterName = "@totalOrdersCount",
                SqlDbType = SqlDbType.Int,
                Direction = ParameterDirection.Output
            },
            new SqlParameter
            {
                ParameterName = "@totalMoneySpent",
                SqlDbType = SqlDbType.Money,
                Direction = ParameterDirection.Output
            },
            new SqlParameter
            {
                ParameterName = "@ordersCountLastMonth",
                SqlDbType = SqlDbType.Int,
                Direction = ParameterDirection.Output
            },
            new SqlParameter
            {
                ParameterName = "@moneySpentLastMonth",
                SqlDbType = SqlDbType.Money,
                Direction = ParameterDirection.Output
            },
            new SqlParameter
            {
                ParameterName = "@waitingOrdersCount",
                SqlDbType = SqlDbType.Int,
                Direction = ParameterDirection.Output
            }
        });

        await _connection.OpenAsync();

        await command.ExecuteNonQueryAsync();

        var statUserDto = new StatUserDto()
        {
            TotalOrdersCount = Convert.IsDBNull(command.Parameters["@totalOrdersCount"].Value) ? null
                : (int?)command.Parameters["@totalOrdersCount"].Value,

            TotalMoneySpent = Convert.IsDBNull(command.Parameters["@totalMoneySpent"].Value) ? null
                : (decimal?)command.Parameters["@totalMoneySpent"].Value,

            OrdersCountLastMonth = Convert.IsDBNull(command.Parameters["@ordersCountLastMonth"].Value) ? null
                : (int?)command.Parameters["@ordersCountLastMonth"].Value,

            MoneySpentLastMonth = Convert.IsDBNull(command.Parameters["@moneySpentLastMonth"].Value) ? null
                : (decimal?)command.Parameters["@moneySpentLastMonth"].Value,

            WaitingOrdersCount = Convert.IsDBNull(command.Parameters["@waitingOrdersCount"].Value) ? null
                : (int?)command.Parameters["@waitingOrdersCount"].Value
        };

        await _connection.CloseAsync();

        return statUserDto;
    }
}
