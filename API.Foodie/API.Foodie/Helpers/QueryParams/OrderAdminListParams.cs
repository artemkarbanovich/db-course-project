namespace API.Foodie.Helpers.QueryParams;

public class OrderAdminListParams
{
    private static readonly string[] StatusValues = { "NOT_MATTER", "ACCEPTED", "IN_WAY", "DELIVERED", "CANCELED" };

    private string status = "NOT_MATTER";

    public string Status
    {
        get => status;
        set
        {
            if (StatusValues.Contains(value.ToUpper()))
                status = value.ToUpper();
            else
                status = "NOT_MATTER";
        }
    }
}
