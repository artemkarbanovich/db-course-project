package karbanovich.fit.bstu.foodie.views;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import com.google.android.material.navigation.NavigationView;

import karbanovich.fit.bstu.foodie.AccountSingleton;
import karbanovich.fit.bstu.foodie.R;
import karbanovich.fit.bstu.foodie.helpers.SharedPreferencesHelper;

public class DrawerBaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override public void setContentView(View view) {
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer_base, null);
        FrameLayout container = drawerLayout.findViewById(R.id.activityContainer);
        container.addView(view);
        super.setContentView(drawerLayout);

        Toolbar toolbar = drawerLayout.findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = drawerLayout.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.menu_drawer_open, R.string.menu_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        switch (item.getItemId()) {
            case R.id.nav_profile: startActivity(new Intent(this, ProfileActivity.class));
                overridePendingTransition(0, 0); break;
            case R.id.nav_dishes: startActivity(new Intent(this, DishesActivity.class));
                overridePendingTransition(0, 0); break;
            case R.id.nav_cart: startActivity(new Intent(this, CartActivity.class));
                overridePendingTransition(0, 0); break;
            case R.id.nav_orders: startActivity(new Intent(this, OrdersActivity.class));
                overridePendingTransition(0, 0); break;
            case R.id.nav_statistics: startActivity(new Intent(this, StatisticsActivity.class));
                overridePendingTransition(0, 0); break;
            case R.id.nav_logout:
                AccountSingleton.destroyAccount();
                SharedPreferencesHelper.deleteAccount(this);
                startActivity(new Intent(this, PreviewActivity.class));
                break;
        }
        return false;
    }

    protected void allocateActivityTitle(String titleString) {
        if(getSupportActionBar() != null)
            getSupportActionBar().setTitle(titleString);
    }
}
