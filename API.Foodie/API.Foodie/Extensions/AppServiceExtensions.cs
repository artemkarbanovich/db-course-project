using API.Foodie.Data;
using API.Foodie.Helpers;
using API.Foodie.Interfaces;
using API.Foodie.Interfaces.Data;
using API.Foodie.Services;

namespace API.Foodie.Extensions;

public static class AppServiceExtensions
{
    public static IServiceCollection AddAppServices(this IServiceCollection services, IConfiguration config)
    {
        services.AddScoped<IMailerService, MailerService>();
        services.AddScoped<IPhotoService, PhotoService>();
        services.AddScoped<ITokenService, TokenService>();
        services.AddScoped<IUnitOfWork, UnitOfWork>();
        services.AddAutoMapper(typeof(AutoMapperProfile).Assembly);
        services.Configure<CloudinarySettings>(config.GetSection("CloudinarySettings"));

        return services;
    }
}
