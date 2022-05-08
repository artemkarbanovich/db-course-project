package karbanovich.fit.bstu.foodie.models;

public class OrderAddDishes {

    private int dishId;
    private int dishesCount;

    public OrderAddDishes(int dishId, int dishesCount) {
        this.dishId = dishId;
        this.dishesCount = dishesCount;
    }

    public int getDishId() { return dishId; }
    public int getDishesCount() { return dishesCount; }

    public void setDishId(int dishId) { this.dishId = dishId; }
    public void setDishesCount(int dishesCount) { this.dishesCount = dishesCount; }
}
