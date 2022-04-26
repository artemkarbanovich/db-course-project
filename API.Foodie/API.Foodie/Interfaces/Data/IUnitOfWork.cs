namespace API.Foodie.Interfaces.Data;

public interface IUnitOfWork
{
    IAppUserRepository AppUserRepository { get; }
    IDishRepository DishRepository { get; }
}
