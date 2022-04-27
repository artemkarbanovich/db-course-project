namespace API.Foodie.DTOs;

public class OrderUserListDto
{
    public DateTime OrderDate { get; set; }
    public DateTime DeliveryDate { get; set; }
    public decimal TotalPrice { get; set; }
    public string Status { get; set; }
    public string Address { get; set; }
}
