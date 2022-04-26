using API.Foodie.Interfaces;
using API.Foodie.Model;

namespace API.Foodie.Services;

public class TokenService : ITokenService
{
    private readonly SymmetricSecurityKey _securityKey;

    public TokenService(IConfiguration config)
    {
        _securityKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(config["JwtTokenKey"]));
    }

    public string CreateToken(AppUser user)
    {
        var claims = new[]
        {
            new Claim(ClaimTypes.NameIdentifier, user.Id.ToString()),
            new Claim(ClaimTypes.Role, user.UserRole)
        };

        var tokenDescriptor = new SecurityTokenDescriptor()
        {
            Subject = new ClaimsIdentity(claims),
            Expires = DateTime.UtcNow.AddDays(7),
            SigningCredentials = new SigningCredentials(_securityKey, SecurityAlgorithms.HmacSha512Signature)
        };

        var tokenHandler = new JwtSecurityTokenHandler();

        return tokenHandler.WriteToken(tokenHandler.CreateToken(tokenDescriptor));
    }
}
