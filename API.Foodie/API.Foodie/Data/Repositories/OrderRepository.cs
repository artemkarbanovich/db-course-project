using API.Foodie.Interfaces.Data;

namespace API.Foodie.Data.Repositories;

public class OrderRepository : IOrderRepository
{
    private readonly SqlConnection _connection;
    
    public OrderRepository(SqlConnection connection)
    {
        _connection = connection;
    }
}
