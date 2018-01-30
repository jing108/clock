package www.theboy.wang.clock.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;

import java.util.List;

import www.theboy.wang.clock.bean.ClockBean;

/**
 * Created by wands_wang on 2017/10/18.
 */

public class ClockService extends Service {

    private NotificationManager notificationManager;

    private Handler c = new Handler();

    private Handler b = null;

    private BroadcastReceiver broadcastReceiver = new TimeChangedReceiver();

    private PowerManager.WakeLock wakeLock;

    private List<ClockBean> clockBeanList;

    private List<ClockBean> getClockBeanList() {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        registerReceiver(broadcastReceiver, intentFilter);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (wakeLock == null) {
            PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "bright");
            wakeLock.acquire();
        }


    }

    class TimeChangedReceiver extends BroadcastReceiver {

        private Runnable runnable = new Runnable() {
            @Override
            public void run() {

            }
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_TIME_CHANGED.equals(action) || Intent.ACTION_TIMEZONE_CHANGED.equals(action)) {
                if (c != null) {
                    c.postDelayed()
                }
            }
        }
    }
}
