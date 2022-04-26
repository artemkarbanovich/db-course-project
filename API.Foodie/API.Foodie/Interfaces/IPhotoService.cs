namespace API.Foodie.Interfaces;

public interface IPhotoService
{
    Task<ImageUploadResult> AddPhotoAsync(IFormFile imageFile);
    Task<DeletionResult> DeletePhotoAsync(string publicId);
}
