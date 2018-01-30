package www.theboy.wang.clock.common;

import java.util.Calendar;

/**
 * Created by wands_wang on 2017/9/26.
 */

public class ClockUtil {

    public static int calculateClockId(int isPm, int minute) {
        int id = minute;
        if (isPm != 0) {
            id += 720;
        }

        return id;
    }

    public static String getFormatTimeString(int isPm, int progressToMaxProgress) {
        int hour = progressToMaxProgress / 60;
        if (isPm == 1) {
            hour += 12;
        }

        int minute = progressToMaxProgress % 60;
        return FormatTimeString.getFormatTimeString(getTimeInMillis(hour, minute));
    }

    public static long getTimeInMillis(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTimeInMillis();
    }

    public static long getTimeInMillisByProgress(int isPm, int progress) {
        int hour = 0;
        if (isPm == 0) {
            hour = progress / 60;
        } else if (isPm == 1){
            hour = progress / 60 + 12;
        }

        int minute = progress % 60;

        return getTimeInMillis(hour, minute);
    }

    public static int getAmOrPm(int hour) {
        if (hour >= 12) {
            return 1;
        } else {
            return 0;
        }
    }
}
