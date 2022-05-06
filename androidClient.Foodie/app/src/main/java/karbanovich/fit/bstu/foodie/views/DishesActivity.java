package karbanovich.fit.bstu.foodie.views;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import karbanovich.fit.bstu.foodie.R;
import karbanovich.fit.bstu.foodie.database.DatabaseBuilder;
import karbanovich.fit.bstu.foodie.databinding.ActivityDishesBinding;
import karbanovich.fit.bstu.foodie.helpers.SystemHelper;
import karbanovich.fit.bstu.foodie.models.Dish;
import karbanovich.fit.bstu.foodie.network.OnFetchDataListener;
import karbanovich.fit.bstu.foodie.network.requestManagers.RequestDishesManager;
import karbanovich.fit.bstu.foodie.queryParams.DishesQueryParams;
import retrofit2.Response;

public class DishesActivity extends DrawerBaseActivity {

    private ProgressBar progressBar;
    private EditText nameSearchStr;
    private Spinner orderBy;
    private Spinner orderByType;

    private final Context context = this;
    private SQLiteDatabase db;
    private ArrayList<Dish> currentDishes;

    private final OnFetchDataListener<ArrayList<Dish>> getDishesListener = new OnFetchDataListener<ArrayList<Dish>>() {
        @Override
        public void onFetchData(Response<ArrayList<Dish>> response) {
            if(response.isSuccessful()) {
                currentDishes = response.body();
            }
            else {
                Toast.makeText(context, "Error by getting dishes 1", Toast.LENGTH_LONG).show();
            }
            progressBar.setVisibility(View.GONE);
        }
        @Override
        public void onFetchError(Throwable error) {
            Toast.makeText(context, "Error by getting dishes 2", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
        }
    };

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDishesBinding activityDishesBinding = ActivityDishesBinding.inflate(getLayoutInflater());
        setContentView(activityDishesBinding.getRoot());
        allocateActivityTitle("Dishes");

        bindingView();
        setSpinners();
        getData();
    }

    private void getData() {
        DishesQueryParams params = new DishesQueryParams(
                nameSearchStr.getText().toString().equals("Default") ? "NOT_MATTER" : nameSearchStr.getText().toString(),
                orderBy.getSelectedItem().toString(),
                orderByType.getSelectedItem().toString());

        if(SystemHelper.isNetworkAvailable(context)) {
            progressBar.setVisibility(View.VISIBLE);
            RequestDishesManager requestManager = new RequestDishesManager(context);
            requestManager.getDishes(getDishesListener, params);
        }
        else if(!SystemHelper.isNetworkAvailable(context)) {

        }
    }

    private void bindingView() {
        progressBar = findViewById(R.id.dishes__progress_bar);
        nameSearchStr = findViewById(R.id.dishes__name_search_str);
        orderBy = findViewById(R.id.dishes_order_by_spinner);
        orderByType = findViewById(R.id.dishes__order_by_type_spinner);
        findViewById(R.id.dishes_search_btn).setOnClickListener(view -> getData());

        db = new DatabaseBuilder(context).getWritableDatabase();
        currentDishes = new ArrayList<>();
    }

    private void setSpinners() {
        String[] orderByValues = { "Default", "Name", "Price" };
        String[] orderByTypeValues = { "Ascending", "Descending" };

        ArrayAdapter<String> orderByAdapter =
                new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, orderByValues);
        ArrayAdapter<String> orderByTypeAdapter =
                new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, orderByTypeValues);

        orderBy.setAdapter(orderByAdapter);
        orderByType.setAdapter(orderByTypeAdapter);
    }

    @Override public void onBackPressed() {
        this.moveTaskToBack(true);
    }
}
