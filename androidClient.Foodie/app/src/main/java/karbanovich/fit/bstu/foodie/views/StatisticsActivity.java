package karbanovich.fit.bstu.foodie.views;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import karbanovich.fit.bstu.foodie.R;
import karbanovich.fit.bstu.foodie.database.DatabaseBuilder;
import karbanovich.fit.bstu.foodie.database.repositories.StatisticsRepository;
import karbanovich.fit.bstu.foodie.databinding.ActivityStatisticsBinding;
import karbanovich.fit.bstu.foodie.helpers.AccountHelper;
import karbanovich.fit.bstu.foodie.helpers.SystemHelper;
import karbanovich.fit.bstu.foodie.models.Statistics;
import karbanovich.fit.bstu.foodie.network.OnFetchDataListener;
import karbanovich.fit.bstu.foodie.network.requestManagers.RequestStatisticsManager;
import retrofit2.Response;

public class StatisticsActivity extends DrawerBaseActivity {

    private TextView noData;
    private ProgressBar progressBar;
    private TextView totalOrdersCount;
    private TextView totalMoneySpent;
    private TextView ordersCountLastMonth;
    private TextView moneySpentLastMonth;
    private TextView waitingOrdersCount;

    private Context context = this;
    private SQLiteDatabase db;
    private Statistics currentStatistics;

    private final OnFetchDataListener<Statistics> getStatisticsListener = new OnFetchDataListener<Statistics>() {
        @Override
        public void onFetchData(Response<Statistics> response) {
            if(response.isSuccessful()) {
                currentStatistics = response.body();
                setStatistics(currentStatistics);

                if(StatisticsRepository.isStatisticsExist(db, AccountHelper.getUserId(context)))
                    StatisticsRepository.updateStatistics(db, currentStatistics, AccountHelper.getUserId(context));
                else
                    StatisticsRepository.insertStatistics(db, currentStatistics, AccountHelper.getUserId(context));
            }
            else {
                Toast.makeText(context, "Error by getting statistics", Toast.LENGTH_LONG).show();
                setStatistics(null);
            }
            progressBar.setVisibility(View.GONE);
        }
        @Override
        public void onFetchError(Throwable error) {
            Toast.makeText(context, "Error by getting statistics", Toast.LENGTH_LONG).show();
            setStatistics(null);
            progressBar.setVisibility(View.GONE);
        }
    };

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStatisticsBinding activityStatisticsBinding = ActivityStatisticsBinding.inflate(getLayoutInflater());
        setContentView(activityStatisticsBinding.getRoot());
        allocateActivityTitle("Statistics");

        bindingView();
        getData();
    }

    private void getData() {
        if(SystemHelper.isNetworkAvailable(context)) {
            progressBar.setVisibility(View.VISIBLE);
            RequestStatisticsManager requestManager = new RequestStatisticsManager(context);
            requestManager.getStatistic(getStatisticsListener);
        }
        else if(!SystemHelper.isNetworkAvailable(context) &&
                StatisticsRepository.isStatisticsExist(db, AccountHelper.getUserId(context)))
        {
            currentStatistics = StatisticsRepository.getStatistics(db, AccountHelper.getUserId(context));
            setStatistics(currentStatistics);
            findViewById(R.id.statistics__may_differ).setVisibility(View.VISIBLE);
        }
        else if(!SystemHelper.isNetworkAvailable(context) &&
                !StatisticsRepository.isStatisticsExist(db, AccountHelper.getUserId(context)))
        {
            setViewVisibility(View.GONE);
            noData.setVisibility(View.VISIBLE);
        }
    }

    private void setStatistics(Statistics statistics) {
        if(statistics != null) {
            totalOrdersCount.setText(checkNull(String.valueOf(statistics.getTotalOrdersCount())));
            totalMoneySpent.setText(checkNull(String.valueOf(statistics.getTotalMoneySpent())));
            ordersCountLastMonth.setText(checkNull(String.valueOf(statistics.getOrdersCountLastMonth())));
            moneySpentLastMonth.setText(checkNull(String.valueOf(statistics.getMoneySpentLastMonth())));
            waitingOrdersCount.setText(checkNull(String.valueOf(statistics.getWaitingOrdersCount())));
        } else {
          totalOrdersCount.setText("-");
          totalMoneySpent.setText("-");
          ordersCountLastMonth.setText("-");
          moneySpentLastMonth.setText("-");
          waitingOrdersCount.setText("-");
        }
    }

    private String checkNull(String str) {
        if(str == null) { return "-"; }
        else return str;
    }

    private void setViewVisibility(int visibility) {
        findViewById(R.id.total_orders_count_container).setVisibility(visibility);
        findViewById(R.id.total_money_spent_container).setVisibility(visibility);
        findViewById(R.id.orders_count_last_month_container).setVisibility(visibility);
        findViewById(R.id.money_spent_last_month_container).setVisibility(visibility);
        findViewById(R.id.waiting_orders_count_container).setVisibility(visibility);
    }

    private void bindingView() {
        noData = findViewById(R.id.statistics__no_data);
        progressBar = findViewById(R.id.statistics__progress_bar);
        totalOrdersCount = findViewById(R.id.total_orders_count_value);
        totalMoneySpent = findViewById(R.id.total_money_spent_value);
        ordersCountLastMonth = findViewById(R.id.orders_count_last_month_value);
        moneySpentLastMonth = findViewById(R.id.money_spent_last_month_value);
        waitingOrdersCount = findViewById(R.id.waiting_orders_count_value);

        db = new DatabaseBuilder(context).getWritableDatabase();
    }

    @Override public void onBackPressed() {
        this.moveTaskToBack(true);
    }
}
