package karbanovich.fit.bstu.foodie.database.repositories;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import karbanovich.fit.bstu.foodie.models.Order;
import karbanovich.fit.bstu.foodie.queryParams.OrdersQueryParams;

public class OrdersRepository {

    private static final String ORDERS_TABLE = "Orders";

    public static ArrayList<Order> getOrders(SQLiteDatabase db, int userId, OrdersQueryParams params) {

        String sql = "SELECT * FROM " + ORDERS_TABLE + " WHERE UserId = " + userId;

        if(params.getOrderDateFrom() != null) {
            sql += " AND OrderDate >= date('" + params.getOrderDateFrom() + "')";
        }
        if(params.getOrderDateTo() != null) {
            sql += " AND OrderDate <= date('" + params.getOrderDateTo() + "', '+1 day')";
        }

        sql += " ORDER BY OrderDate DESC";

        Cursor cursor = db.rawQuery(sql, null);

        ArrayList<Order> orders = new ArrayList<>();
        while (cursor.moveToNext()) {
            Order order = new Order();

            order.setOrderDate(cursor.getString(cursor.getColumnIndexOrThrow("OrderDate")));
            order.setDeliveryDate(cursor.getString(cursor.getColumnIndexOrThrow("DeliveryDate")));
            order.setAddress(cursor.getString(cursor.getColumnIndexOrThrow("Address")));
            order.setTotalPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("TotalPrice")));
            order.setStatus(cursor.getString(cursor.getColumnIndexOrThrow("Status")));

            orders.add(order);
        }

        return orders;
    }

    public static void addOrders(SQLiteDatabase db, ArrayList<Order> orders, int userId) {
        for(Order order : orders) {
            ContentValues values = new ContentValues();

            values.put("UserId", userId);
            values.put("OrderDate", order.getOrderDate());
            values.put("DeliveryDate", order.getDeliveryDate());
            values.put("Address", order.getAddress());
            values.put("TotalPrice", order.getTotalPrice());
            values.put("Status", order.getStatus());

            db.insert(ORDERS_TABLE, null, values);
        }
    }

    public static void deleteAllOrders(SQLiteDatabase db, int userId) {
        db.delete(ORDERS_TABLE, "UserId = ?", new String[]{ String.valueOf(userId) });
    }

    public static boolean isOrdersExist(SQLiteDatabase db, int userId) {
        return db.rawQuery("SELECT * FROM " + ORDERS_TABLE + " WHERE UserId = " + userId + ";",
                null).getCount() > 0;
    }
}
