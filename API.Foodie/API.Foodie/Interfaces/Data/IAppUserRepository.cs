using API.Foodie.Model;

namespace API.Foodie.Interfaces.Data;

public interface IAppUserRepository
{
    Task<bool> AddUserAsync(AppUser user);
    Task<bool> IsUserExistAsync<T>(T identifier);
    Task<AppUser> GetUserAsync<T>(T identifier);
    Task<bool> UpdateUserAsync(AppUser user);
}
