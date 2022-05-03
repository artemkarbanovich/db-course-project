namespace API.Foodie.DTOs;

public class DishAdminListDto
{
    public int Id { get; set; }
    public string Name { get; set; }
    public decimal Price { get; set; }
    public bool IsVisible { get; set; }

    public List<PhotoDto> Photos { get; set; }
}
