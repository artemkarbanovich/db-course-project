using API.Foodie.Data.Repositories;
using API.Foodie.Interfaces.Data;

namespace API.Foodie.Data;

public class UnitOfWork : IUnitOfWork
{
    private readonly SqlConnection _connection;
    private readonly IAppUserRepository _appUserRepository;
    private readonly IDishRepository _dishRepository;
    private readonly IOrderRepository _orderRepository;

    public UnitOfWork(IConfiguration config)
    {
        _connection = new SqlConnection(config.GetConnectionString("DefaultConnection"));
    }

    public IAppUserRepository AppUserRepository => _appUserRepository ?? new AppUserRepository(_connection);
    public IDishRepository DishRepository => _dishRepository ?? new DishRepository(_connection);
    public IOrderRepository OrderRepository => _orderRepository ?? new OrderRepository(_connection);
}
