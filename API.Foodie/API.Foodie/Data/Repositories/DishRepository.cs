using API.Foodie.DTOs;
using API.Foodie.Helpers.QueryParams;
using API.Foodie.Interfaces.Data;
using API.Foodie.Model;

namespace API.Foodie.Data.Repositories;

public class DishRepository : IDishRepository
{
    private readonly SqlConnection _connection;

    public DishRepository(SqlConnection connection)
    {
        _connection = connection;
    }


    public async Task<bool> AddDishAsync(Dish dish)
    {
        var command = new SqlCommand()
        {
            CommandText = "dbo.AddDish",
            Connection = _connection,
            CommandType = CommandType.StoredProcedure
        };

        command.Parameters.AddWithValue("@name", dish.Name);
        command.Parameters.AddWithValue("@cookingTime", dish.CookingTime.ToString());
        command.Parameters.AddWithValue("@youWillNeed", dish.YouWillNeed);
        command.Parameters.AddWithValue("@dishWeight", dish.DishWeight);
        command.Parameters.AddWithValue("@price", dish.Price);
        command.Parameters.AddWithValue("@ingredients", dish.Ingredients);
        command.Parameters.AddWithValue("@isVisible", dish.IsVisible);

        var photoList = new DataTable();
        photoList.Columns.Add(new DataColumn("Url", typeof(string)));
        photoList.Columns.Add(new DataColumn("PublicId", typeof(string)));

        foreach (var p in dish.Photos)
        {
            DataRow row = photoList.NewRow();

            row["Url"] = p.Url;
            row["PublicId"] = p.PublicId;

            photoList.Rows.Add(row);
        }

        SqlParameter photoListParam = command.Parameters.AddWithValue("@list", photoList);
        photoListParam.SqlDbType = SqlDbType.Structured;
        photoListParam.TypeName = "dbo.PhotoList";

        await _connection.OpenAsync();

        int executeResult = (int)await command.ExecuteScalarAsync();

        await _connection.CloseAsync();

        return executeResult == 1 ? true : false;
    }

    public async Task<List<DishAdminListDto>> GetAdminListAsync(DishAdminListParams queryParams)
    {
        var command = new SqlCommand()
        {
            CommandText = "dbo.GetDishAdminList",
            Connection = _connection,
            CommandType = CommandType.StoredProcedure
        };

        command.Parameters.AddWithValue("@nameSearchStr", queryParams.NameSearchStr);
        command.Parameters.AddWithValue("@orderBy", queryParams.OrderBy);
        command.Parameters.AddWithValue("@orderByType", queryParams.OrderByType);
        command.Parameters.AddWithValue("@isVisible", queryParams.IsVisible);

        await _connection.OpenAsync();

        using var reader = await command.ExecuteReaderAsync();

        var dishAdminListDto = new List<DishAdminListDto>();

        if (!reader.HasRows)
        {
            return dishAdminListDto;
        }

        while (await reader.ReadAsync())
        {
            var dishAdminList = new DishAdminListDto()
            {
                Id = (int)reader["DishId"],
                Name = (string)reader["Name"],
                Price = (decimal)reader["Price"],
                Photos = new List<PhotoDto>()
            };
            dishAdminList.Photos.Add(new PhotoDto()
            {
                Id = (int)reader["PhotoId"],
                Url = (string)reader["Url"]
            });
            
            var dishById = dishAdminListDto.SingleOrDefault(d => d.Id == dishAdminList.Id);

            if (dishById == null)
            {
                dishAdminListDto.Add(dishAdminList);
            }
            else
            {
                dishById.Photos.AddRange(dishAdminList.Photos);
            }
        }

        await _connection.CloseAsync();

        return dishAdminListDto;
    }

    public async Task<Dish> GetDishAsync(int id)
    {
        var command = new SqlCommand() 
        { 
            Connection = _connection,
            CommandText = 
                "SELECT Dishes.Id AS DishId, Name, CookingTime, YouWillNeed, DishWeight, Price, IsVisible, Ingredients, Photos.Id AS PhotoId, Url " +
                "FROM Dishes JOIN Photos ON Dishes.Id = Photos.DishId " +
                "WHERE Dishes.Id = @id;"
        };

        command.Parameters.AddWithValue("@id", id);

        await _connection.OpenAsync();

        using var reader = await command.ExecuteReaderAsync();

        if (!reader.HasRows || !await reader.ReadAsync())
        {
            return null;
        }

        var dish = new Dish()
        {
            Id = (int)reader["DishId"],
            Name = (string)reader["Name"],
            CookingTime = (TimeSpan)reader["CookingTime"],
            YouWillNeed = (string)reader["YouWillNeed"],
            DishWeight = (int)reader["DishWeight"],
            Price = (decimal)reader["Price"],
            IsVisible = (bool)reader["IsVisible"],
            Ingredients = (string)reader["Ingredients"],
            Photos = new List<Photo>()
        };
        dish.Photos.Add(new Photo()
        {
            Id = (int)reader["PhotoId"],
            Url = (string)reader["Url"]
        });
        
        while (await reader.ReadAsync())
        {
            dish.Photos.Add(new Photo()
            {
                Id = (int)reader["PhotoId"],
                Url = (string)reader["Url"]
            });
        }

        await _connection.CloseAsync();

        return dish;
    }

