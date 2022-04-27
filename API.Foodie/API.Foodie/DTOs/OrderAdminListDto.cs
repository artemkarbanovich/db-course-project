namespace API.Foodie.DTOs;

public class OrderAdminListDto
{
    public int Id { get; set; }
    public DateTime OrderDate { get; set; }
    public DateTime DeliveryDate { get; set; }
    public decimal TotalPrice { get; set; }
    public string Status { get; set; }
    public string Address { get; set; }

    public string Email { get; set; }
    public string FirstName { get; set; }
    public string LastName { get; set; }
    public string PhoneNumber { get; set; }

    public List<OrderDishAdminListDto> Dishes { get; set; }
}
