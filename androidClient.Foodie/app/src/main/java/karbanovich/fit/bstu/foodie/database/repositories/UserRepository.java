package karbanovich.fit.bstu.foodie.database.repositories;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import karbanovich.fit.bstu.foodie.models.User;

public class UserRepository {
    private static final  String USERS_TABLE = "Users";

    public static void insertUser(SQLiteDatabase db, User user) {
        ContentValues values = new ContentValues();

        values.put("Id", user.getId());
        values.put("Email", user.getEmail());
        values.put("FirstName", user.getFirstName());
        values.put("LastName", user.getLastName());
        values.put("Birthday", user.getBirthday());
        values.put("RegistrationDate", user.getRegistrationDate());
        values.put("PhoneNumber", user.getPhoneNumber());
        values.put("UpdateStatus", user.getUpdateStatus());

        db.insert(USERS_TABLE, null, values);
    }

    public static User getUser(SQLiteDatabase db, int id) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + USERS_TABLE + " WHERE Id = " + id + ";", null);

        if(cursor.moveToNext()) {
            User user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow("Id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("Email")),
                    cursor.getString(cursor.getColumnIndexOrThrow("FirstName")),
                    cursor.getString(cursor.getColumnIndexOrThrow("LastName")),
                    cursor.getString(cursor.getColumnIndexOrThrow("RegistrationDate")),
                    cursor.getString(cursor.getColumnIndexOrThrow("Birthday")),
                    cursor.getString(cursor.getColumnIndexOrThrow("PhoneNumber"))
            );
            user.setUpdateStatus(cursor.getString(cursor.getColumnIndexOrThrow("UpdateStatus")));

            return user;
        }
        return null;
    }

    public static void updateUser(SQLiteDatabase db, User user) {
        ContentValues values = new ContentValues();

        values.put("Email", user.getEmail());
        values.put("FirstName", user.getFirstName());
        values.put("LastName", user.getLastName());
        values.put("Birthday", user.getBirthday());
        values.put("PhoneNumber", user.getPhoneNumber());
        values.put("UpdateStatus", user.getUpdateStatus());

        db.update(USERS_TABLE, values, "Id = ?", new String[]{ String.valueOf(user.getId()) });
    }

    public static void deleteUser(SQLiteDatabase db, int id) {
        db.delete(USERS_TABLE, "Id = ?", new String[]{ String.valueOf(id) });
    }

    public static boolean isUserExist(SQLiteDatabase db, int id) {
        return db.rawQuery("SELECT * FROM " + USERS_TABLE + " WHERE Id = " + id + ";", null).getCount() > 0;
    }
}
