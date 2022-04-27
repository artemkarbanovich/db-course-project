namespace API.Foodie.DTOs;

public class OrderDishAdminListDto
{
    public int DishesCount { get; set; }
    public string Name { get; set; }
    public TimeSpan CookingTime { get; set; }
    public int DishWeight { get; set; }
    public decimal Price { get; set; }
    public string Ingredients { get; set; }
}
