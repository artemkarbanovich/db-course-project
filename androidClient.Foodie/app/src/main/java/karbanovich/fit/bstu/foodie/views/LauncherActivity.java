package karbanovich.fit.bstu.foodie.views;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import karbanovich.fit.bstu.foodie.helpers.SharedPreferencesHelper;

public class LauncherActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(SharedPreferencesHelper.isAccountExist(this))
            startActivity(new Intent(this, DishesActivity.class));
        else
            startActivity(new Intent(this, PreviewActivity.class));
    }
}
