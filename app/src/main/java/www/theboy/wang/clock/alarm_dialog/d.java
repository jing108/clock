package www.theboy.wang.clock.alarm_dialog;

import java.util.Calendar;

import www.theboy.wang.clock.bean.ClockBean;

/**
 * Created by wands_wang on 2017/10/19.
 */

public class d {
    public static long getNextRingTime(ClockBean clockBean, boolean is) {
        if (is) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.SECOND, 0);

            return calendar.getTimeInMillis() + clockBean.interval * 60 * 1000;

        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, clockBean.getSetHour());
            calendar.set(Calendar.MINUTE, clockBean.getSetMin());
            calendar.set(Calendar.SECOND, 0);

            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            int day = 8;
            for (int i = 0; i < clockBean.period.length; i++) {
                if (clockBean.period[i]) {
                    int tmp = (i - (dayOfWeek + 5) % 7 + 7) % 7;
                    if (tmp <= 0) {
                        tmp = 7;
                    }
                    day = Math.min(tmp, day);
                }
            }

            if (day == 8) {
                long result = calendar.getTimeInMillis();
                while (System.currentTimeMillis() - result > 0) {
                    result += 86400000;
                }
                return result;
            } else {
                return day * 24 * 60 * 60 * 1000 + calendar.getTimeInMillis();
            }
        }
    }
}
