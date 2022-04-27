using API.Foodie.Model;

namespace API.Foodie.Interfaces;

public interface IOrderStatusNotificatorService
{
    Task NotifyAsync(Order order);
}
