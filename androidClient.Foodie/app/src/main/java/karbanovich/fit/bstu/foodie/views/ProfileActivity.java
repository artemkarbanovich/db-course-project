package karbanovich.fit.bstu.foodie.views;
import android.app.DatePickerDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;
import karbanovich.fit.bstu.foodie.R;
import karbanovich.fit.bstu.foodie.database.DatabaseBuilder;
import karbanovich.fit.bstu.foodie.database.repositories.UserRepository;
import karbanovich.fit.bstu.foodie.databinding.ActivityProfileBinding;
import karbanovich.fit.bstu.foodie.helpers.AccountHelper;
import karbanovich.fit.bstu.foodie.helpers.DateTimeHelper;
import karbanovich.fit.bstu.foodie.helpers.SystemHelper;
import karbanovich.fit.bstu.foodie.helpers.ValidationHelper;
import karbanovich.fit.bstu.foodie.models.User;
import karbanovich.fit.bstu.foodie.network.OnFetchDataListener;
import karbanovich.fit.bstu.foodie.network.requestManagers.RequestUserManager;
import retrofit2.Response;

public class ProfileActivity extends DrawerBaseActivity {

    private TextView error_success_db_updating;
    private TextView noData;
    private ProgressBar progressBar;
    private EditText email;
    private EditText firstName;
    private EditText lastName;
    private EditText birthday;
    private Calendar date;
    private EditText phoneNumber;
    private Button update;
    private boolean isValid = false;
    private final Context context = this;
    private SQLiteDatabase db;
    private User currentUser;

    private final OnFetchDataListener<User> getUserListener = new OnFetchDataListener<User>() {
        @Override
        public void onFetchData(Response<User> response) {
            if(response.isSuccessful()) {
                currentUser = response.body();
                setUserData(currentUser);

                if(UserRepository.isUserExist(db, AccountHelper.getUserId(context)))
                    UserRepository.updateUser(db, currentUser);
                else
                    UserRepository.insertUser(db, currentUser);
            }
            else {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
            }
            progressBar.setVisibility(View.GONE);
        }
        @Override
        public void onFetchError(Throwable error) {
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
        }
    };

    private final OnFetchDataListener<Void> updateUserListener = new OnFetchDataListener<Void>() {
        @Override
        public void onFetchData(Response<Void> response) {
            if(response.isSuccessful()) {
                Toast.makeText(context, "User successfully updated", Toast.LENGTH_LONG).show();
                UserRepository.updateUser(db, currentUser);
                AccountHelper.updateEmail(context, currentUser.getEmail());
            }
            else if(response.code() == 400){
                email.setError("Email already taken");
                isValid = false;
            }
            else {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
            }
            progressBar.setVisibility(View.GONE);
            error_success_db_updating.setVisibility(View.GONE);
        }
        @Override
        public void onFetchError(Throwable error) {
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
            error_success_db_updating.setVisibility(View.GONE);
        }
    };

    private final OnFetchDataListener<Void> updateUserFromDb = new OnFetchDataListener<Void>() {
        @Override
        public void onFetchData(Response<Void> response) {
            if(response.isSuccessful()) {
                setErrorOrSuccessDbUpdating("green",
                        "Your profile successfully updated after connecting to the internet");
                setUserData(currentUser);

                currentUser.setUpdateStatus(null);
                UserRepository.updateUser(db, currentUser);
                AccountHelper.updateEmail(context, currentUser.getEmail());
            }
            else if(response.code() == 400){
                setErrorOrSuccessDbUpdating("red",
                        "Error by updating your profile after connecting to the internet due to email already taken");
                getData();
            }
            else {
                setErrorOrSuccessDbUpdating("red",
                        "Error by updating your profile after connecting to the internet");
                getData();
            }
            progressBar.setVisibility(View.GONE);
        }
        @Override
        public void onFetchError(Throwable error) {
            setErrorOrSuccessDbUpdating("red",
                    "Error by updating your profile after connecting to the internet");
            getData();
            progressBar.setVisibility(View.GONE);
        }
    };

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityProfileBinding activityProfileBinding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(activityProfileBinding.getRoot());
        allocateActivityTitle("Profile");
        
