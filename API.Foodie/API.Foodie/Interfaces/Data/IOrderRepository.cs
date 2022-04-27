using API.Foodie.DTOs;
using API.Foodie.Helpers.QueryParams;
using API.Foodie.Model;

namespace API.Foodie.Interfaces.Data;

public interface IOrderRepository
{
    Task<decimal> CalculateOrderTotalPriceAsync(List<OrderDish> orderDishes);
    Task<bool> AddOrderAsync(Order order);
    Task<Order> GetOrderAsync(int id);
    Task<bool> UpdateOrderStatusAsync(int orderId, string status);
    Task<List<OrderAdminListDto>> GetAdminListAsync(OrderAdminListParams queryParams);
    Task<List<OrderUserListDto>> GetUserListAsync(OrderUserListParams queryParams, int userId);
}
