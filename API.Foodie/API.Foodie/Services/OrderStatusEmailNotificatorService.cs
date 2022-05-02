using API.Foodie.Interfaces;
using API.Foodie.Model;

namespace API.Foodie.Services;

public class OrderStatusEmailNotificatorService : IOrderStatusNotificatorService
{
    private readonly IMailerService _mailer;

    public OrderStatusEmailNotificatorService(IMailerService mailer)
    {
        _mailer = mailer;
    }

    public async Task NotifyAsync(Order order)
    {
        string subject;
        string message;

        switch (order.Status)
        {
            case "ACCEPTED":
                subject = "Order is accepted";
                message =
                    $"<h3>{order.AppUser.FirstName} {order.AppUser.LastName}, your order has been successfully accepted {DateTime.Now:dd.MM.yyyy} at {DateTime.Now:HH:mm}. Expect delivery.</h3>" +
                    $"<h4>Total price: {Math.Round(order.TotalPrice, 2)} BYN</h4>" +
                    $"<p>With respect, Foodie!</p>";
                break;

            case "IN_WAY":
                subject = "Courier on the way";
                message =
                    $"<h3>{order.AppUser.FirstName} {order.AppUser.LastName}, your order is on the way. Waiting for an hour at {order.Address}.</h3>" +
                    $"<h4>Total price: {Math.Round(order.TotalPrice, 2)} BYN</h4>" +
                    $"<p>With respect, Foodie!</p>";
                break;

            case "DELIVERED":
                subject = "Order is delivered";
                message =
                    $"<h3>{order.AppUser.FirstName} {order.AppUser.LastName}, your order is successfully delivered {DateTime.Now:dd.MM.yyyy} at {DateTime.Now:HH:mm}. If you have any questions contact support.</h3>" +
                    $"<p>With respect, Foodie!</p>";
                break;

            case "CANCELED":
                subject = "Order is canceled";
                message =
                    $"<h3>{order.AppUser.FirstName} {order.AppUser.LastName}, your order is canceled. If you have any questions contact support.</h3>" +
                    $"<p>With respect, Foodie!</p>";
                break;

            default: return;
        }

        await _mailer.SendEmailAsync(order.AppUser.Email, subject, message);
    }
}