    public async Task<bool> UpdateDishAsync(Dish dish)
    {
        var command = new SqlCommand() 
        { 
            Connection = _connection,
            CommandText =
                "UPDATE Dishes SET Name = @name, CookingTime = @cookingTime, YouWillNeed = @youWillNeed, DishWeight = @dishWeight, Price = @price, IsVisible = @isVisible, Ingredients = @ingredients " +
                "WHERE Id = @id;"
        };

        command.Parameters.AddWithValue("@name", dish.Name);
        command.Parameters.AddWithValue("@cookingTime", dish.CookingTime.ToString());
        command.Parameters.AddWithValue("@youWillNeed", dish.YouWillNeed);
        command.Parameters.AddWithValue("@dishWeight", dish.DishWeight);
        command.Parameters.AddWithValue("@price", dish.Price);
        command.Parameters.AddWithValue("@isVisible", dish.IsVisible);
        command.Parameters.AddWithValue("@ingredients", dish.Ingredients);
        command.Parameters.AddWithValue("@id", dish.Id);

        await _connection.OpenAsync();

        int updatedRows = await command.ExecuteNonQueryAsync();

        await _connection.CloseAsync();

        return updatedRows > 0;
    }

    public async Task AddDishPhotosAsync(List<Photo> photos)
    {
        var command = new SqlCommand()
        {
            Connection = _connection,
            CommandText =
                "INSERT INTO Photos (Url, PublicId, DishId) " +
                "VALUES (@url, @publicId, @dishId);"
        };

        await _connection.OpenAsync();

        foreach (var p in photos)
        {
            command.Parameters.AddWithValue("@url", p.Url);
            command.Parameters.AddWithValue("@publicId", p.PublicId);
            command.Parameters.AddWithValue("@dishId", p.DishId);

            await command.ExecuteNonQueryAsync();
        }

        await _connection.CloseAsync();
    }

    public async Task<List<PhotoDto>> GetPhotosAsync(int dishId)
    {
        var command = new SqlCommand()
        {
            Connection = _connection,
            CommandText = "SELECT Id, Url FROM Photos WHERE DishId = @dishId"
        };

        command.Parameters.AddWithValue("@dishId", dishId);

        await _connection.OpenAsync();

        using var reader = await command.ExecuteReaderAsync();

        if (!reader.HasRows)
        {
            return null;
        }

        var photos = new List<PhotoDto>();

        while (await reader.ReadAsync())
        {
            photos.Add(new PhotoDto()
            {
                Id = (int)reader["Id"],
                Url = (string)reader["Url"]
            });
        }

        await _connection.CloseAsync();

        return photos;
    }

    public async Task<Photo> GetPhotoAsync(int id)
    {
        var command = new SqlCommand()
        {
            Connection = _connection,
            CommandText = "SELECT * FROM Photos WHERE Id = @id"
        };

        command.Parameters.AddWithValue("@id", id);

        await _connection.OpenAsync();

        using var reader = await command.ExecuteReaderAsync();

        if (!reader.HasRows || !await reader.ReadAsync())
        {
            return null;
        }

        var photo = new Photo()
        {
            Id = (int)reader["Id"],
            Url = (string)reader["Url"],
            PublicId = (string)reader["PublicId"],
            DishId = (int)reader["dishId"]
        };

        await _connection.CloseAsync();

        return photo;
    }

    public async Task<bool> DeletePhotoAsync(Photo photo)
    {
        var command = new SqlCommand()
        {
            Connection = _connection,
            CommandText = "DELETE FROM Photos WHERE Id = @id"
        };

        command.Parameters.AddWithValue("@id", photo.Id);

        await _connection.OpenAsync();

        int deletedRows = await command.ExecuteNonQueryAsync();

        await _connection.CloseAsync();

        return deletedRows > 0;
    }

    public async Task<List<DishUserListDto>> GetUserListAsync(DishUserListParams queryParams)
    {
        var command = new SqlCommand()
        {
            CommandText = "dbo.GetDishUserList",
            Connection = _connection,
            CommandType = CommandType.StoredProcedure
        };

        command.Parameters.AddWithValue("@nameSearchStr", queryParams.NameSearchStr);
        command.Parameters.AddWithValue("@orderBy", queryParams.OrderBy);
        command.Parameters.AddWithValue("@orderByType", queryParams.OrderByType);

        await _connection.OpenAsync();

        using var reader = await command.ExecuteReaderAsync();

        var dishUserListDto = new List<DishUserListDto>();

        if (!reader.HasRows)
        {
            return dishUserListDto;
        }

        while (await reader.ReadAsync())
        {
            var dishUserList = new DishUserListDto()
            {
                Id = (int)reader["DishId"],
                Name = (string)reader["Name"],
                CookingTime = (TimeSpan)reader["CookingTime"],
                YouWillNeed = (string)reader["YouWillNeed"],
                DishWeight = (int)reader["DishWeight"],
                Price = (decimal)reader["Price"],
                Ingredients = (string)reader["Ingredients"],
                Photos = new List<PhotoDto>()
            };
            dishUserList.Photos.Add(new PhotoDto()
            {
                Id = (int)reader["PhotoId"],
                Url = (string)reader["Url"]
            });

            var dishById = dishUserListDto.SingleOrDefault(d => d.Id == dishUserList.Id);

            if (dishById == null)
            {
                dishUserListDto.Add(dishUserList);
            }
            else
            {
                dishById.Photos.AddRange(dishUserList.Photos);
            }
        }

        await _connection.CloseAsync();

        return dishUserListDto;
    }
}
