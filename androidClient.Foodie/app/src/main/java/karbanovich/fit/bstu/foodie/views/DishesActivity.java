package karbanovich.fit.bstu.foodie.views;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import karbanovich.fit.bstu.foodie.R;

public class DishesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dishes);
    }

    @Override public void onBackPressed() {
        this.moveTaskToBack(true);
    }
}
