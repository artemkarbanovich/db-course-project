package karbanovich.fit.bstu.foodie.network;
import karbanovich.fit.bstu.foodie.models.Account;
import karbanovich.fit.bstu.foodie.models.Login;
import karbanovich.fit.bstu.foodie.models.Register;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface FoodieAPI {
    @POST("account/register")
    Call<Account> register(@Body Register register);

    @POST("account/login")
    @Headers("user-agent: android")
    Call<Account> login(@Body Login login);
}
