package karbanovich.fit.bstu.foodie.network.requestManagers;
import karbanovich.fit.bstu.foodie.constants.ApiConstants;
import karbanovich.fit.bstu.foodie.helpers.HttpHelper;
import karbanovich.fit.bstu.foodie.models.Account;
import karbanovich.fit.bstu.foodie.models.Login;
import karbanovich.fit.bstu.foodie.models.Register;
import karbanovich.fit.bstu.foodie.network.FoodieAPI;
import karbanovich.fit.bstu.foodie.network.OnFetchDataListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestAccountManager {

    private final Retrofit retrofit = new Retrofit.Builder()
            .client(HttpHelper.getUnsafeOkHttpClient())
            .baseUrl(ApiConstants.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private final FoodieAPI foodieAPI = retrofit.create(FoodieAPI.class);

    public void register(OnFetchDataListener<Account> listener, Register register) {
        Call<Account> call = foodieAPI.register(register);

        call.enqueue(new Callback<Account>() {
            @Override public void onResponse(Call<Account> call, Response<Account> response) {
                listener.onFetchData(response);
            }
            @Override public void onFailure(Call<Account> call, Throwable t) {
                listener.onFetchError(t);
            }
        });
    }

    public void login(OnFetchDataListener<Account> listener, Login login) {
        Call<Account> call = foodieAPI.login(login);

        call.enqueue(new Callback<Account>() {
            @Override public void onResponse(Call<Account> call, Response<Account> response) {
                listener.onFetchData(response);
            }
            @Override public void onFailure(Call<Account> call, Throwable t) {
                listener.onFetchError(t);
            }
        });
    }
}
