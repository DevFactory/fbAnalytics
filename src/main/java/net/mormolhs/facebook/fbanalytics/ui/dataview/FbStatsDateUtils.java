package net.mormolhs.facebook.fbanalytics.ui.dataview;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by toikonomakos on 3/27/14.
 */
public class FbStatsDateUtils {

    public static String convertTime(String timeInput) {
        long time = Long.valueOf(timeInput);
        Date date = new Date(time * 1000);
        Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
        return format.format(date).toString();
    }

}
