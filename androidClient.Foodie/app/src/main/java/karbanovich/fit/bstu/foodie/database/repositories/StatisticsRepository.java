package karbanovich.fit.bstu.foodie.database.repositories;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import karbanovich.fit.bstu.foodie.models.Statistics;

public class StatisticsRepository {
    private static final  String STATISTICS_TABLE = "Statistics";

    public static void insertStatistics(SQLiteDatabase db, Statistics statistics, int userId) {
        ContentValues values = new ContentValues();

        values.put("UserId", String.valueOf(userId));
        values.put("TotalOrdersCount", statistics.getTotalOrdersCount());
        values.put("TotalMoneySpent", statistics.getTotalMoneySpent());
        values.put("OrdersCountLastMonth ", statistics.getOrdersCountLastMonth());
        values.put("MoneySpentLastMonth", statistics.getMoneySpentLastMonth());
        values.put("WaitingOrdersCount", statistics.getWaitingOrdersCount());

        db.insert(STATISTICS_TABLE, null, values);
    }

    public static Statistics getStatistics(SQLiteDatabase db, int userId) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + STATISTICS_TABLE + " WHERE UserId = " + userId + ";",
                null);

        if(cursor.moveToNext()) {
            return new Statistics(
                    cursor.getInt(cursor.getColumnIndexOrThrow("TotalOrdersCount")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("TotalMoneySpent")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("OrdersCountLastMonth")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("MoneySpentLastMonth")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("WaitingOrdersCount"))
            );
        }
        return null;
    }

    public static void updateStatistics(SQLiteDatabase db, Statistics statistics, int userId) {
        ContentValues values = new ContentValues();

        values.put("TotalOrdersCount", statistics.getTotalOrdersCount());
        values.put("TotalMoneySpent", statistics.getTotalMoneySpent());
        values.put("OrdersCountLastMonth ", statistics.getOrdersCountLastMonth());
        values.put("MoneySpentLastMonth", statistics.getMoneySpentLastMonth());
        values.put("WaitingOrdersCount", statistics.getWaitingOrdersCount());

        db.update(STATISTICS_TABLE, values, "UserId = ?", new String[]{ String.valueOf(userId) });
    }

    public static void deleteStatistics(SQLiteDatabase db, int userId) {
        db.delete(STATISTICS_TABLE, "UserId = ?", new String[]{ String.valueOf(userId) });
    }

    public static boolean isStatisticsExist(SQLiteDatabase db, int userId) {
        return db.rawQuery("SELECT * FROM " + STATISTICS_TABLE + " WHERE UserId = " + userId + ";",
                null).getCount() > 0;
    }
}
