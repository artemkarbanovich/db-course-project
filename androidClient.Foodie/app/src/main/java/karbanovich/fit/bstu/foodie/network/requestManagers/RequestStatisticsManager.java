package karbanovich.fit.bstu.foodie.network.requestManagers;
import android.content.Context;
import karbanovich.fit.bstu.foodie.AccountSingleton;
import karbanovich.fit.bstu.foodie.constants.ApiConstants;
import karbanovich.fit.bstu.foodie.helpers.HttpHelper;
import karbanovich.fit.bstu.foodie.helpers.SharedPreferencesHelper;
import karbanovich.fit.bstu.foodie.models.Account;
import karbanovich.fit.bstu.foodie.models.Statistics;
import karbanovich.fit.bstu.foodie.network.FoodieAPI;
import karbanovich.fit.bstu.foodie.network.OnFetchDataListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestStatisticsManager {

    private final Retrofit retrofit = new Retrofit.Builder()
            .client(HttpHelper.getUnsafeOkHttpClient())
            .baseUrl(ApiConstants.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private final FoodieAPI foodieAPI = retrofit.create(FoodieAPI.class);
    private final Context context;

    public RequestStatisticsManager(Context context) { this.context = context; }

    public void getStatistic(OnFetchDataListener<Statistics> listener) {
        Call<Statistics> call;
        Account accSharedPreferences = SharedPreferencesHelper.getAccount(context);
        Account accSingleton = AccountSingleton.getAccount();

        if(accSharedPreferences != null) {
            call = foodieAPI.getStatistics("Bearer " + accSharedPreferences.getToken());
        } else if(accSingleton != null) {
            call = foodieAPI.getStatistics("Bearer " + accSingleton.getToken());
        } else { return; }

        call.enqueue(new Callback<Statistics>() {
            @Override public void onResponse(Call<Statistics> call, Response<Statistics> response) {
                listener.onFetchData(response);
            }
            @Override public void onFailure(Call<Statistics> call, Throwable t) {
                listener.onFetchError(t);
            }
        });
    }
}
