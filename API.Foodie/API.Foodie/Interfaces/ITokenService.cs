using API.Foodie.Model;

namespace API.Foodie.Interfaces;

public interface ITokenService
{
    string CreateToken(AppUser user);
}
