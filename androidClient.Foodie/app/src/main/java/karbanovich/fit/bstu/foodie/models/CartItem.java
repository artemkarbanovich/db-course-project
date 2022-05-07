package karbanovich.fit.bstu.foodie.models;

public class CartItem {
    public int dishId;
    public String name;
    public double price;
    public String photoUrl;
    public int count;

    public CartItem(int dishId, String name, double price, String photoUrl, int count) {
        this.dishId = dishId;
        this.name = name;
        this.price = price;
        this.photoUrl = photoUrl;
        this.count = count;
    }

    public int getDishId() { return dishId; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getImageUrl() { return photoUrl; }
    public int getCount() { return count; }

    public void setDishId(int dishId) { this.dishId = dishId; }
    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setImageUrl(String photoUrl) { this.photoUrl = photoUrl; }
    public void setCount(int count) { this.count = count; }
}
