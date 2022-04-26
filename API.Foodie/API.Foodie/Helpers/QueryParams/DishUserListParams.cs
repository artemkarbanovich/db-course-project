namespace API.Foodie.Helpers.QueryParams;

public class DishUserListParams
{
    private static readonly string[] OrderByValues = { "NOT_MATTER", "NAME", "PRICE" };
    private static readonly string[] OrderByTypeValues = { "ASC", "DESC" };

    private string orderBy = "NOT_MATTER";
    private string orderByType = "ASC";

    public string NameSearchStr { get; set; } = "";
    public string OrderBy
    {
        get => orderBy;
        set
        {
            if (OrderByValues.Contains(value.ToUpper()))
                orderBy = value.ToUpper();
            else
                orderBy = "NOT_MATTER";
        }
    }
    public string OrderByType
    {
        get => orderByType;
        set
        {
            if (OrderByTypeValues.Contains(value.ToUpper()))
                orderByType = value.ToUpper();
            else
                orderByType = "ASC";
        }
    }
}
