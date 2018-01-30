package www.theboy.wang.clock.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by wands_wang on 2017/9/26.
 */

public class FormatTimeString {
    public static String getFormatTimeString(long timeInMillis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return simpleDateFormat.format(new Date(timeInMillis));
    }
}
