package karbanovich.fit.bstu.foodie.models;

public class Statistics {
    private int totalOrdersCount;
    private double totalMoneySpent;
    private int ordersCountLastMonth;
    private double moneySpentLastMonth;
    private  int waitingOrdersCount;

    public Statistics(int totalOrdersCount, double totalMoneySpent, int ordersCountLastMonth,
                      double moneySpentLastMonth, int waitingOrdersCount) {
        this.totalOrdersCount = totalOrdersCount;
        this.totalMoneySpent = totalMoneySpent;
        this.ordersCountLastMonth = ordersCountLastMonth;
        this.moneySpentLastMonth = moneySpentLastMonth;
        this.waitingOrdersCount = waitingOrdersCount;
    }

    public int getTotalOrdersCount() { return totalOrdersCount; }
    public double getTotalMoneySpent() { return totalMoneySpent; }
    public int getOrdersCountLastMonth() { return ordersCountLastMonth; }
    public double getMoneySpentLastMonth() { return moneySpentLastMonth; }
    public int getWaitingOrdersCount() { return waitingOrdersCount; }

    public void setTotalOrdersCount(int totalOrdersCount) { this.totalOrdersCount = totalOrdersCount; }
    public void setTotalMoneySpent(double totalMoneySpent) { this.totalMoneySpent = totalMoneySpent; }
    public void setOrdersCountLastMonth(int ordersCountLastMonth) { this.ordersCountLastMonth = ordersCountLastMonth; }
    public void setMoneySpentLastMonth(double moneySpentLastMonth) { this.moneySpentLastMonth = moneySpentLastMonth; }
    public void setWaitingOrdersCount(int waitingOrdersCount) { this.waitingOrdersCount = waitingOrdersCount; }
}
