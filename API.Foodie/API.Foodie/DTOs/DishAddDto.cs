namespace API.Foodie.DTOs;

public class DishAddDto
{
    [Required]
    [StringLength(25)]
    public string Name { get; set; }

    [Required]
    public TimeSpan CookingTime { get; set; }

    [StringLength(120, MinimumLength = 3)]
    public string YouWillNeed { get; set; }

    [Range(1, 2000)]
    public int DishWeight { get; set; }

    [Range(1, 500)]
    public decimal Price { get; set; }

    [Required]
    public bool IsVisible { get; set; }

    [StringLength(120, MinimumLength = 3)]
    public string Ingredients { get; set; }

    [Required]
    public List<IFormFile> Photos { get; set; }
}
