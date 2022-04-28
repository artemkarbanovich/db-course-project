using API.Foodie.DTOs;
using API.Foodie.Extensions;
using API.Foodie.Interfaces.Data;

namespace API.Foodie.Controllers;

public class StatisticsController : BaseApiController
{
    private readonly IUnitOfWork _unitOfWork;

    public StatisticsController(IUnitOfWork unitOfWork)
    {
        _unitOfWork = unitOfWork;
    }


    [HttpGet("admin")]
    [Authorize(Policy = "Admin")]
    public async Task<ActionResult<StatAdminDto>> GetAdminStat()
    {
        return await _unitOfWork.StatRepository.GetAdminStatAsync();
    }

    [HttpGet("user")]
    [Authorize(Policy = "User")]
    public async Task<ActionResult<StatUserDto>> GetUserStat()
    {
        if(await _unitOfWork.AppUserRepository.GetUserAsync(User.GetId()) == null)
        {
            return NotFound("User not found");
        }

        return await _unitOfWork.StatRepository.GetUserStatAsync(User.GetId());
    }
}
