package www.theboy.wang.clock.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.UUID;

import www.theboy.wang.clock.R;
import www.theboy.wang.clock.bean.ClockBean;
import www.theboy.wang.clock.c.g;
import www.theboy.wang.clock.common.DensityUtil;
import www.theboy.wang.clock.common.ClockUtil;
import www.theboy.wang.clock.sputils.SpOption;
import www.theboy.wang.clock.view.CircularSeekBar;
import www.theboy.wang.clock.view.wheelview.TimePickerControl;
import www.theboy.wang.clock.view.wheelview.WheelView;

/**
 * Created by wands_wang on 2017/9/20.
 */

public class GetupClockActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout getUpRy;

    private CircularSeekBar getUpCircular;

    private CheckBox[] checkBoxes;
    private CheckBox weekBox1;
    private CheckBox weekBox2;
    private CheckBox weekBox3;
    private CheckBox weekBox4;
    private CheckBox weekBox5;
    private CheckBox weekBox6;
    private CheckBox weekBox7;

    private TimePickerControl timePickerControl;

    private View getUpVeil;

    private ClockBean clockBean;

    private int progressToMaxProgress;

    private PopupWindow popupTextWindow;
    private TextView popupTxt;

    private LinearLayout timePickerView;
    private LinearLayout cancelLy;
    private LinearLayout sureLy;
    private Button cancelBtn;
    private Button addBtn;
    private WheelView.Callback callback = new WheelView.Callback() {
    };

    @Override
    protected Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_up);

        getUpRy = (RelativeLayout) findViewById(R.id.get_up_ry);
        getUpCircular = (CircularSeekBar) findViewById(R.id.get_up_circular);

        initCheckBox();
        initCircularSeekBar();
        initWheelView();
        initClockBean();

        cancelBtn = (Button) findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(this);
        addBtn = (Button) findViewById(R.id.add_btn);
        addBtn.setOnClickListener(this);

        getUpVeil = findViewById(R.id.get_up_veil);
        getUpVeil.setOnClickListener(this);
    }

    private void initClockBean() {
        clockBean = new ClockBean();
        clockBean.id = UUID.randomUUID().toString();
        clockBean.clockId = ClockUtil.calculateClockId(0, 420);
        clockBean.curWarmCount = 0;
        clockBean.period = new boolean[]{true, true, true, true, true, false, false};
        clockBean.interval = 10;
        clockBean.isClose = false;
        clockBean.isVibrate = true;
        clockBean.setTime = 0;
        clockBean.voice = 50;
        clockBean.warmCount = 2;
        clockBean.nameID = 0;
        clockBean.clockName = getString(R.string.get_up_clock);
        clockBean.isEnhance = true;
        StringBuilder sb = new StringBuilder("android.resource://");
        sb.append(getPackageName());
        sb.append("/2131099649");
        clockBean.ringUri = sb.toString();

        updateByClockBean(clockBean);
    }

    private void initWheelView() {
        timePickerControl = new TimePickerControl(this);
        timePickerView = (LinearLayout) timePickerControl.initTimePicker(23, "%02d", getString(R.string.label_h), "%02d", getString(R.string.label_m), callback);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        getUpRy.addView(timePickerView, params);

        cancelLy = (LinearLayout) timePickerView.findViewById(R.id.wheel_cancel_ly);
        cancelLy.setOnClickListener(this);
        sureLy = (LinearLayout) timePickerView.findViewById(R.id.wheel_sure_ly);
        sureLy.setOnClickListener(this);

        timePickerView.setVisibility(View.GONE);
    }

    private void initCircularSeekBar() {
        getUpCircular.setTouchAble(true);
        getUpCircular.a();
        getUpCircular.setLabelText1(getString(R.string.label_h));
        getUpCircular.setLabelText2(getString(R.string.label_m));
        getUpCircular.setMaxProgress(720);
        getUpCircular.setProgressToMaxProgress(0);

        getUpCircular.setOnViewClickListener(new CircularSeekBar.OnViewClickListener() {
            @Override
            public void onViewClick() {
                timePickerView.setVisibility(View.VISIBLE);
                getUpVeil.setVisibility(View.VISIBLE);

                timePickerControl.setFirstCurrentItem(ClockUtil.calculateClockId(clockBean.AMorPM, progressToMaxProgress) / 60);
                timePickerControl.setSecondCurrentItem(ClockUtil.calculateClockId(clockBean.AMorPM, progressToMaxProgress) % 60);
            }
        });

        getUpCircular.setSeekBarChangeListener(new CircularSeekBar.SeekBarChangeListener() {
            @Override
            public void seekBarChanged(float progressToMaxProgress) {
                GetupClockActivity.this.progressToMaxProgress = (int) progressToMaxProgress;
                String timeFormat = ClockUtil.getFormatTimeString(clockBean.AMorPM, GetupClockActivity.this.progressToMaxProgress);
                getUpCircular.setCenterText(timeFormat);
                if (popupTextWindow != null) {
                    if (popupTextWindow.isShowing()) {
                        if (popupTxt != null) {
                            popupTxt.setText(timeFormat);
                        }
                    }
                }
            }
        });

        getUpCircular.setCircleChangeListener(new CircularSeekBar.CircleChangeListener() {
            @Override
            public void circleChange() {
                clockBean.AMorPM = (clockBean.AMorPM + 1) % 2;
            }
        });

        getUpCircular.setOnShowBarListener(new CircularSeekBar.OnShowBarListener() {
            @Override
            public void showBar(boolean show) {
                if (show) {
                    showPopupWindow();
                    popupTxt.setText(getUpCircular.getCenterText());
                } else {
                    if (popupTextWindow != null) {
                        if (popupTextWindow.isShowing()) {
                            popupTextWindow.dismiss();
                        }
                    }
                }
            }
        });

        getUpCircular.setMoveRadio(2.5f);
    }

    private void initCheckBox() {
        checkBoxes = new CheckBox[7];
        weekBox1 = (CheckBox) findViewById(R.id.week_box_1);
        checkBoxes[0] = weekBox1;
        weekBox2 = (CheckBox) findViewById(R.id.week_box_2);
        checkBoxes[1] = weekBox2;
        weekBox3 = (CheckBox) findViewById(R.id.week_box_3);
        checkBoxes[2] = weekBox3;
        weekBox4 = (CheckBox) findViewById(R.id.week_box_4);
        checkBoxes[3] = weekBox4;
        weekBox5 = (CheckBox) findViewById(R.id.week_box_5);
        checkBoxes[4] = weekBox5;
        weekBox6 = (CheckBox) findViewById(R.id.week_box_6);
        checkBoxes[5] = weekBox6;
        weekBox7 = (CheckBox) findViewById(R.id.week_box_7);
        checkBoxes[6] = weekBox7;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_btn: //--1
                SpOption.addUsedAppSp(this);
                clockBean.setTime = ClockUtil.getTimeInMillisByProgress(clockBean.AMorPM, progressToMaxProgress);
                clockBean.clockId = ClockUtil.calculateClockId(clockBean.AMorPM, progressToMaxProgress);
                for (int i = 0; i < clockBean.period.length; i++) {
                    clockBean.period[i] = checkBoxes[i].isChecked();
                }

                g x = new g(this);
                x.b(clockBean);

                clockBean.mNextRingTime = ;


                break;

            case R.id.cancel_btn: //0
                SpOption.addUsedAppSp(this);
                finish(RESULT_CANCELED);
                break;

            case R.id.get_up_veil: //4
                goneTimePicker();
                break;

            case R.id.wheel_cancel_ly: //2
                goneTimePicker();
                break;

            case R.id.wheel_sure_ly: //3
                clockBean.AMorPM = ClockUtil.getAmOrPm(timePickerControl.getFirstCurrentItem());
                progressToMaxProgress = (timePickerControl.getFirstCurrentItem() % 12) * 60 + timePickerControl.getSecondCurrentItem();
                getUpCircular.setProgressToMaxProgress(progressToMaxProgress);
                goneTimePicker();
                break;
        }
    }

    private void updateByClockBean(ClockBean clockBean) {
        int hour = clockBean.getSetHour();
        int progress = (hour % 12) * 60 + clockBean.getSetMin();
        getUpCircular.setProgressToMaxProgress(progress);

        for (int i = 0; i < clockBean.period.length; i++) {
            checkBoxes[i].setChecked(clockBean.period[i]);
        }
    }

    private void goneTimePicker() {
        timePickerView.setVisibility(View.GONE);
        getUpVeil.setVisibility(View.GONE);
    }

    private void showPopupWindow() {
        if (popupTextWindow == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.popwindow_time, null);
            popupTxt = (TextView) view.findViewById(R.id.pop_time_txt);
            popupTextWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, false);
        }

        if (!popupTextWindow.isShowing()) {
            popupTextWindow.showAtLocation(getUpCircular, Gravity.NO_GRAVITY, 0, DensityUtil.dp2px(this, 50.0f));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (timePickerView != null) {
                if (timePickerView.getVisibility() == View.VISIBLE) {
                    goneTimePicker();
                    return false;
                }
            }
            SpOption.addUsedAppSp(this);
            finish(RESULT_CANCELED);
        }

        return super.onKeyDown(keyCode, event);
    }

    private void finish(int resultCode) {
        setResult(resultCode);
        finish();
    }
}
