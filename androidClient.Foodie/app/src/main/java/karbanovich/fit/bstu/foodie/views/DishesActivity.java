package karbanovich.fit.bstu.foodie.views;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import karbanovich.fit.bstu.foodie.R;
import karbanovich.fit.bstu.foodie.adapters.dishes.DishesAdapter;
import karbanovich.fit.bstu.foodie.adapters.dishes.DishesRecyclerScroll;
import karbanovich.fit.bstu.foodie.database.DatabaseBuilder;
import karbanovich.fit.bstu.foodie.database.repositories.DishesRepository;
import karbanovich.fit.bstu.foodie.databinding.ActivityDishesBinding;
import karbanovich.fit.bstu.foodie.helpers.AccountHelper;
import karbanovich.fit.bstu.foodie.helpers.SystemHelper;
import karbanovich.fit.bstu.foodie.models.Dish;
import karbanovich.fit.bstu.foodie.network.OnFetchDataListener;
import karbanovich.fit.bstu.foodie.network.requestManagers.RequestDishesManager;
import karbanovich.fit.bstu.foodie.queryParams.DishesQueryParams;
import retrofit2.Response;

public class DishesActivity extends DrawerBaseActivity {

    private TextView noData;
    private ProgressBar progressBar;
    private ConstraintLayout dishesSearchParamsContainer;
    private EditText nameSearchStr;
    private Spinner orderBy;
    private Spinner orderByType;
    private RecyclerView dishesRecycler;

    private final Context context = this;
    private SQLiteDatabase db;
    private ArrayList<Dish> currentDishes;

    private final OnFetchDataListener<ArrayList<Dish>> getDishesListener = new OnFetchDataListener<ArrayList<Dish>>() {
        @Override
        public void onFetchData(Response<ArrayList<Dish>> response) {
            if(response.isSuccessful()) {
                currentDishes = response.body();
                displayDishes(currentDishes);

                DishesRepository.deleteAllDishes(db, AccountHelper.getUserId(context));
                DishesRepository.addDishes(db, currentDishes, AccountHelper.getUserId(context));
            }
            else {
                Toast.makeText(context, "Error by getting dishes", Toast.LENGTH_LONG).show();
            }
            progressBar.setVisibility(View.GONE);
        }
        @Override
        public void onFetchError(Throwable error) {
            Toast.makeText(context, "Error by getting dishes", Toast.LENGTH_LONG).show();
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
        noData.setVisibility(View.GONE);

        DishesQueryParams params = new DishesQueryParams(
                nameSearchStr.getText().toString(),
                orderBy.getSelectedItem().toString().equals("Default") ? "NOT_MATTER" : orderBy.getSelectedItem().toString(),
                orderByType.getSelectedItem().toString().equals("Ascending") ? "ASC" : "DESC");

        if(SystemHelper.isNetworkAvailable(context)) {
            progressBar.setVisibility(View.VISIBLE);
            RequestDishesManager requestManager = new RequestDishesManager(context);
            requestManager.getDishes(getDishesListener, params);
        }
        else if(!SystemHelper.isNetworkAvailable(context) &&
                DishesRepository.isDishesExist(db, AccountHelper.getUserId(context))) {
            currentDishes = DishesRepository.getDishes(db, AccountHelper.getUserId(context), params);
            displayDishes(currentDishes);
        }
        else if(!SystemHelper.isNetworkAvailable(context) &&
                !DishesRepository.isDishesExist(db, AccountHelper.getUserId(context))) {
            noData.setVisibility(View.VISIBLE);
        }
    }

    private void displayDishes(ArrayList<Dish> dishes) {
        dishesRecycler.setHasFixedSize(true);
        dishesRecycler.setLayoutManager(new GridLayoutManager(context, 1));
        DishesAdapter dishesAdapter = new DishesAdapter(context, dishes);
        dishesRecycler.setAdapter(dishesAdapter);
    }

    private void bindingView() {
        noData = findViewById(R.id.dishes__no_data);
        progressBar = findViewById(R.id.dishes__progress_bar);
        dishesSearchParamsContainer = findViewById(R.id.dishes__search_params_container);
        nameSearchStr = findViewById(R.id.dishes__name_search_str);
        orderBy = findViewById(R.id.dishes_order_by_spinner);
        orderByType = findViewById(R.id.dishes__order_by_type_spinner);
        findViewById(R.id.dishes_search_btn).setOnClickListener(view -> getData());

        dishesRecycler = findViewById(R.id.dishes__dishes_list_recycler);
        dishesRecycler.setOnScrollListener(new DishesRecyclerScroll() {
            @Override public void showSearchParams() {
                dishesSearchParamsContainer.animate()
                        .translationY(Integer.parseInt("0"))
                        .alpha(1.0f)
                        .setDuration(700)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                dishesSearchParamsContainer.setVisibility(View.VISIBLE);
                            }
                        });
            }
            @Override public void hideSearchParams() {
                dishesSearchParamsContainer.animate()
                        .translationY(Integer.parseInt("-" + dishesSearchParamsContainer.getHeight()))
                        .alpha(0.0f)
                        .setDuration(700)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                dishesSearchParamsContainer.setVisibility(View.GONE);
                            }
                        });
            }
        });

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
