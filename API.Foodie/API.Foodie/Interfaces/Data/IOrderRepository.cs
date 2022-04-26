using API.Foodie.Model;

namespace API.Foodie.Interfaces.Data;

public interface IOrderRepository
{
    Task<decimal> CalculateOrderTotalPriceAsync(List<OrderDish> orderDishes);
    Task<bool> AddOrderAsync(Order order);
}
