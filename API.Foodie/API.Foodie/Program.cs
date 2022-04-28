using API.Foodie.Extensions;
using API.Foodie.Helpers.Json;


CultureInfo.DefaultThreadCurrentCulture = new CultureInfo("en-US");

var builder = WebApplication.CreateBuilder(args);
var services = builder.Services;
var config = builder.Configuration;

services.AddAppServices(config);
services.AddIdentityServices(config);
services.AddCors();
services.AddControllers()
    .AddJsonOptions(opt =>
    {
        opt.JsonSerializerOptions.Converters.Add(new DateOnlyConverter());
        opt.JsonSerializerOptions.Converters.Add(new TimeOnlyConverter());
    });


var app = builder.Build();

app.UseHttpsRedirection();
app.UseRouting();
app.UseAuthentication();
app.UseAuthorization();
app.UseCors(opt => opt
    .AllowAnyHeader()
    .AllowAnyMethod()
    .AllowCredentials()
    .AllowAnyOrigin());
app.MapControllers();
app.Run();
