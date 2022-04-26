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

    public async Task<List<DishAdminListDto>> GetAdminList(DishAdminListParams queryParams)
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

        using SqlDataReader reader = await command.ExecuteReaderAsync();

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
}
