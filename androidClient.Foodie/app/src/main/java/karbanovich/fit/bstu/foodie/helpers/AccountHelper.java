package karbanovich.fit.bstu.foodie.helpers;
import android.content.Context;
import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;
import karbanovich.fit.bstu.foodie.AccountSingleton;
import karbanovich.fit.bstu.foodie.models.Account;

public class AccountHelper {

    public static int getUserId(Context context) {
        String token = "";
        if(SharedPreferencesHelper.isAccountExist(context))
            token = SharedPreferencesHelper.getAccount(context).getToken();
        else
            token = AccountSingleton.getAccount().getToken();

        JWT parsedJWT = new JWT(token);
        Claim subscriptionMetaData = parsedJWT.getClaim("nameid");
        String userId = subscriptionMetaData.asString();

        return Integer.parseInt(userId);
    }

    public static void updateEmail(Context context, String email) {
        if(SharedPreferencesHelper.isAccountExist(context)) {
            String token = SharedPreferencesHelper.getAccount(context).getToken();
            SharedPreferencesHelper.deleteAccount(context);
            SharedPreferencesHelper.addAccount(context, new Account(email, token));
        } else {
            String token = AccountSingleton.getAccount().getToken();
            AccountSingleton.destroyAccount();
            AccountSingleton.setAccount(email, token);
        }
    }

}
