package karbanovich.fit.bstu.foodie.views;
import android.app.DatePickerDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import karbanovich.fit.bstu.foodie.R;
import karbanovich.fit.bstu.foodie.adapters.orders.OrdersAdapter;
import karbanovich.fit.bstu.foodie.database.DatabaseBuilder;
import karbanovich.fit.bstu.foodie.database.repositories.OrdersRepository;
import karbanovich.fit.bstu.foodie.databinding.ActivityOrdersBinding;
import karbanovich.fit.bstu.foodie.helpers.AccountHelper;
import karbanovich.fit.bstu.foodie.helpers.DateTimeHelper;
import karbanovich.fit.bstu.foodie.helpers.SystemHelper;
import karbanovich.fit.bstu.foodie.models.Order;
import karbanovich.fit.bstu.foodie.network.OnFetchDataListener;
import karbanovich.fit.bstu.foodie.network.requestManagers.RequestOrdersManager;
import karbanovich.fit.bstu.foodie.queryParams.OrdersQueryParams;
import retrofit2.Response;

public class OrdersActivity extends DrawerBaseActivity {

    private TextView noData;
    private ProgressBar progressBar;
    private EditText orderDateFrom;
    private EditText orderDateTo;
    private RecyclerView ordersRecycler;
    private Calendar date1;
    private Calendar date2;

    private final Context context = this;
    private SQLiteDatabase db;
    private ArrayList<Order> currentOrders;

    private final OnFetchDataListener<ArrayList<Order>> getOrdersListener = new OnFetchDataListener<ArrayList<Order>>() {
        @Override
        public void onFetchData(Response<ArrayList<Order>> response) {
            if(response.isSuccessful()) {
                currentOrders = response.body();
                displayOrders(currentOrders);

                OrdersRepository.deleteAllOrders(db, AccountHelper.getUserId(context));
                OrdersRepository.addOrders(db, currentOrders, AccountHelper.getUserId(context));
            }
            else {
                Toast.makeText(context, "Error by getting orders", Toast.LENGTH_LONG).show();
            }
            progressBar.setVisibility(View.GONE);
        }
        @Override
        public void onFetchError(Throwable error) {
            Toast.makeText(context, "Error by getting orders", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
        }
    };

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityOrdersBinding activityOrdersBinding = ActivityOrdersBinding.inflate(getLayoutInflater());
        setContentView(activityOrdersBinding.getRoot());
        allocateActivityTitle("Orders");

        bindingView();
        setDateListeners();
        getData();
    }

    private void getData() {
        noData.setVisibility(View.GONE);

        OrdersQueryParams params = new OrdersQueryParams();

        if(SystemHelper.isNetworkAvailable(context)) {
            progressBar.setVisibility(View.VISIBLE);

            if(orderDateFrom.getText().toString().length() != 0) {
                params.setOrderDateFrom(orderDateFrom.getText().toString().replace("-", ""));
            }
            if(orderDateTo.getText().toString().length() != 0) {
                params.setOrderDateTo(orderDateTo.getText().toString().replace("-", ""));
            }

            RequestOrdersManager requestManager = new RequestOrdersManager(context);
            requestManager.getOrders(getOrdersListener, params);
        }
        else if(!SystemHelper.isNetworkAvailable(context) &&
                OrdersRepository.isOrdersExist(db, AccountHelper.getUserId(context))) {
            progressBar.setVisibility(View.VISIBLE);

            if(orderDateFrom.getText().toString().length() != 0) {
                params.setOrderDateFrom(orderDateFrom.getText().toString());
            }
            if(orderDateTo.getText().toString().length() != 0) {
                params.setOrderDateTo(orderDateTo.getText().toString());
            }

            currentOrders = OrdersRepository.getOrders(db, AccountHelper.getUserId(context), params);
            displayOrders(currentOrders);

            progressBar.setVisibility(View.GONE);
        }
        else if(!SystemHelper.isNetworkAvailable(context) &&
                !OrdersRepository.isOrdersExist(db, AccountHelper.getUserId(context))) {
            noData.setVisibility(View.VISIBLE);
        }
    }

    private void displayOrders(ArrayList<Order> orders) {
        ordersRecycler.setHasFixedSize(true);
        ordersRecycler.setLayoutManager(new GridLayoutManager(context, 1));
        OrdersAdapter ordersAdapter = new OrdersAdapter(context, orders);
        ordersRecycler.setAdapter(ordersAdapter);
    }

    private void bindingView() {
        noData = findViewById(R.id.orders__no_data);
        progressBar = findViewById(R.id.orders__progress_bar);
        orderDateFrom = findViewById(R.id.orders__order_date_from);
        orderDateTo = findViewById(R.id.orders__order_date_to);
        ordersRecycler = findViewById(R.id.orders__orders_list_recycler);
        findViewById(R.id.orders_search_btn).setOnClickListener(view -> getData());
        findViewById(R.id.orders__clear_btn).setOnClickListener(view -> {
            orderDateFrom.setText("");
            orderDateTo.setText("");
        });
        date1 = Calendar.getInstance();
        date2 = Calendar.getInstance();

        db = new DatabaseBuilder(context).getWritableDatabase();
        currentOrders = new ArrayList<>();
    }

    private void setDateListeners() {
        orderDateFrom.setInputType(InputType.TYPE_NULL);
        orderDateTo.setInputType(InputType.TYPE_NULL);

        DatePickerDialog.OnDateSetListener d1 = (view, year, monthOfYear, dayOfMonth) -> {
            orderDateFrom.setText(DateTimeHelper.getGeneralDateFormat(year, monthOfYear + 1, dayOfMonth));
            if(orderDateFrom.getText().toString().length() != 0) {
                orderDateFrom.setError(null);
            }
        };
        DatePickerDialog.OnDateSetListener d2 = (view, year, monthOfYear, dayOfMonth) -> {
            orderDateTo.setText(DateTimeHelper.getGeneralDateFormat(year, monthOfYear + 1, dayOfMonth));
            if(orderDateTo.getText().toString().length() != 0) {
                orderDateTo.setError(null);
            }
        };

        DatePickerDialog orderDateFromDialog = new DatePickerDialog(this, d1,
                date1.get(Calendar.YEAR),
                date1.get(Calendar.MONTH),
                date1.get(Calendar.DAY_OF_MONTH));
        DatePickerDialog orderDateToDialog = new DatePickerDialog(this, d2,
                date2.get(Calendar.YEAR),
                date2.get(Calendar.MONTH),
                date2.get(Calendar.DAY_OF_MONTH));

        orderDateFromDialog.getDatePicker().setMaxDate(new Date().getTime());
        orderDateToDialog.getDatePicker().setMaxDate(new Date().getTime());

        orderDateFrom.setOnClickListener(view -> {
            SystemHelper.hideKeyboard(this);
            orderDateFromDialog.show();
        });
        orderDateTo.setOnClickListener(view -> {
            SystemHelper.hideKeyboard(this);
            orderDateToDialog.show();
        });

        orderDateFrom.setOnFocusChangeListener((view, hasFocus) -> {
            if(hasFocus) {
                SystemHelper.hideKeyboard(this);
                orderDateFromDialog.show();
            }
        });
        orderDateTo.setOnFocusChangeListener((view, hasFocus) -> {
            if(hasFocus) {
                SystemHelper.hideKeyboard(this);
                orderDateToDialog.show();
            }
        });
    }

    @Override public void onBackPressed() {
        this.moveTaskToBack(true);
    }
}
