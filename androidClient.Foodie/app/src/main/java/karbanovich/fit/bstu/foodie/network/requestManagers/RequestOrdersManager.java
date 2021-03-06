package karbanovich.fit.bstu.foodie.network.requestManagers;
import android.content.Context;
import java.util.ArrayList;
import karbanovich.fit.bstu.foodie.constants.ApiConstants;
import karbanovich.fit.bstu.foodie.helpers.AccountHelper;
import karbanovich.fit.bstu.foodie.helpers.HttpHelper;
import karbanovich.fit.bstu.foodie.models.Order;
import karbanovich.fit.bstu.foodie.models.OrderAdd;
import karbanovich.fit.bstu.foodie.network.FoodieAPI;
import karbanovich.fit.bstu.foodie.network.OnFetchDataListener;
import karbanovich.fit.bstu.foodie.queryParams.OrdersQueryParams;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestOrdersManager {

    private final Retrofit retrofit = new Retrofit.Builder()
            .client(HttpHelper.getUnsafeOkHttpClient())
            .baseUrl(ApiConstants.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private final FoodieAPI foodieAPI = retrofit.create(FoodieAPI.class);
    private final Context context;

    public RequestOrdersManager(Context context) { this.context = context; }

    public void makeOrder(OnFetchDataListener<Void> listener, OrderAdd orderAdd) {
        Call<Void> call = foodieAPI.makeOrder("Bearer " + AccountHelper.getBearerToken(context), orderAdd);

        call.enqueue(new Callback<Void>() {
            @Override public void onResponse(Call<Void> call, Response<Void> response) {
                listener.onFetchData(response);
            }
            @Override public void onFailure(Call<Void> call, Throwable t) {
                listener.onFetchError(t);
            }
        });
    }

    public void getOrders(OnFetchDataListener<ArrayList<Order>> listener, OrdersQueryParams params) {
        Call<ArrayList<Order>> call = foodieAPI.getOrders(
                "Bearer " + AccountHelper.getBearerToken(context),
                params.getOrderDateFrom(),
                params.getOrderDateTo());

        call.enqueue(new Callback<ArrayList<Order>>() {
            @Override public void onResponse(Call<ArrayList<Order>> call, Response<ArrayList<Order>> response) {
                listener.onFetchData(response);
            }
            @Override public void onFailure(Call<ArrayList<Order>> call, Throwable t) {
                listener.onFetchError(t);
            }
        });
    }
}
