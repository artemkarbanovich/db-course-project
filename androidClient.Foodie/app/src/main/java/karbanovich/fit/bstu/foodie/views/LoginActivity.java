package karbanovich.fit.bstu.foodie.views;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;
import karbanovich.fit.bstu.foodie.AccountSingleton;
import karbanovich.fit.bstu.foodie.R;
import karbanovich.fit.bstu.foodie.helpers.SharedPreferencesHelper;
import karbanovich.fit.bstu.foodie.helpers.SystemHelper;
import karbanovich.fit.bstu.foodie.helpers.ValidationHelper;
import karbanovich.fit.bstu.foodie.models.Account;
import karbanovich.fit.bstu.foodie.models.Login;
import karbanovich.fit.bstu.foodie.network.OnFetchDataListener;
import karbanovich.fit.bstu.foodie.network.requestManagers.RequestAccountManager;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private EditText email;
    private EditText password;
    private CheckBox rememberMe;
    private boolean isValid = false;

    private final Context context = this;
    private final OnFetchDataListener<Account> responseListener = new OnFetchDataListener<Account>() {
        @Override public void onFetchData(Response<Account> response) {
            if(response.isSuccessful()) {
                if(rememberMe.isChecked())
                    SharedPreferencesHelper.addAccount(context, response.body());
                else
                    AccountSingleton.setAccount(response.body().getEmail(), response.body().getToken());

                startActivity(new Intent(context, ProfileActivity.class));
            } else if(response.code() == 404) {
                email.setError("Account with this email not exist");
                isValid = false;
            } else if(response.code() == 401) {
                password.setError("Invalid password");
                isValid = false;
            }
            progressBar.setVisibility(View.GONE);
        }
        @Override public void onFetchError(Throwable error) {
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
        }
    };

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bindingView();
        addValidation();
    }

    private void login() {
        isValid = true;
        callInputs();

        if(!isValid) {
            Toast.makeText(this, "Verify entered data and try again", Toast.LENGTH_LONG).show();
            return;
        } else if(!SystemHelper.isNetworkAvailable(this)) {
            Toast.makeText(this, "Check internet connection and try again", Toast.LENGTH_LONG).show();
            return;
        }

        Login login = new Login(email.getText().toString(), password.getText().toString());

        progressBar.setVisibility(View.VISIBLE);
        RequestAccountManager requestManager = new RequestAccountManager();

        // TODO: Remove Timer when deployed
        new Timer().schedule(new TimerTask() {
            @Override public void run() {
                requestManager.login(responseListener, login);
            }
        }, 1500L);
    }

    private void bindingView() {
        progressBar = findViewById(R.id.login__progress_bar);
        email = findViewById(R.id.login__email_text);
        password = findViewById(R.id.login__password_text);
        rememberMe = findViewById(R.id.login__remember_me_check);
        findViewById(R.id.login__login_btn).setOnClickListener(view -> login());
    }

    private void addValidation() {
        Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

        email.addTextChangedListener(new ValidationHelper(email) {
            @Override public void validate(TextView textView, String text) {
                if(!VALID_EMAIL_ADDRESS_REGEX.matcher(text).find()) {
                    textView.setError("Invalid format");
                    isValid = false;
                }
                if(text.length() == 0) {
                    textView.setError("Cannot be empty");
                    isValid = false;
                }
            }
        });
        password.addTextChangedListener(new ValidationHelper(password) {
            @Override public void validate(TextView textView, String text) {
                if(text.length() < 6 || text.length() > 32) {
                    textView.setError("Length must be from 6 to 32");
                    isValid = false;
                }
            }
        });
    }

    private void callInputs() {
        email.setText(email.getText().toString());
        password.setText(password.getText().toString());
    }

    @Override public void onBackPressed() {
        startActivity(new Intent(this, PreviewActivity.class));
    }
}
