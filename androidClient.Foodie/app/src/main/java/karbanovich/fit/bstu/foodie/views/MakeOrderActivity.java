package karbanovich.fit.bstu.foodie.views;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import karbanovich.fit.bstu.foodie.CartService;
import karbanovich.fit.bstu.foodie.R;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import karbanovich.fit.bstu.foodie.databinding.ActivityMakeOrderBinding;
import karbanovich.fit.bstu.foodie.helpers.DateTimeHelper;
import karbanovich.fit.bstu.foodie.helpers.SystemHelper;
import karbanovich.fit.bstu.foodie.helpers.ValidationHelper;
import karbanovich.fit.bstu.foodie.models.CartItem;
import karbanovich.fit.bstu.foodie.models.OrderAdd;
import karbanovich.fit.bstu.foodie.models.OrderAddDishes;
import karbanovich.fit.bstu.foodie.network.OnFetchDataListener;
import karbanovich.fit.bstu.foodie.network.requestManagers.RequestOrdersManager;
import retrofit2.Response;

public class MakeOrderActivity extends DrawerBaseActivity {

    private ProgressBar progressBar;
    private EditText address;
    private EditText deliveryDate;
    private EditText deliveryTime;
    private Button makeOrderBtn;
    private Calendar date;
    private Calendar time;
    private boolean isValid;
    private Context context = this;

    private final OnFetchDataListener<Void> makeOrderListener = new OnFetchDataListener<Void>() {
        @Override public void onFetchData(Response<Void> response) {
            if(response.isSuccessful()) {
                Toast.makeText(context, "Order is successfully make", Toast.LENGTH_LONG).show();
                CartService.removeAllItems();
                startActivity(new Intent(context, OrdersActivity.class));
            } else {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
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
        ActivityMakeOrderBinding activityMakeOrderBinding = ActivityMakeOrderBinding.inflate(getLayoutInflater());
        setContentView(activityMakeOrderBinding.getRoot());
        allocateActivityTitle("Make order");

        bindingView();
        setDateListener();
        setTimeListener();
        addValidation();
    }

    private void bindingView() {
        progressBar = findViewById(R.id.make_order__progress_bar);
        address = findViewById(R.id.make_order__address);
        deliveryDate = findViewById(R.id.make_order__delivery_date);
        deliveryTime = findViewById(R.id.make_order__delivery_time);
        makeOrderBtn = findViewById(R.id.make_order__make_order_btn);

        makeOrderBtn.setText("ORDER FOR " + String.format("%.2f", CartService.getTotalPrice()) + " BYN");
        makeOrderBtn.setOnClickListener(view -> makeOrder());

        date = Calendar.getInstance();
        time = Calendar.getInstance();
    }

    private void makeOrder() {
        isValid = true;
        callInputs();

        if(!isValid) {
            Toast.makeText(this, "Verify entered data and try again", Toast.LENGTH_LONG).show();
            return;
        } else if(!SystemHelper.isNetworkAvailable(this)) {
            Toast.makeText(this, "Check internet connection and try again", Toast.LENGTH_LONG).show();
            return;
        } else if(CartService.getCartItems().size() == 0) { return; }

        ArrayList<OrderAddDishes> dishes = new ArrayList<>();
        for (CartItem cartItem : CartService.getCartItems()) {
            dishes.add(new OrderAddDishes(cartItem.getDishId(), cartItem.getCount()));
        }

        OrderAdd orderAdd = new OrderAdd(
                deliveryDate.getText().toString() + "T" + deliveryTime.getText().toString() + ":00",
                address.getText().toString(),
                dishes
        );

        progressBar.setVisibility(View.VISIBLE);
        RequestOrdersManager requestManager = new RequestOrdersManager(context);
        requestManager.makeOrder(makeOrderListener, orderAdd);
    }

    private void setDateListener() {
        deliveryDate.setInputType(InputType.TYPE_NULL);

        DatePickerDialog.OnDateSetListener d = (view, year, monthOfYear, dayOfMonth) -> {
            deliveryDate.setText(DateTimeHelper.getGeneralDateFormat(year, monthOfYear + 1, dayOfMonth));
            if(deliveryDate.getText().toString().length() != 0) {
                deliveryDate.setError(null);
            }
        };

        DatePickerDialog deliveryDateDialog = new DatePickerDialog(this, d,
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH));

        deliveryDateDialog.getDatePicker().setMinDate(new Date().getTime());

        deliveryDate.setOnClickListener(view -> {
            SystemHelper.hideKeyboard(this);
            deliveryDateDialog.show();
        });
        deliveryDate.setOnFocusChangeListener((view, hasFocus) -> {
            if(hasFocus) {
                SystemHelper.hideKeyboard(this);
                deliveryDateDialog.show();
            }
        });
    }

    private void setTimeListener() {
        deliveryTime.setInputType(InputType.TYPE_NULL);

        TimePickerDialog.OnTimeSetListener t = (view, hourOfDay, minutes) -> {
            deliveryTime.setText(DateTimeHelper.getGeneralTimeFormat(hourOfDay, minutes));
            if(deliveryTime.getText().toString().length() != 0) {
                deliveryTime.setError(null);
            }
        };

        TimePickerDialog deliveryTimeDialog = new TimePickerDialog(this, t,
                time.get(Calendar.HOUR_OF_DAY),
                time.get(Calendar.MINUTE),
                true);

        deliveryTime.setOnClickListener(view -> {
            SystemHelper.hideKeyboard(this);
            deliveryTimeDialog.show();
        });
        deliveryTime.setOnFocusChangeListener((view, hasFocus) -> {
            if(hasFocus) {
                SystemHelper.hideKeyboard(this);
                deliveryTimeDialog.show();
            }
        });
    }

    private void addValidation() {
        address.addTextChangedListener(new ValidationHelper(address) {
            @Override public void validate(TextView textView, String text) {
                if(text.length() == 0) {
                    textView.setError("Cannot be empty");
                    isValid = false;
                } else if(text.length() < 5 || text.length() > 120) {
                    textView.setError("Length must be from 5 to 120");
                    isValid = false;
                }
            }
        });
        deliveryDate.addTextChangedListener(new ValidationHelper(deliveryDate) {
            @Override public void validate(TextView textView, String text) {
                if(text.length() == 0) {
                    textView.setError("Cannot be empty");
                    isValid = false;
                }
            }
        });
        deliveryTime.addTextChangedListener(new ValidationHelper(deliveryTime) {
            @Override public void validate(TextView textView, String text) {
                if(text.length() == 0) {
                    textView.setError("Cannot be empty");
                    isValid = false;
                }
            }
        });
    }

    private void callInputs() {
        address.setText(address.getText().toString());
        deliveryDate.setText(deliveryDate.getText().toString());
        deliveryTime.setText(deliveryTime.getText().toString());
    }

    @Override public void onBackPressed() {
        startActivity(new Intent(this, CartActivity.class));
    }
}
