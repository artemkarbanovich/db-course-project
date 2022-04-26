namespace API.Foodie.DTOs;

public class UserUpdateDto
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
}
