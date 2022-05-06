package karbanovich.fit.bstu.foodie.database;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseBuilder extends SQLiteOpenHelper {

    private static final int SCHEMA = 1;
    private static final String DATABASE_NAME = "Foodie";
    private static final  String USERS_TABLE = "Users";
    private static final String STATISTICS_TABLE = "Statistics";
    private static final String DISHES_TABLE = "Dishes";
    private static final String PHOTOS_TABLE = "Photos";

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
        db.execSQL("CREATE TABLE " + DISHES_TABLE + "           (" +
                "Id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,  " +
                "UserId INTEGER NOT NULL,                        " +
                "DishId INTEGER NOT NULL,                        " +
                "Name TEXT NOT NULL,                             " +
                "CookingTime TEXT NOT NULL,                      " +
                "YouWillNeed TEXT NOT NULL,                      " +
                "DishWeight INTEGER NOT NULL,                    " +
                "Price REAL NOT NULL,                            " +
                "Ingredients TEXT NOT NULL                     );"
        );
        db.execSQL("CREATE TABLE " + PHOTOS_TABLE + "                          (" +
                "Id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,                 " +
                "DishId INTEGER NOT NULL,                                       " +
                "PhotoId INTEGER NOT NULL,                                      " +
                "Url TEXT NOT NULL,                                             " +
                "FOREIGN KEY(DishId) REFERENCES " + DISHES_TABLE + "(DishId)  );"
        );
    }

    @Override public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + STATISTICS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PHOTOS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DISHES_TABLE);
        onCreate(db);
    }
}
