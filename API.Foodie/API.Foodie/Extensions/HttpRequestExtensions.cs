namespace API.Foodie.Extensions;

public static class HttpRequestExtensions
{
    public static bool IsMobile(this HttpRequest request)
    {
        var userAgent = request.Headers["user-agent"].ToString();

        if (string.IsNullOrEmpty(userAgent))
        {
            return false;
        }
        else if (Regex.IsMatch(userAgent, "(tablet|ipad|playbook|silk)|(android(?!.*mobile))", RegexOptions.IgnoreCase))
        {
            return true;
        }

        const string mobileRegex = "blackberry|iphone|mobile|windows ce|opera mini|htc|sony|palm|symbianos|ipad|ipod|blackberry|bada|kindle|symbian|sonyericsson|android|samsung|nokia|wap|motor";

        if (Regex.IsMatch(userAgent, mobileRegex, RegexOptions.IgnoreCase))
        {
            return true;
        }

        return false;
    }
}
