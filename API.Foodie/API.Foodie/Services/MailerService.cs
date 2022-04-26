using API.Foodie.Interfaces;

namespace API.Foodie.Services;

public class MailerService : IMailerService
{
    private readonly IConfiguration _config;

    public MailerService(IConfiguration config)
    {
        _config = config;
    }

    public async Task SendEmailAsync(string recipient, string subject, string message)
    {
        var email = new MimeMessage();

        email.From.Add(new MailboxAddress(
            _config.GetValue<string>("MailerOptions:SenderName"),
            _config.GetValue<string>("MailerOptions:SenderEmail")));

        email.To.Add(MailboxAddress.Parse(recipient));
        email.Subject = subject;
        email.Body = new TextPart(TextFormat.Html) { Text = message };


        using var smtp = new SmtpClient();

        await smtp.ConnectAsync(
            _config.GetValue<string>("MailerOptions:Server"),
            _config.GetValue<int>("MailerOptions:Port"),
            SecureSocketOptions.StartTls);

        await smtp.AuthenticateAsync(
            _config.GetValue<string>("MailerOptions:SenderEmail"),
            _config.GetValue<string>("MailerOptions:Password"));

        await smtp.SendAsync(email);
        await smtp.DisconnectAsync(true);
    }
}
