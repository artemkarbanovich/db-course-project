package karbanovich.fit.bstu.foodie.models;
import java.util.ArrayList;

public class OrderAdd {

    private String deliveryDate;
    private String address;
    private ArrayList<OrderAddDishes> dishes;

    public OrderAdd(String deliveryDate, String address, ArrayList<OrderAddDishes> dishes) {
        this.deliveryDate = deliveryDate;
        this.address = address;
        this.dishes = dishes;
    }

    public String getDeliveryDate() { return deliveryDate; }
    public String getAddress() { return address; }
    public ArrayList<OrderAddDishes> getDishes() { return dishes; }

    public void setDeliveryDate(String deliveryDate) { this.deliveryDate = deliveryDate; }
    public void setAddress(String address) { this.address = address; }
    public void setDishes(ArrayList<OrderAddDishes> dishes) { this.dishes = dishes; }
}
