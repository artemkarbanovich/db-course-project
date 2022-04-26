using API.Foodie.DTOs;
using API.Foodie.Model;

namespace API.Foodie.Helpers;

public class AutoMapperProfile : Profile
{
    public AutoMapperProfile()
    {
        CreateMap<RegisterDto, AppUser>();
        CreateMap<UserUpdateDto, AppUser>();
        CreateMap<DishAddDto, Dish>()
            .ForMember(d => d.Photos, opt => opt.Ignore());

        CreateMap<AppUser, UserDto>();
    }
}
