package net.infobank.lab.testparseapp;

import android.content.Context;
import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Describe about this class here...
 *
 * @author ohjongin
 * @since 1.0
 * 15. 7. 1
 */
public class IbPhonesMiscUtils {
    public static SimpleDateFormat getSimpleDateFormat() {
        return new SimpleDateFormat("M/d H:mm:ss");
    }

    public static String getDateTimeString(Context context, long time_milis) {
        SimpleDateFormat sdf = getSimpleDateFormat();
        String datetime = sdf.format(new Date(time_milis));

        String duration_str = "";
        long duration = System.currentTimeMillis() - time_milis;
        if (duration > 0) {
            if (duration < DateUtils.MINUTE_IN_MILLIS) {
                duration_str = String.format(context.getString(R.string.datetime_in_seconds), (int) (duration / DateUtils.SECOND_IN_MILLIS));
            } else if (duration < DateUtils.HOUR_IN_MILLIS) {
                duration_str = String.format(context.getString(R.string.datetime_in_minutes), (int) (duration / DateUtils.MINUTE_IN_MILLIS));
            } else if (duration < DateUtils.HOUR_IN_MILLIS * 24) {
                duration_str = String.format(context.getString(R.string.datetime_in_hours), (int) (duration / DateUtils.HOUR_IN_MILLIS));
            }
        }

        if (duration_str.length() > 0) {
            datetime = duration_str + ", " + datetime;
        }
        return datetime;
    }
}
