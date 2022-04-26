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
        var dishAdminListDto = await _unitOfWork.DishRepository.GetAdminListAsync(queryParams);

        if (dishAdminListDto == null)
        {
            return BadRequest("Error by getting admin's dish list");
        }

        return dishAdminListDto;
    }

    [HttpPut]
    public async Task<ActionResult> UpdateDish(DishUpdateDto dishUpdateDto)
    {
        if (await _unitOfWork.DishRepository.GetDishAsync(dishUpdateDto.Id) == null)
        {
            return NotFound($"Dish with ID {dishUpdateDto.Id} was not found");
        }

        if (!await _unitOfWork.DishRepository.UpdateDishAsync(_mapper.Map<Dish>(dishUpdateDto)))
        {
            return BadRequest("Error by updating dish");
        }

        return Ok();
    }

    [HttpGet("{id}")]
    public async Task<ActionResult<DishDto>> GetDish(int id)
    {
        var dish = await _unitOfWork.DishRepository.GetDishAsync(id);

        if(dish == null)
        {
            return NotFound($"Dish with ID {id} was not found");
        }

        var dishDto = _mapper.Map<DishDto>(dish);
        dishDto.Photos = new List<PhotoDto>();

        foreach(var p in dish.Photos)
        {
            dishDto.Photos.Add(_mapper.Map<PhotoDto>(p));
        }

        return dishDto;
    }

    [HttpPost("{id}/photos")]
    public async Task<ActionResult<List<PhotoDto>>> AddPhotos([FromForm] List<IFormFile> files, int id)
    {
        if (await _unitOfWork.DishRepository.GetDishAsync(id) == null)
        {
            return NotFound($"Dish with ID {id} was not found");
        }
        else if(files.Count == 0)
        {
            return BadRequest("You must attach photos");
        }

        var photos = new List<Photo>();

        foreach (var f in files)
        {
            var uploadResult = await _photoService.AddPhotoAsync(f);

            if (uploadResult.Error == null)
            {
                photos.Add(new Photo()
                {
                    Url = uploadResult.SecureUrl.AbsoluteUri,
                    PublicId = uploadResult.PublicId,
                    DishId = id
                });

                continue;
            }
            else if (uploadResult.Error != null)
            {
                foreach (var p in photos)
                {
                    await _photoService.DeletePhotoAsync(p.PublicId);
                }

                return BadRequest("Error by uploading dish photos");
            }
        }

        await _unitOfWork.DishRepository.AddDishPhotosAsync(photos);

        var photosDto = await _unitOfWork.DishRepository.GetPhotosAsync(id);

        if(photos == null)
        {
            return BadRequest("Error by getting photos");
        }

        return photosDto;
    }

    [HttpDelete("photos/{id}")]
    public async Task<ActionResult> DeletePhoto(int id)
    {
        var photo = await _unitOfWork.DishRepository.GetPhotoAsync(id);

        if(photo == null)
        {
            return NotFound("Photo not found");
        }

        var deletionResult = await _photoService.DeletePhotoAsync(photo.PublicId);

        if(!await _unitOfWork.DishRepository.DeletePhotoAsync(photo) || deletionResult.Error != null)
        {
            return BadRequest("Error by deleting photo");
        }

        return Ok();
    }
}
