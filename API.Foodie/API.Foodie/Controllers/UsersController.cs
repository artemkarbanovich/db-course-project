using API.Foodie.DTOs;
using API.Foodie.Extensions;
using API.Foodie.Interfaces.Data;
using API.Foodie.Model;

namespace API.Foodie.Controllers;

public class UsersController : BaseApiController
{
    private readonly IUnitOfWork _unitOfWork;
    private readonly IMapper _mapper;

    public UsersController(IUnitOfWork unitOfWork, IMapper mapper)
    {
        _unitOfWork = unitOfWork;
        _mapper = mapper;
    }


    [HttpGet("{email}")]
    [Authorize]
    public async Task<ActionResult<UserDto>> GetUser(string email)
    {
        email = email.ToLower();

        var requester = await _unitOfWork.AppUserRepository.GetUserAsync(User.GetId());
        var user = await _unitOfWork.AppUserRepository.GetUserAsync(email);

        if (user == null)
        {
            return NotFound("User's personal data not found");
        }
        else if (requester.Id != user.Id && requester.UserRole != "Admin")
        {
            return Unauthorized();
        }

        return _mapper.Map<AppUser, UserDto>(user);
    }

    [HttpPut]
    [Authorize(Policy = "User")]
    public async Task<ActionResult> UpdateUser(UserUpdateDto userUpdateDto)
    {
        userUpdateDto.Email = userUpdateDto.Email.ToLower();

        var requester = await _unitOfWork.AppUserRepository.GetUserAsync(User.GetId());
        var emailTaker = await _unitOfWork.AppUserRepository.GetUserAsync(userUpdateDto.Email);

        if (emailTaker != null && emailTaker.Id != requester.Id)
        {
            return BadRequest("Email is taken");
        }

        var user = _mapper.Map<AppUser>(userUpdateDto);
        user.Id = requester.Id;

        await _unitOfWork.AppUserRepository.UpdateUserAsync(user);

        return Ok();
    }
}
