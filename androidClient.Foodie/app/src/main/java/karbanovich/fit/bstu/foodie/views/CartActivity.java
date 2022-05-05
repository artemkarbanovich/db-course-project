package karbanovich.fit.bstu.foodie.views;
import android.os.Bundle;
import karbanovich.fit.bstu.foodie.databinding.ActivityCartBinding;

public class CartActivity extends DrawerBaseActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCartBinding activityCartBinding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(activityCartBinding.getRoot());
        allocateActivityTitle("Cart");
    }

    @Override public void onBackPressed() {
        this.moveTaskToBack(true);
    }
}