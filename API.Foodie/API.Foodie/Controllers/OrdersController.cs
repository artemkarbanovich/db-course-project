using API.Foodie.DTOs;
using API.Foodie.Extensions;
using API.Foodie.Interfaces;
using API.Foodie.Interfaces.Data;
using API.Foodie.Model;

namespace API.Foodie.Controllers;

public class OrdersController : BaseApiController
{
    private readonly IUnitOfWork _unitOfWork;
    private readonly IMailerService _mailer;

    public OrdersController(IUnitOfWork unitOfWork, IMailerService mailer)
    {
        _unitOfWork = unitOfWork;
        _mailer = mailer;
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
            Dishes = orderDishes
        };

        if(!await _unitOfWork.OrderRepository.AddOrderAsync(order))
        {
            return BadRequest("Error by making order");
        }

        await _mailer.SendEmailAsync(user.Email, "Order accepted",
            $"<h2>{user.FirstName} {user.LastName}, your order has been successfully accepted. Expect delivery.</h2>" +
            $"<h4>Total price: {Math.Round(order.TotalPrice, 2)} BYN</h4>" +
            $"<p>With respect, Foodie!</p>");

        return Ok();
    }
}
