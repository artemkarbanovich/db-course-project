namespace API.Foodie.DTOs;

public class OrderStatusDto
{
    private static readonly string[] StatusValues = { "ACCEPTED", "IN_WAY", "DELIVERED", "CANCELED" };

    private string status = null;

    public int OrderId { get; set; }
    public string Status
    {
        get => status;
        set
        {
            if (StatusValues.Contains(value.ToUpper()))
                status = value.ToUpper();
            else
                status = null;
        }
    }
}
