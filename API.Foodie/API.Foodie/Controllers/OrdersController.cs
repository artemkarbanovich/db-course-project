using API.Foodie.DTOs;
using API.Foodie.Extensions;
using API.Foodie.Interfaces;
using API.Foodie.Interfaces.Data;
using API.Foodie.Model;

namespace API.Foodie.Controllers;

public class OrdersController : BaseApiController
{
    private readonly IUnitOfWork _unitOfWork;
    private readonly IOrderStatusNotificatorService _orderStatusNotificator;

    public OrdersController(IUnitOfWork unitOfWork, IOrderStatusNotificatorService orderStatusNotificator)
    {
        _unitOfWork = unitOfWork;
        _orderStatusNotificator = orderStatusNotificator;
    }


    [HttpPost]
    [Authorize(Policy = "User")]
    public async Task<ActionResult> MakeOrder(OrderAddDto orderAddDto)
    {
        var user = await _unitOfWork.AppUserRepository.GetUserAsync(User.GetId());

        if(user == null)
        {
            return NotFound("User not found");
        }

        var orderDishes = new List<OrderDish>();

        foreach(var d in orderAddDto.Dishes)
        {
            orderDishes.Add(new OrderDish() 
            {
                DishesCount = d.DishesCount,
                DishId = d.DishId
            });
        }

        var totalPrice = await _unitOfWork.OrderRepository.CalculateOrderTotalPriceAsync(orderDishes);

        if(totalPrice <= 0)
        {
            return BadRequest("Error by calculating total price");
        }

        var order = new Order()
        {
            OrderDate = DateTime.Now,
            DeliveryDate = orderAddDto.DeliveryDate,
            TotalPrice = totalPrice,
            Status = "ACCEPTED",
            Address = orderAddDto.Address,
            AppUserId = user.Id,
            AppUser = user,
            Dishes = orderDishes
        };

        if(!await _unitOfWork.OrderRepository.AddOrderAsync(order))
        {
            return BadRequest("Error by making order");
        }

        await _orderStatusNotificator.NotifyAsync(order);

        return Ok();
    }
    
    [HttpPatch]
    [Authorize(Policy = "Admin")]
    public async Task<ActionResult> UpdateOrderStatus(OrderStatusDto orderStatusDto)
    {
        if (orderStatusDto.Status == null)
        {
            return BadRequest($"Status {orderStatusDto.Status} is not supported");
        }

        var order = await _unitOfWork.OrderRepository.GetOrderAsync(orderStatusDto.OrderId);

        if(order == null)
        {
            return BadRequest($"Order with {orderStatusDto.OrderId} ID not found");
        }

        order.Status = orderStatusDto.Status;

        if(!await _unitOfWork.OrderRepository.UpdateOrderStatusAsync(order.Id, order.Status))
        {
            return BadRequest("Error by updating order status");
        }

        await _orderStatusNotificator.NotifyAsync(order);

        return Ok();
    }
}
