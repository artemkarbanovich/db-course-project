package karbanovich.fit.bstu.foodie.helpers;

public class DateTimeHelper {

    public static String getGeneralDateFormat(int year, int month, int day) {
        String date = "" + year + "-";

        if(month < 10) date += "0" + month + "-";
        else date += month + "-";

        if(day < 10) date += "0" + day;
        else date += day;

        return date;
    }
}
