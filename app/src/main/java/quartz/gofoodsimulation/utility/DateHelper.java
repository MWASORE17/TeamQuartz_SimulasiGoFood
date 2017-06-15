package quartz.gofoodsimulation.utility;

import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sxio on 31-May-17.
 */

public class DateHelper {
    public static String format(String string) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = format.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return DateFormat.format("dd MMM yyyy", date).toString();
    }

    public static Date convertToDate(String string) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = format.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String convertToString(Date date) {
        return DateFormat.format("yyyy-MM-dd", date).toString();
    }
}
