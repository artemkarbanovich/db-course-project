package karbanovich.fit.bstu.foodie.views;
import android.os.Bundle;
import karbanovich.fit.bstu.foodie.databinding.ActivityStatisticBinding;

public class StatisticsActivity extends DrawerBaseActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStatisticBinding activityStatisticBinding = ActivityStatisticBinding.inflate(getLayoutInflater());
        setContentView(activityStatisticBinding.getRoot());
        allocateActivityTitle("Statistics");
    }

    @Override public void onBackPressed() {
        this.moveTaskToBack(true);
    }
}