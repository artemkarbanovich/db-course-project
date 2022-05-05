package karbanovich.fit.bstu.foodie.views;
import android.os.Bundle;
import karbanovich.fit.bstu.foodie.databinding.ActivityDishesBinding;

public class DishesActivity extends DrawerBaseActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDishesBinding activityDishesBinding = ActivityDishesBinding.inflate(getLayoutInflater());
        setContentView(activityDishesBinding.getRoot());
        allocateActivityTitle("Dishes");
    }

    @Override public void onBackPressed() {
        this.moveTaskToBack(true);
    }
}
