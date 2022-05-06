package karbanovich.fit.bstu.foodie.network.requestManagers;
import android.content.Context;
import java.util.ArrayList;
import karbanovich.fit.bstu.foodie.constants.ApiConstants;
import karbanovich.fit.bstu.foodie.helpers.AccountHelper;
import karbanovich.fit.bstu.foodie.helpers.HttpHelper;
import karbanovich.fit.bstu.foodie.models.Dish;
import karbanovich.fit.bstu.foodie.network.FoodieAPI;
import karbanovich.fit.bstu.foodie.network.OnFetchDataListener;
import karbanovich.fit.bstu.foodie.queryParams.DishesQueryParams;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestDishesManager {

    private final Retrofit retrofit = new Retrofit.Builder()
            .client(HttpHelper.getUnsafeOkHttpClient())
            .baseUrl(ApiConstants.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private final FoodieAPI foodieAPI = retrofit.create(FoodieAPI.class);
    private final Context context;

    public RequestDishesManager(Context context) { this.context = context; }

    public void getDishes(OnFetchDataListener<ArrayList<Dish>> listener, DishesQueryParams params) {
        Call<ArrayList<Dish>> call = foodieAPI.getDishes(
                "Bearer " + AccountHelper.getBearerToken(context),
                params.getNameSearchStr(),
                params.getOrderBy(),
                params.getOrderByType());

        call.enqueue(new Callback<ArrayList<Dish>>() {
            @Override public void onResponse(Call<ArrayList<Dish>> call, Response<ArrayList<Dish>> response) {
                listener.onFetchData(response);
            }
            @Override public void onFailure(Call<ArrayList<Dish>> call, Throwable t) {
                listener.onFetchError(t);
            }
        });
    }
}
