package www.theboy.wang.clock.bean;

import android.support.annotation.NonNull;

/**
 * Created by wands_wang on 2017/9/26.
 */

public class ClockBean implements Comparable<ClockBean> {

    public int AMorPM; //am:0 or pm:1
    public int intentrequestCode;
    public long addedTime;
    public int clockId;
    public String clockName = "";
    public int curWarmCount;
    public String id = "";
    public int interval; //间隔
    public boolean isClose;
    public boolean isEnhance = true; //加强，提高
    public boolean isVibrate = true; //震动
    public long mLaterRingTime;
    public long mNextRingTime;
    public boolean mSingleClock;
    public int nameID;
    public boolean[] period = {false, false, false, false, false, false, false}; //期
    public long ringAlbum_id; //响铃id
    public String ringUri;
    public long setTime;
    public int voice;
    public int warmCount;


    @Override
    public int compareTo(@NonNull ClockBean o) {
        if (o.nameID == nameID) {
            return clockId - o.clockId;
        }

        return nameID - o.nameID;
    }

    public int getSetHour() {
        int hour = 0;
        if (clockId != 0) {
            hour = clockId / 60;
        }
        return hour;
    }

    public int getSetMin() {
        int min = 0;
        if (clockId != 0) {
            min = clockId % 60;
        }

        return min;
    }

    public boolean isLatterClock() {
        return mLaterRingTime > 0 && curWarmCount > 0;
    }

    public boolean isSingleTime() {
        int count = 0;
        for (boolean b : period) {
            if (b) count++;
        }

        return count == 1;

        //cond_1
    }

    public void resetRemindLatter() {
        curWarmCount = 0;
        mLaterRingTime = 0;
    }
}
