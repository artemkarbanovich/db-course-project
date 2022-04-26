﻿namespace API.Foodie.DTOs;

public class DishUserListDto
{
    public int Id { get; set; }
    public string Name { get; set; }
    public TimeSpan CookingTime { get; set; }
    public string YouWillNeed { get; set; }
    public int DishWeight { get; set; }
    public decimal Price { get; set; }
    public string Ingredients { get; set; }

    public List<PhotoDto> Photos { get; set; }
}
