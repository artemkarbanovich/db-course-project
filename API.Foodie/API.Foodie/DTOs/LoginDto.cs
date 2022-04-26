namespace API.Foodie.DTOs;

public class LoginDto
{
    [EmailAddress]
    public string Email { get; set; }

    [StringLength(32, MinimumLength = 6)]
    public string Password { get; set; }
}
