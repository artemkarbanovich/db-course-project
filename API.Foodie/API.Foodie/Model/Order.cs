namespace API.Foodie.Model;

public class Order
{
    public int Id { get; set; }
    public DateTime OrderDate { get; set; }
    public DateTime DeliveryDate { get; set; }
    public decimal TotalPrice { get; set; }
    public string Status { get; set; }
    public string Address { get; set; }

    public int AppUserId { get; set; }
    public AppUser AppUser { get; set; }
    public List<OrderDish> Dishes { get; set; }
}
