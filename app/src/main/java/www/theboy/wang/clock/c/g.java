package www.theboy.wang.clock.c;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import www.theboy.wang.clock.R;
import www.theboy.wang.clock.bean.ClockBean;
import www.theboy.wang.clock.common.ClockUtil;
import www.theboy.wang.clock.common.Constant;
import www.theboy.wang.clock.common.log.Logger;
import www.theboy.wang.clock.service.ClockService;

/**
 * Created by wands_wang on 2017/10/18.
 */

public class g {
    private Context context;

    public g() {

    }

    public g(Context context) {
        this.context = context;
    }

    public void b(ClockBean clockBean) {

        long oneDayInMillis = 86400000;
        long clockTime = ClockUtil.getTimeInMillis(clockBean.getSetHour(), clockBean.getSetMin());

        Intent intent = new Intent(context.getApplicationContext(), ClockService.class);
        intent.putExtra(Constant.INTENT_KEY_CLOCK_ID, clockBean.clockId);
        intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);

        PendingIntent pendingIntent = PendingIntent.getService(context.getApplicationContext(), clockBean.clockId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        long current = System.currentTimeMillis();
        while (clockTime - current < 0) {
            clockTime += oneDayInMillis;
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        clockBean.setTime = clockTime;
        Date date = new Date(clockBean.setTime);
        Logger.log("手动注册一个闹钟：" + formatter.format(date));

        manager.setRepeating(AlarmManager.RTC_WAKEUP, clockBean.setTime, oneDayInMillis, pendingIntent);
        showToast(clockBean, clockBean.setTime);
    }

    private void showToast(ClockBean clockBean, long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int setDay = calendar.get(Calendar.DAY_OF_WEEK);

        int day = 8;
        for (int i = 0; i < clockBean.period.length; i++) {
            if (clockBean.period[i]) {
                day = Math.min((i - (setDay + 5) % 7 + 7) % 7, day);
            }
        }

        calendar.setTimeInMillis(time - System.currentTimeMillis());
        int hour = (int) ((time - System.currentTimeMillis()) / 1000 / 60 / 60);

        int minute = (int) ((time - System.currentTimeMillis()) / 1000 / 60 % 60);

        String toast;

        if (day != 8 && day != 0) {
            //cond_4
            toast = context.getString(R.string.alarm_latter_d, String.valueOf(day), String.valueOf(hour), String.valueOf(minute));
        } else {
            if (hour > 0) {
                toast = context.getString(R.string.alarm_latter_h, String.valueOf(hour), String.valueOf(minute));
            } else {
                toast = context.getString(R.string.alarm_latter_m, String.valueOf(minute));
            }
        }

        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
    }
}
