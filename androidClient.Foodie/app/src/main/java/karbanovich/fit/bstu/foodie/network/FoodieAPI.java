package karbanovich.fit.bstu.foodie.network;
import java.util.ArrayList;
import java.util.List;
import karbanovich.fit.bstu.foodie.models.Account;
import karbanovich.fit.bstu.foodie.models.Dish;
import karbanovich.fit.bstu.foodie.models.Login;
import karbanovich.fit.bstu.foodie.models.Register;
import karbanovich.fit.bstu.foodie.models.Statistics;
import karbanovich.fit.bstu.foodie.models.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FoodieAPI {
    @POST("account/register")
    Call<Account> register(@Body Register register);

    @POST("account/login")
    @Headers("user-agent: android")
    Call<Account> login(@Body Login login);

    @GET("users/{email}/")
    Call<User> getUser(@Header("authorization") String token, @Path("email") String email);

    @PUT("users")
    Call<Void> updateUser(@Header("authorization") String token, @Body User user);

    @GET("statistics/user")
    Call<Statistics> getStatistics(@Header("authorization") String token);

    @GET("dishes/user-list")
    Call<ArrayList<Dish>> getDishes(@Header("authorization") String token,
                                    @Query("nameSearchStr") String nameSearchStr,
                                    @Query("orderBy") String orderBy,
                                    @Query("orderByType") String orderByType);
}
