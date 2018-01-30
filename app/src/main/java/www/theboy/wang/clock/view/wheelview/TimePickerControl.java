package www.theboy.wang.clock.view.wheelview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import www.theboy.wang.clock.R;
import www.theboy.wang.clock.view.wheelview.ValueAdapterImpl;
import www.theboy.wang.clock.view.wheelview.WheelView;

/**
 * Created by wands_wang on 2017/9/26.
 */

public class TimePickerControl {
    private static int e = 0;
    private static int f = 23;

    private boolean d;

    private View contentView;
    private WheelView first;
    private WheelView second;

    public TimePickerControl(Context context) {
        contentView = LayoutInflater.from(context).inflate(R.layout.timepicker, null);
        d = false;
    }

    public View initTimePicker(int maxIndex, String formatStr1, String str1, String formatStr2, String str2, WheelView.Callback callback) {
        first = (WheelView) contentView.findViewById(R.id.year);
        //设置Adapter
        first.setAdapter(new ValueAdapterImpl(maxIndex, formatStr1));
        first.setCyclic(true);
        first.setLabel(str1);
        first.setCurrentItem(0);
        first.setCallback(callback);

        second = (WheelView) contentView.findViewById(R.id.month);
        second.setAdapter(new ValueAdapterImpl(59, formatStr2));
        second.setCyclic(true);
        second.setLabel(str2);
        second.setCallback(callback);
        second.setCurrentItem(0);

        return contentView;
    }

    public int getFirstCurrentItem() {
        return first.getCurrentItem();
    }

    public void setFirstCurrentItem(int item) {
        first.setCurrentItem(item);
    }

    public int getSecondCurrentItem() {
        return second.getCurrentItem();
    }

    public void setSecondCurrentItem(int item) {
        second.setCurrentItem(item);
    }
}
