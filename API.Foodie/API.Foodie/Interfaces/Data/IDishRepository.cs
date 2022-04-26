using API.Foodie.DTOs;
using API.Foodie.Helpers.QueryParams;
using API.Foodie.Model;

namespace API.Foodie.Interfaces.Data;

public interface IDishRepository
{
    Task<bool> AddDishAsync(Dish dish);
    Task<List<DishAdminListDto>> GetAdminList(DishAdminListParams queryParams);
}
