package karbanovich.fit.bstu.foodie.database.repositories;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import karbanovich.fit.bstu.foodie.models.Dish;
import karbanovich.fit.bstu.foodie.models.Photo;
import karbanovich.fit.bstu.foodie.queryParams.DishesQueryParams;

public class DishesRepository {

    private static final String DISHES_TABLE = "Dishes";
    private static final String PHOTOS_TABLE = "Photos";

    public static ArrayList<Dish> getDishes(SQLiteDatabase db, int userId, DishesQueryParams params) {
        String sqlOrderBy = "";
        if(params.getOrderBy().equals("Name")) {
            sqlOrderBy = " ORDER BY Name " + params.getOrderByType();
        } else if(params.getOrderBy().equals("Price")) {
            sqlOrderBy = " ORDER BY Price " + params.getOrderByType();
        }

        String sql = "SELECT * FROM " + DISHES_TABLE + " WHERE UserId = " + userId +
                " AND Name LIKE '%" + params.getNameSearchStr() + "%'" + sqlOrderBy;

        Cursor cursor = db.rawQuery(sql, null);

        ArrayList<Dish> dishes = new ArrayList<>();
        while (cursor.moveToNext()) {
            Dish dish = new Dish();

            int dishGlobalId = cursor.getInt(cursor.getColumnIndexOrThrow("Id"));
            dish.setId(cursor.getInt(cursor.getColumnIndexOrThrow("DishId")));
            dish.setName(cursor.getString(cursor.getColumnIndexOrThrow("Name")));
            dish.setCookingTime(cursor.getString(cursor.getColumnIndexOrThrow("CookingTime")));
            dish.setYouWillNeed(cursor.getString(cursor.getColumnIndexOrThrow("YouWillNeed")));
            dish.setDishWeight(cursor.getInt(cursor.getColumnIndexOrThrow("DishWeight")));
            dish.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("Price")));
            dish.setIngredients(cursor.getString(cursor.getColumnIndexOrThrow("Ingredients")));

            ArrayList<Photo> photos = new ArrayList<>();

            Cursor cursorPhotos = db.rawQuery(
                    "SELECT * FROM " + PHOTOS_TABLE + " WHERE DishId = " + dishGlobalId, null);

            while (cursorPhotos.moveToNext()) {
                Photo photo = new Photo();

                photo.setId(cursor.getInt(cursorPhotos.getColumnIndexOrThrow("PhotoId")));
                photo.setUrl(cursor.getString(cursorPhotos.getColumnIndexOrThrow("Url")));

                photos.add(photo);
            }

            dish.setPhotos(photos);
            dishes.add(dish);
        }

        return dishes;
    }

    public static void addDishes(SQLiteDatabase db, ArrayList<Dish> dishes, int userId) {
        for(Dish dish: dishes) {
            ContentValues dishValues = new ContentValues();

            dishValues.put("UserId", userId);
            dishValues.put("DishId", dish.getId());
            dishValues.put("Name", dish.getName());
            dishValues.put("CookingTime", dish.getCookingTime());
            dishValues.put("YouWillNeed", dish.getYouWillNeed());
            dishValues.put("DishWeight", dish.getDishWeight());
            dishValues.put("Price", dish.getPrice());
            dishValues.put("Ingredients", dish.getIngredients());

            long insertedDishId = db.insert(DISHES_TABLE, null, dishValues);

            for (Photo photo: dish.getPhotos()) {
                ContentValues photoValues = new ContentValues();

                photoValues.put("DishId", insertedDishId);
                photoValues.put("PhotoId", photo.getId());
                photoValues.put("Url", photo.getUrl());

                db.insert(PHOTOS_TABLE, null, photoValues);
            }
        }
    }

    public static void deleteAllDishes(SQLiteDatabase db, int userId) {
        db.delete(DISHES_TABLE, "UserId = ?", new String[]{ String.valueOf(userId) });
    }

    public static boolean isDishesExist(SQLiteDatabase db, int userId) {
        return db.rawQuery("SELECT * FROM " + DISHES_TABLE + " WHERE UserId = " + userId + ";",
                null).getCount() > 0;
    }
}
