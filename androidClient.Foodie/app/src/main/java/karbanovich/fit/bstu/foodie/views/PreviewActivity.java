package karbanovich.fit.bstu.foodie.views;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import karbanovich.fit.bstu.foodie.R;
import karbanovich.fit.bstu.foodie.helpers.SharedPreferencesHelper;

public class PreviewActivity extends AppCompatActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        if(SharedPreferencesHelper.isAccountExist(this))
            startActivity(new Intent(this, DishesActivity.class));

        findViewById(R.id.preview__register_btn).setOnClickListener(view ->
                startActivity(new Intent(this, RegisterActivity.class)));
        findViewById(R.id.preview__login_btn).setOnClickListener(view ->
                startActivity(new Intent(this, LoginActivity.class)));
    }

    @Override public void onBackPressed() { this.moveTaskToBack(true); }
}
