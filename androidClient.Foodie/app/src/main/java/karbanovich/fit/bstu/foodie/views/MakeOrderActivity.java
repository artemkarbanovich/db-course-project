package karbanovich.fit.bstu.foodie.views;
import android.content.Intent;
import android.os.Bundle;
import karbanovich.fit.bstu.foodie.databinding.ActivityMakeOrderBinding;

public class MakeOrderActivity extends DrawerBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMakeOrderBinding activityMakeOrderBinding = ActivityMakeOrderBinding.inflate(getLayoutInflater());
        setContentView(activityMakeOrderBinding.getRoot());
        allocateActivityTitle("Make order");

        bindingView();
    }

    private void bindingView() {

    }

    @Override public void onBackPressed() {
        startActivity(new Intent(this, CartActivity.class));
    }
}