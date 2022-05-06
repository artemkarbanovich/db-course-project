package karbanovich.fit.bstu.foodie.views;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;
import karbanovich.fit.bstu.foodie.AccountSingleton;
import karbanovich.fit.bstu.foodie.R;
import karbanovich.fit.bstu.foodie.helpers.DateTimeHelper;
import karbanovich.fit.bstu.foodie.helpers.SharedPreferencesHelper;
import karbanovich.fit.bstu.foodie.helpers.SystemHelper;
import karbanovich.fit.bstu.foodie.helpers.ValidationHelper;
import karbanovich.fit.bstu.foodie.models.Account;
import karbanovich.fit.bstu.foodie.models.Register;
import karbanovich.fit.bstu.foodie.network.OnFetchDataListener;
import karbanovich.fit.bstu.foodie.network.requestManagers.RequestAccountManager;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private EditText email;
    private EditText firstName;
    private EditText lastName;
    private EditText birthday;
    private Calendar date;
    private EditText phoneNumber;
    private EditText password;
    private EditText confirmPassword;
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

                startActivity(new Intent(context, DishesActivity.class));
            } else {
                email.setError("Email already taken");
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
        setContentView(R.layout.activity_register);

        bindingView();
        setDateListener();
        addValidation();
    }

    private void register() {
        isValid = true;
        callInputs();

        if(!isValid) {
            Toast.makeText(this, "Verify entered data and try again", Toast.LENGTH_LONG).show();
            return;
        } else if(!SystemHelper.isNetworkAvailable(this)) {
            Toast.makeText(this, "Check internet connection and try again", Toast.LENGTH_LONG).show();
            return;
        }

        Register register = new Register(
                email.getText().toString(),
                firstName.getText().toString(),
                lastName.getText().toString(),
                birthday.getText().toString(),
                phoneNumber.getText().toString(),
                password.getText().toString());

        progressBar.setVisibility(View.VISIBLE);
        RequestAccountManager requestManager = new RequestAccountManager();
        requestManager.register(responseListener, register);
    }

    private void bindingView() {
        progressBar = findViewById(R.id.register__progress_bar);
        email = findViewById(R.id.register__email_text);
        firstName = findViewById(R.id.register__first_name_text);
        lastName = findViewById(R.id.register__last_name_text);
        birthday = findViewById(R.id.register__birthday_text);
        date = Calendar.getInstance();
        phoneNumber = findViewById(R.id.register__phone_number_text);
        password = findViewById(R.id.register__password_text);
        confirmPassword = findViewById(R.id.register__confirm_password_text);
        rememberMe = findViewById(R.id.register__remember_me_check);
        findViewById(R.id.register__register_btn).setOnClickListener(view -> register());
    }

    private void setDateListener() {
        birthday.setInputType(InputType.TYPE_NULL);

        DatePickerDialog.OnDateSetListener d = (view, year, monthOfYear, dayOfMonth) -> {
            birthday.setText(DateTimeHelper.getGeneralDateFormat(year, monthOfYear + 1, dayOfMonth));
            if(birthday.getText().toString().length() != 0) {
                birthday.setError(null);
            }
        };

        DatePickerDialog birthdayDialog = new DatePickerDialog(this, d,
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH));

        birthdayDialog.getDatePicker().setMaxDate(new Date().getTime());

        birthday.setOnClickListener(view -> {
            SystemHelper.hideKeyboard(this);
            birthdayDialog.show();
        });
        birthday.setOnFocusChangeListener((view, hasFocus) -> {
            if(hasFocus) {
                SystemHelper.hideKeyboard(this);
                birthdayDialog.show();
            }
        });
    }

    private void addValidation() {
        Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Pattern VALID_PHONE_NUMBER_REGEX =
                Pattern.compile("^(\\+375)(29|25|44|33)(\\d{3})(\\d{2})(\\d{2})$", Pattern.CASE_INSENSITIVE);

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
        firstName.addTextChangedListener(new ValidationHelper(firstName) {
            @Override public void validate(TextView textView, String text) {
                if(text.length() < 2 || text.length() > 25) {
                    textView.setError("Length must be from 2 to 25");
                    isValid = false;
                }
            }
        });
        lastName.addTextChangedListener(new ValidationHelper(lastName) {
            @Override public void validate(TextView textView, String text) {
                if(text.length() < 2 || text.length() > 25) {
                    textView.setError("Length must be from 2 to 25");
                    isValid = false;
                }
            }
        });
        birthday.addTextChangedListener(new ValidationHelper(birthday) {
            @Override public void validate(TextView textView, String text) {
                if(text.length() == 0) {
                    textView.setError("Cannot be empty");
                    isValid = false;
                }
            }
        });
        phoneNumber.addTextChangedListener(new ValidationHelper(phoneNumber) {
            @Override public void validate(TextView textView, String text) {
                if(text.length() == 0) {
                    textView.setError("Cannot be empty");
                    isValid = false;
                }
                if(!VALID_PHONE_NUMBER_REGEX.matcher(text).find()) {
                    textView.setError("Invalid format");
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
        confirmPassword.addTextChangedListener(new ValidationHelper(confirmPassword) {
            @Override public void validate(TextView textView, String text) {
                if(!text.equals(password.getText().toString())) {
                    textView.setError("Passwords must match");
                    isValid = false;
                }
            }
        });
    }

    private void callInputs() {
        email.setText(email.getText().toString());
        firstName.setText(firstName.getText().toString());
        lastName.setText(lastName.getText().toString());
        birthday.setText(birthday.getText().toString());
        phoneNumber.setText(phoneNumber.getText().toString());
        password.setText(password.getText().toString());
        confirmPassword.setText(confirmPassword.getText().toString());
    }

    @Override public void onBackPressed() {
        startActivity(new Intent(this, PreviewActivity.class));
    }
}
