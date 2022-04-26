namespace API.Foodie.Model;

public class Photo
{
    public int Id { get; set; }
    public string Url { get; set; }
    public string PublicId { get; set; }

    public int DishId { get; set; }
    public Dish Dish { get; set; }
}
