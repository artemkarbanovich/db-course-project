package karbanovich.fit.bstu.foodie.queryParams;

public class DishesQueryParams {

    private String nameSearchStr = "";
    private String orderBy = "NOT_MATTER";
    private String orderByType = "ASC";

    public DishesQueryParams( String nameSearchStr, String orderBy, String orderByType) {
        this.nameSearchStr = nameSearchStr;
        this.orderBy = orderBy;
        this.orderByType = orderByType;
    }

    public DishesQueryParams() { };

    public String getNameSearchStr() { return nameSearchStr; }
    public String getOrderBy() { return orderBy; }
    public String getOrderByType() { return orderByType; }

    public void setNameSearchStr(String nameSearchStr) { this.nameSearchStr = nameSearchStr; }
    public void setOrderBy(String orderBy) { this.orderBy = orderBy; }
    public void setOrderByType(String orderByType) { this.orderByType = orderByType; }
}
