package www.theboy.wang.clock.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import www.theboy.wang.clock.R;
import www.theboy.wang.clock.activity.tip.SystemSetTipActivity;
import www.theboy.wang.clock.common.Constant;
import www.theboy.wang.clock.common.SPReflector;
import www.theboy.wang.clock.common.platform.HuaweiUtil;
import www.theboy.wang.clock.common.platform.XiaomiUtil;

/**
 * Created by wands_wang on 2017/9/21.
 */

public class HoneyRemind extends BaseActivity implements View.OnClickListener {

    private boolean isXiaomi;
    private boolean isHuawei;

    private TextView allowTxt;
    private TextView mUserTxt;
    private TextView gotoUserHelpTxt;
    private Button gotoSetBtn;

    @Override
    protected Activity getActivity() {
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goto_user_help:
                finish();
                break;

            case R.id.go_to_set:
                if (isXiaomi) {
                    XiaomiUtil.setAutoStart(this);
                    //打开 systemsettip页面
                    Intent intent = new Intent(this, SystemSetTipActivity.class);
                    intent.putExtra(Constant.INTENT_KEY_START_DATA, 0x4e1d);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

                if (isHuawei) {
                    HuaweiUtil.setAutoStart(this);
                }

                finish();
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_honey);

        SharedPreferences.Editor editor = getSharedPreferences(Constant.SP_NAME_CLOCK, MODE_PRIVATE).edit();
        editor.putBoolean(Constant.SP_KEY_HONEY_TIP, true);
        SPReflector.deal(editor);
        isXiaomi = XiaomiUtil.isXiaomi();
        isHuawei = HuaweiUtil.isHuawei();

        allowTxt = (TextView) findViewById(R.id.allow);
        mUserTxt = (TextView) findViewById(R.id.m_user);
        gotoUserHelpTxt = (TextView) findViewById(R.id.goto_user_help);
        gotoUserHelpTxt.setOnClickListener(this);
        gotoSetBtn = (Button) findViewById(R.id.go_to_set);
        gotoSetBtn.setOnClickListener(this);

        if (isHuawei) {
            mUserTxt.setText(getString(R.string.remind_huawei));
            gotoSetBtn.setText(getString(R.string.huawei_go_to_set));
        } else if (isXiaomi) {
            mUserTxt.setText(getString(R.string.remind_miui));
            gotoSetBtn.setText(getString(R.string.mui_go_to_set));
        } else {
            gotoSetBtn.setText(getString(R.string.do_not_to_set));
            mUserTxt.setVisibility(View.GONE);
            gotoUserHelpTxt.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
