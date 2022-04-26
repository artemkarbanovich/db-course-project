namespace API.Foodie.DTOs;

public class RegisterDto
{
    [EmailAddress]
    public string Email { get; set; }

    [StringLength(25, MinimumLength = 2)]
    public string FirstName { get; set; }

    [StringLength(25, MinimumLength = 2)]
    public string LastName { get; set; }

    [Required]
    public DateOnly Birthday { get; set; }

    [Phone]
    [StringLength(25)]
    public string PhoneNumber { get; set; }

    [StringLength(32, MinimumLength = 6)]
    public string Password { get; set; }
}
