namespace API.Foodie.Model;

public class OrderDish
{
    public int Id { get; set; }
    public int DishesCount { get; set; }

    public int DishId { get; set; }
    public Dish Dish { get; set; }
}
