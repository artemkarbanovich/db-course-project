package karbanovich.fit.bstu.foodie.models;
import java.util.List;

public class Dish {

    private int id;
    private String name;
    private String cookingTime;
    private String youWillNeed;
    private int dishWeight;
    private double price;
    private String ingredients;
    private List<Photo> photos;

    public Dish(int id, String name, String cookingTime, String youWillNeed,
                int dishWeight, double price, String ingredients, List<Photo> photos) {
        this.id = id;
        this.name = name;
        this.cookingTime = cookingTime;
        this.youWillNeed = youWillNeed;
        this.dishWeight = dishWeight;
        this.price = price;
        this.ingredients = ingredients;
        this.photos = photos;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getCookingTime() { return cookingTime; }
    public String getYouWillNeed() { return youWillNeed; }
    public int getDishWeight() { return dishWeight; }
    public double getPrice() { return price; }
    public String getIngredients() { return ingredients; }
    public List<Photo> getPhotos() { return photos; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setCookingTime(String cookingTime) { this.cookingTime = cookingTime; }
    public void setYouWillNeed(String youWillNeed) { this.youWillNeed = youWillNeed; }
    public void setDishWeight(int dishWeight) { this.dishWeight = dishWeight; }
    public void setPrice(double price) { this.price = price; }
    public void setIngredients(String ingredients) { this.ingredients = ingredients; }
    public void setPhotos(List<Photo> photos) { this.photos = photos; }
}
