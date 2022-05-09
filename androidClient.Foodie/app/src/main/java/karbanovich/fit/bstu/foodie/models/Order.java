package karbanovich.fit.bstu.foodie.models;

public class Order {

    private String orderDate;
    private String deliveryDate;
    private double totalPrice;
    private String status;
    private String address;

    public Order(String orderDate, String deliveryDate, double totalPrice, String status, String address) {
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
        this.totalPrice = totalPrice;
        this.status = status;
        this.address = address;
    }

    public Order() { }

    public String getOrderDate() { return orderDate; }
    public String getDeliveryDate() { return deliveryDate; }
    public double getTotalPrice() { return totalPrice; }
    public String getStatus() { return status; }
    public String getAddress() { return address; }

    public void setOrderDate(String orderDate) { this.orderDate = orderDate; }
    public void setDeliveryDate(String deliveryDate) { this.deliveryDate = deliveryDate; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public void setStatus(String status) { this.status = status; }
    public void setAddress(String address) { this.address = address; }
}
