package karbanovich.fit.bstu.foodie.views;
import android.os.Bundle;
import karbanovich.fit.bstu.foodie.databinding.ActivityProfileBinding;

public class ProfileActivity extends DrawerBaseActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityProfileBinding activityProfileBinding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(activityProfileBinding.getRoot());
        allocateActivityTitle("Profile");
    }

    @Override public void onBackPressed() {
        this.moveTaskToBack(true);
    }
}
