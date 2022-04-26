namespace API.Foodie.Interfaces;

public interface IMailerService
{
    Task SendEmailAsync(string recipient, string subject, string message);
}
