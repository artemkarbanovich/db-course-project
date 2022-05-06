package karbanovich.fit.bstu.foodie.database;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseBuilder extends SQLiteOpenHelper {

    private static final int SCHEMA = 1;
    private static final String DATABASE_NAME = "Foodie";
    private static final  String USERS_TABLE = "Users";
    private static final String STATISTICS_TABLE = "Statistics";

    public DatabaseBuilder(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + USERS_TABLE + "(" +
                "Id INTEGER PRIMARY KEY NOT NULL,    " +
                "Email TEXT NOT NULL,                " +
                "FirstName TEXT NOT NULL,            " +
                "LastName TEXT NOT NULL,             " +
                "Birthday TEXT NOT NULL,             " +
                "RegistrationDate TEXT NOT NULL,     " +
                "PhoneNumber TEXT NOT NULL,          " +
                "UpdateStatus TEXT                 );"
        );

        db.execSQL("CREATE TABLE " + STATISTICS_TABLE + "(" +
                "UserId INTEGER PRIMARY KEY NOT NULL,     " +
                "TotalOrdersCount INTEGER NOT NULL,       " +
                "TotalMoneySpent REAL NOT NULL,           " +
                "OrdersCountLastMonth INTEGER NOT NULL,   " +
                "MoneySpentLastMonth REAL NOT NULL,       " +
                "WaitingOrdersCount REAL NOT NULL       );"
        );
    }

    @Override public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE);
        onCreate(db);
    }
}