        bindingView();
        setDateListener();
        addValidation();
        checkForUpdating();
    }

    private void getData() {
        if(SystemHelper.isNetworkAvailable(context)) {
            progressBar.setVisibility(View.VISIBLE);
            RequestUserManager requestManager = new RequestUserManager(context);
            requestManager.getUser(getUserListener);
        }
        else if(!SystemHelper.isNetworkAvailable(context) &&
                UserRepository.isUserExist(db, AccountHelper.getUserId(context))) {
            currentUser = UserRepository.getUser(db, AccountHelper.getUserId(context));
            setUserData(currentUser);
        }
        else if(!SystemHelper.isNetworkAvailable(context) &&
                !UserRepository.isUserExist(db, AccountHelper.getUserId(context))) {
            setViewVisibility(View.GONE);
            noData.setVisibility(View.VISIBLE);
        }
    }

    private void updateProfile() {
        isValid = true;
        callInputs();

        if(!isValid) {
            Toast.makeText(this, "Verify entered data and try again", Toast.LENGTH_LONG).show();
            return;
        }

        currentUser.setEmail(email.getText().toString());
        currentUser.setFirstName(firstName.getText().toString());
        currentUser.setLastName(lastName.getText().toString());
        currentUser.setBirthday(birthday.getText().toString());
        currentUser.setPhoneNumber(phoneNumber.getText().toString());

        if(SystemHelper.isNetworkAvailable(context)) {
            progressBar.setVisibility(View.VISIBLE);
            RequestUserManager requestManager = new RequestUserManager(context);
            requestManager.updateUser(updateUserListener, currentUser);
        } else if (!SystemHelper.isNetworkAvailable(context)) {
            currentUser.setUpdateStatus("UPDATED_LOCALLY");
            UserRepository.updateUser(db, currentUser);
            error_success_db_updating.setVisibility(View.GONE);
            Toast.makeText(context, "User will be updated", Toast.LENGTH_LONG).show();
        }
    }

    private void checkForUpdating() {
        currentUser = UserRepository.getUser(db, AccountHelper.getUserId(context));

        if(currentUser != null &&
                currentUser.getUpdateStatus() != null &&
                SystemHelper.isNetworkAvailable(context) &&
                currentUser.getUpdateStatus().equals("UPDATED_LOCALLY"))
        {
                progressBar.setVisibility(View.VISIBLE);
                RequestUserManager requestManager = new RequestUserManager(context);
                requestManager.updateUser(updateUserFromDb, currentUser);
        }
        else { getData(); }
    }

    private void setUserData(User user) {
        email.setText(user.getEmail());
        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        birthday.setText(user.getBirthday());
        phoneNumber.setText(user.getPhoneNumber());
    }

    private void setViewVisibility(int visibility) {
        email.setVisibility(visibility);
        firstName.setVisibility(visibility);
        lastName.setVisibility(visibility);
        birthday.setVisibility(visibility);
        phoneNumber.setVisibility(visibility);
        update.setVisibility(visibility);
    }

    private void setErrorOrSuccessDbUpdating(String color, String text) {
        if(color.equals("red"))
            error_success_db_updating.setTextColor(Color.parseColor("#AF0909"));
        else if(color.equals("green"))
            error_success_db_updating.setTextColor(Color.parseColor("#03910A"));

        error_success_db_updating.setVisibility(View.VISIBLE);
        error_success_db_updating.setText(text);
    }

    private void bindingView() {
        error_success_db_updating = findViewById(R.id.profile__error_success_db_updating);
        noData = findViewById(R.id.profile__no_data);
        progressBar = findViewById(R.id.profile__progress_bar);
        email = findViewById(R.id.profile__email_text);
        firstName = findViewById(R.id.profile__first_name_text);
        lastName = findViewById(R.id.profile__last_name_text);
        birthday = findViewById(R.id.profile__birthday_text);
        date = Calendar.getInstance();
        phoneNumber = findViewById(R.id.profile__phone_number_text);
        update = findViewById(R.id.profile__update_btn);
        update.setOnClickListener(view -> updateProfile());

        db = new DatabaseBuilder(context).getWritableDatabase();
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
    }

    private void callInputs() {
        email.setText(email.getText().toString());
        firstName.setText(firstName.getText().toString());
        lastName.setText(lastName.getText().toString());
        birthday.setText(birthday.getText().toString());
        phoneNumber.setText(phoneNumber.getText().toString());
    }

    @Override public void onBackPressed() {
        this.moveTaskToBack(true);
    }
}
