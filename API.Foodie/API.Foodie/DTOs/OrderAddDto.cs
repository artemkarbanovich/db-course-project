namespace API.Foodie.DTOs;

public class OrderAddDto
{
    [Required]
    public DateTime DeliveryDate { get; set; }

    [StringLength(120, MinimumLength = 5)]
    public string Address { get; set; }

    [Required]
    public List<OrderDishAddDto> Dishes { get; set; }
}
