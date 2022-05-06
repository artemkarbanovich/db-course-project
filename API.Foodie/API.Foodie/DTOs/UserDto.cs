namespace API.Foodie.DTOs;

public class UserDto
{
    public int Id { get; set; }
    public string Email { get; set; }
    public string FirstName { get; set; }
    public string LastName { get; set; }
    public DateTime RegistrationDate { get; set; }
    public DateOnly Birthday { get; set; }
    public string PhoneNumber { get; set; }
}
