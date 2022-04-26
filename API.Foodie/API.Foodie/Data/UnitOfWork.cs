using API.Foodie.Data.Repositories;
using API.Foodie.Interfaces.Data;

namespace API.Foodie.Data;

public class UnitOfWork : IUnitOfWork
{
    private readonly SqlConnection _connection;
    private readonly IAppUserRepository _appUserRepository = null;
    private readonly IDishRepository _dishRepository = null;

    public UnitOfWork(IConfiguration config)
    {
        _connection = new SqlConnection(config.GetConnectionString("DefaultConnection"));
    }


    public IAppUserRepository AppUserRepository => _appUserRepository ?? new AppUserRepository(_connection);
    public IDishRepository DishRepository => _dishRepository ?? new DishRepository(_connection);
}
