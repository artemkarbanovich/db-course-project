using API.Foodie.Helpers;
using API.Foodie.Interfaces;

namespace API.Foodie.Services;

public class PhotoService : IPhotoService
{
    private readonly Cloudinary _cloudinary;

    public PhotoService(IOptions<CloudinarySettings> config)
    {
        _cloudinary = new Cloudinary(new Account(config.Value.CloudName, config.Value.ApiKey, config.Value.ApiSecret));
    }
    
    public async Task<ImageUploadResult> AddPhotoAsync(IFormFile imageFile)
    {
        var uploadResult = new ImageUploadResult();

        if (imageFile.Length > 0)
        {
            using var stream = imageFile.OpenReadStream();
            var uploadParams = new ImageUploadParams { File = new FileDescription(imageFile.FileName, stream) };

            uploadResult = await _cloudinary.UploadAsync(uploadParams);
        }

        return uploadResult;
    }

    public async Task<DeletionResult> DeletePhotoAsync(string publicId)
    {
        return await _cloudinary.DestroyAsync(new DeletionParams(publicId));
    }
}
