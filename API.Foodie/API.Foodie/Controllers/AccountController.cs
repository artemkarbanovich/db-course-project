using API.Foodie.DTOs;
using API.Foodie.Extensions;
using API.Foodie.Interfaces;
using API.Foodie.Interfaces.Data;
using API.Foodie.Model;

namespace API.Foodie.Controllers;

public class AccountController : BaseApiController
{
    private readonly IUnitOfWork _unitOfWork;
    private readonly IMapper _mapper;
    private readonly ITokenService _tokenService;

    public AccountController(IUnitOfWork unitOfWork, IMapper mapper, ITokenService tokenService)
    {
        _unitOfWork = unitOfWork;
        _mapper = mapper;
        _tokenService = tokenService;
    }


    [HttpPost("register")]
    public async Task<ActionResult<AccountDto>> Register(RegisterDto registerDto)
    {
        registerDto.Email = registerDto.Email.ToLower();

        if (await _unitOfWork.AppUserRepository.IsUserExistAsync(registerDto.Email))
        {
            return BadRequest("Email is taken");
        }

        var user = _mapper.Map<AppUser>(registerDto);

        using var hmac = new HMACSHA512();

        user.RegistrationDate = DateTime.Now;
        user.UserRole = "User";
        user.PasswordHash = hmac.ComputeHash(Encoding.UTF8.GetBytes(registerDto.Password));
        user.PasswordSalt = hmac.Key;

        if(!await _unitOfWork.AppUserRepository.AddUserAsync(user))
        {
            return BadRequest("Unknown registration error");
        }

        return new AccountDto()
        {
            Email = user.Email,
            Token = _tokenService.CreateToken(user)
        };
    }

    [HttpPost("login")]
    public async Task<ActionResult<AccountDto>> Login(LoginDto loginDto)
    {
        loginDto.Email = loginDto.Email.ToLower();

        var user = await _unitOfWork.AppUserRepository.GetUserAsync(loginDto.Email);

        if (user == null)
        {
            return NotFound("Invalid email");
        }

        if (!Request.IsMobile() && user.UserRole != "Admin")
        {
            return Unauthorized("Only admin can login");
        }
        else if (Request.IsMobile() && user.UserRole != "User")
        {
            return Unauthorized("Only user can login");
        }

        using var hmac = new HMACSHA512(user.PasswordSalt);

        var computedHash = hmac.ComputeHash(Encoding.UTF8.GetBytes(loginDto.Password));

        for (int i = 0; i < computedHash.Length; i++)
        {
            if (computedHash[i] != user.PasswordHash[i])
            {
                return Unauthorized("Invalid password");
            }
        }

        return new AccountDto()
        {
            Email = user.Email,
            Token = _tokenService.CreateToken(user)
        };
    }
}
