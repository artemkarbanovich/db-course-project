namespace API.Foodie.Helpers.QueryParams;

public class OrderUserListParams
{
    private string orderDateFrom = null;
    private string orderDateTo = null;

    public string OrderDateFrom
    {
        get
        {
            if (DateTime.TryParseExact(orderDateFrom, "yyyyMMdd", CultureInfo.InvariantCulture, DateTimeStyles.None, out DateTime date))
                return date.ToString("yyyy-MM-dd");

            return null;
        }
        set => orderDateFrom = value;
    }
    public string OrderDateTo
    {
        get
        {
            if (DateTime.TryParseExact(orderDateTo, "yyyyMMdd", CultureInfo.InvariantCulture, DateTimeStyles.None, out DateTime date))
                return date.ToString("yyyy-MM-dd");

            return null;
        }
        set => orderDateTo = value;
    }
}
