namespace API.Foodie.DTOs;

public class StatAdminDto
{
    // Users
    public int TotalUsersCount { get; set; }
    public int ActiveUsersCount { get; set; }

    // Money
    public decimal TotalRevenue { get; set; }
    public decimal AvgOrderPrice { get => OrdersCount != 0 ? TotalRevenue / OrdersCount : 0; }

    // Orders
    public int OrdersCount { get => AcceptedOrdersCount + InWayOrdersCount + DeliveredOrdersCount + CanceledOrdersCount; }
    public int AcceptedOrdersCount { get; set; }
    public int InWayOrdersCount { get; set; }
    public int DeliveredOrdersCount { get; set; }
    public int CanceledOrdersCount { get; set; }

    // Dishes
    public int DishesCount { get; set; }
    public int VisibleDishesCount { get; set; }
    public decimal AvgDishPrice { get; set; }
}
