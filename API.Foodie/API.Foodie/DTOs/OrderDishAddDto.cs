namespace API.Foodie.DTOs;

public class OrderDishAddDto
{
    [Required]
    [Range(1, int.MaxValue)]
    public int DishesCount { get; set; }

    [Required]
    [Range(1, int.MaxValue)]
    public int DishId { get; set; }
}
