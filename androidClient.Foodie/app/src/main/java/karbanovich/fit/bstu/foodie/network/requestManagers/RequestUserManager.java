package karbanovich.fit.bstu.foodie.network.requestManagers;
import android.content.Context;
import karbanovich.fit.bstu.foodie.AccountSingleton;
import karbanovich.fit.bstu.foodie.constants.ApiConstants;
import karbanovich.fit.bstu.foodie.helpers.HttpHelper;
import karbanovich.fit.bstu.foodie.helpers.SharedPreferencesHelper;
import karbanovich.fit.bstu.foodie.models.Account;
import karbanovich.fit.bstu.foodie.models.User;
import karbanovich.fit.bstu.foodie.network.FoodieAPI;
import karbanovich.fit.bstu.foodie.network.OnFetchDataListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestUserManager {

    private final Retrofit retrofit = new Retrofit.Builder()
            .client(HttpHelper.getUnsafeOkHttpClient())
            .baseUrl(ApiConstants.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private final FoodieAPI foodieAPI = retrofit.create(FoodieAPI.class);
    private final Context context;

    public RequestUserManager(Context context) { this.context = context; }

    public void getUser(OnFetchDataListener<User> listener) {
        Call<User> call;
        Account accSharedPreferences = SharedPreferencesHelper.getAccount(context);
        Account accSingleton = AccountSingleton.getAccount();

        if(accSharedPreferences != null) {
            call = foodieAPI.getUser(
                    "Bearer " + accSharedPreferences.getToken(),
                    accSharedPreferences.getEmail());
        } else if(accSingleton != null) {
            call = foodieAPI.getUser(
                    "Bearer " + accSingleton.getToken(),
                    accSingleton.getEmail());
        } else { return; }

        call.enqueue(new Callback<User>() {
            @Override public void onResponse(Call<User> call, Response<User> response) {
                listener.onFetchData(response);
            }
            @Override public void onFailure(Call<User> call, Throwable t) {
                listener.onFetchError(t);
            }
        });
    }

    public void updateUser(OnFetchDataListener<Void> listener, User user) {
        Call<Void> call;
        Account accSharedPreferences = SharedPreferencesHelper.getAccount(context);
        Account accSingleton = AccountSingleton.getAccount();

        if(accSharedPreferences != null) {
            call = foodieAPI.updateUser("Bearer " + accSharedPreferences.getToken(), user);
        } else if(accSingleton != null) {
            call = foodieAPI.updateUser("Bearer " + accSingleton.getToken(), user);
        } else { return; }

        call.enqueue(new Callback<Void>() {
            @Override public void onResponse(Call<Void> call, Response<Void> response) {
                listener.onFetchData(response);
            }
            @Override public void onFailure(Call<Void> call, Throwable t) {
                listener.onFetchError(t);
            }
        });
    }
}
