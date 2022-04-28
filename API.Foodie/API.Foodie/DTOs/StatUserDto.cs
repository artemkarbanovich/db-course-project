namespace API.Foodie.DTOs;

public class StatUserDto
{
    public int TotalOrdersCount { get; set; }
    public decimal TotalMoneySpent { get; set; }
    public int OrdersCountLastMonth { get; set; }
    public decimal MoneySpentLastMonth { get; set; }
    public int WaitingOrdersCount { get; set; }
}
