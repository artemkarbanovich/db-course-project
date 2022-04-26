using API.Foodie.DTOs;
using API.Foodie.Interfaces.Data;
using API.Foodie.Model;

namespace API.Foodie.Data.Repositories;

public class AppUserRepository : IAppUserRepository
{
    private readonly SqlConnection _connection;

    public AppUserRepository(SqlConnection connection)
    {
        _connection = connection;
    }


    public async Task<bool> AddUserAsync(AppUser user)
    {
        var command = new SqlCommand() { Connection = _connection };

        command.CommandText = 
            "INSERT INTO AppUsers (Email, FirstName, LastName, RegistrationDate, Birthday, PhoneNumber, UserRole, PasswordHash, PasswordSalt) " +
            "OUTPUT inserted.Id " +
            "VALUES (@email, @firstName, @lastName, @registrationDate, @birthday, @phoneNumber, @userRole, @passwordHash, @passwordSalt);";

        command.Parameters.AddWithValue("@email", user.Email);
        command.Parameters.AddWithValue("@firstName", user.FirstName);
        command.Parameters.AddWithValue("@lastName", user.LastName);
        command.Parameters.AddWithValue("@registrationDate", user.RegistrationDate.ToString("yyyy-MM-ddTHH:mm:ss"));
        command.Parameters.AddWithValue("@birthday", user.Birthday.ToString("yyyy-MM-dd"));
        command.Parameters.AddWithValue("@phoneNumber", user.PhoneNumber);
        command.Parameters.AddWithValue("@userRole", user.UserRole);
        command.Parameters.AddWithValue("@passwordHash", user.PasswordHash);
        command.Parameters.AddWithValue("@passwordSalt", user.PasswordSalt);

        await _connection.OpenAsync();

        user.Id = (int)await command.ExecuteScalarAsync();

        await _connection.CloseAsync();

        return user.Id > 0;
    }

    public async Task<bool> IsUserExistAsync<T>(T identifier)
    {
        var command = new SqlCommand() { Connection = _connection };

        if (identifier is int)
        {
            command.CommandText = "SELECT COUNT(*) FROM AppUsers WHERE Id = @identifier;";
        }
        else if (identifier is string)
        {
            command.CommandText = "SELECT COUNT(*) FROM AppUsers WHERE Email = @identifier;";
        }
        else
        {
            return false;
        }

        command.Parameters.AddWithValue("@identifier", identifier);

        await _connection.OpenAsync();

        bool isExist = (int)await command.ExecuteScalarAsync() > 0;

        await _connection.CloseAsync();

        return isExist;
    }

    public async Task<AppUser> GetUserAsync<T>(T identifier)
    {
        var command = new SqlCommand() { Connection = _connection };

        if (identifier is int)
        {
            command.CommandText = "SELECT * FROM AppUsers WHERE Id = @identifier;";
        }
        else if (identifier is string)
        {
            command.CommandText = "SELECT * FROM AppUsers WHERE Email = @identifier;";
        }
        else
        {
            return null;
        }

        command.Parameters.AddWithValue("@identifier", identifier);

        await _connection.OpenAsync();

        using SqlDataReader reader = await command.ExecuteReaderAsync();

        if (!reader.HasRows || !await reader.ReadAsync())
        {
            return null;
        }

        var user = new AppUser()
        {
            Id = (int)reader["Id"],
            Email = (string)reader["Email"],
            FirstName = (string)reader["FirstName"],
            LastName = (string)reader["LastName"],
            RegistrationDate = Convert.ToDateTime(reader["RegistrationDate"]),
            Birthday = DateOnly.Parse(Convert.ToDateTime(reader["Birthday"]).ToString("yyyy-MM-dd")),
            PhoneNumber = (string)reader["PhoneNumber"],
            UserRole = (string)reader["UserRole"],
            PasswordHash = (byte[])reader["PasswordHash"],
            PasswordSalt = (byte[])reader["PasswordSalt"]
        };

        await _connection.CloseAsync();

        return user;
    }

    public async Task<bool> UpdateUserAsync(AppUser user)
    {
        var command = new SqlCommand() { Connection = _connection };

        command.CommandText =
            "UPDATE AppUsers SET Email = @email, FirstName = @firstName, LastName = @lastName, Birthday = @birthday, PhoneNumber = @phoneNumber " +
            "WHERE Id = @id;";

        command.Parameters.AddWithValue("@email", user.Email);
        command.Parameters.AddWithValue("@firstName", user.FirstName);
        command.Parameters.AddWithValue("@lastName", user.LastName);
        command.Parameters.AddWithValue("@birthday", user.Birthday.ToString("yyyy-MM-dd"));
        command.Parameters.AddWithValue("@phoneNumber", user.PhoneNumber);
        command.Parameters.AddWithValue("@id", user.Id);

        await _connection.OpenAsync();

        int updatedRows = await command.ExecuteNonQueryAsync();

        await _connection.CloseAsync();

        return updatedRows > 0;
    }
}
