using API.Foodie.DTOs;

namespace API.Foodie.Interfaces.Data;

public interface IStatRepository
{
    Task<StatAdminDto> GetAdminStatAsync();
    Task<StatUserDto> GetUserStatAsync(int userId);
}
