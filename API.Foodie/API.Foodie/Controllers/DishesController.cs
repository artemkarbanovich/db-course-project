using API.Foodie.DTOs;
using API.Foodie.Helpers.QueryParams;
using API.Foodie.Interfaces;
using API.Foodie.Interfaces.Data;
using API.Foodie.Model;

namespace API.Foodie.Controllers;

[Authorize(Policy = "Admin")]
public class DishesController : BaseApiController
{
    private readonly IUnitOfWork _unitOfWork;
    private readonly IMapper _mapper;
    private readonly IPhotoService _photoService;

    public DishesController(IUnitOfWork unitOfWork, IMapper mapper, IPhotoService photoService)
    {
        _unitOfWork = unitOfWork;
        _mapper = mapper;
        _photoService = photoService;
    }


    [HttpPost]
    public async Task<ActionResult> AddDish([FromForm] DishAddDto dishAddDto)
    {
        var dish = _mapper.Map<Dish>(dishAddDto);
        dish.Photos = new List<Photo>();

        foreach (var f in dishAddDto.Photos)
        {
            var uploadResult = await _photoService.AddPhotoAsync(f);

            if (uploadResult.Error == null)
            {
                dish.Photos.Add(new Photo()
                {
                    Url = uploadResult.SecureUrl.AbsoluteUri,
                    PublicId = uploadResult.PublicId
                });

                continue;
            }
            else if (uploadResult.Error != null)
            {
                foreach (var p in dish.Photos)
                {
                    await _photoService.DeletePhotoAsync(p.PublicId);
                }

                return BadRequest("Error by uploading dish photos");
            }
        }

        if (!await _unitOfWork.DishRepository.AddDishAsync(dish))
        {
            foreach (var p in dish.Photos)
            {
                await _photoService.DeletePhotoAsync(p.PublicId);
            }

            return BadRequest("Error by adding dish");
        }

        return Ok();
    }

    [HttpGet("admin-list")]
    public async Task<ActionResult<List<DishAdminListDto>>> GetAdminList([FromQuery] DishAdminListParams queryParams)
    {
        var dishAdminListDto = await _unitOfWork.DishRepository.GetAdminList(queryParams);

        if(dishAdminListDto == null)
        {
            return BadRequest("Error by getting admin's dish list");
        }

        return dishAdminListDto;
    }
}
