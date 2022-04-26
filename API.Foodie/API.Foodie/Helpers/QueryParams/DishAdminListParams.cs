namespace API.Foodie.Helpers.QueryParams;

public class DishAdminListParams
{
    private static readonly string[] OrderByValues = { "NOT_MATTER", "NAME", "PRICE" };
    private static readonly string[] OrderByTypeValues = { "ASC", "DESC" };
    private static readonly string[] IsVisibleValues = { "NOT_MATTER", "TRUE", "FALSE" };

    private string orderBy = "NOT_MATTER";
    private string orderByType = "ASC";
    private string isVisible = "NOT_MATTER";
    
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
    public string IsVisible
    {
        get => isVisible;
        set
        {
            if (IsVisibleValues.Contains(value.ToUpper()))
                isVisible = value.ToUpper();
            else
                isVisible = "NOT_MATTER";
        }
    }
}
