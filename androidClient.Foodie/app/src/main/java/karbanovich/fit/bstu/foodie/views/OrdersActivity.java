package karbanovich.fit.bstu.foodie.views;
import android.os.Bundle;
import karbanovich.fit.bstu.foodie.databinding.ActivityOrdersBinding;

public class OrdersActivity extends DrawerBaseActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityOrdersBinding activityOrdersBinding = ActivityOrdersBinding.inflate(getLayoutInflater());
        setContentView(activityOrdersBinding.getRoot());
        allocateActivityTitle("Orders");
    }

    @Override public void onBackPressed() {
        this.moveTaskToBack(true);
    }
}