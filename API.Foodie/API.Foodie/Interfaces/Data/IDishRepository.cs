using API.Foodie.DTOs;
using API.Foodie.Helpers.QueryParams;
using API.Foodie.Model;

namespace API.Foodie.Interfaces.Data;

public interface IDishRepository
{
    Task<bool> AddDishAsync(Dish dish);
    Task<List<DishAdminListDto>> GetAdminListAsync(DishAdminListParams queryParams);
    Task<Dish> GetDishAsync(int id);
    Task<bool> UpdateDishAsync(Dish dish);
    Task AddDishPhotosAsync(List<Photo> photos);
    Task<List<PhotoDto>> GetPhotosAsync(int dishId);
    Task<Photo> GetPhotoAsync(int id);
    Task<bool> DeletePhotoAsync(Photo photo);
    Task<List<DishUserListDto>> GetUserListAsync(DishUserListParams queryParams);
}
