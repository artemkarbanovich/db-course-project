package karbanovich.fit.bstu.foodie.queryParams;

public class OrdersQueryParams {

    private String orderDateFrom;
    private String orderDateTo;

    public OrdersQueryParams(String orderDateFrom, String orderDateTo) {
        this.orderDateFrom = orderDateFrom;
        this.orderDateTo = orderDateTo;
    }

    public OrdersQueryParams() { }

    public String getOrderDateFrom() { return orderDateFrom; }
    public String getOrderDateTo() { return orderDateTo; }

    public void setOrderDateFrom(String orderDateFrom) { this.orderDateFrom = orderDateFrom; }
    public void setOrderDateTo(String orderDateTo) { this.orderDateTo = orderDateTo; }
}
