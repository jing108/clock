package www.theboy.wang.clock.activity.tip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import www.theboy.wang.clock.R;
import www.theboy.wang.clock.common.Constant;
import www.theboy.wang.clock.common.platform.HuaweiUtil;
import www.theboy.wang.clock.common.platform.XiaomiUtil;

/**
 * Created by wands_wang on 2017/9/21.
 */

public class SystemSetTipActivity extends Activity {

    private TextView tipTxt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_system_set_tip);

        tipTxt = (TextView) findViewById(R.id.tip_txt);
        tipTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemSetTipActivity.this.finish();
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            int startData = intent.getIntExtra(Constant.INTENT_KEY_START_DATA, 0);
            if (startData == 0x4e1f) {
                if (HuaweiUtil.isHuawei()) {
                    tipTxt.setText(getString(R.string.huawei_float_window_tip_txt));
                } else {
                    String versionName = XiaomiUtil.getProp("ro.miui.ui.version.name");
                    if (versionName != null) {
                        switch (versionName) {
                            case "V5":
                                tipTxt.setText(getString(R.string.m_ui5_float_window_tip_txt));
                                break;

                            case "V6":
                            case "V7":
                            case "V8":
                            case "V9":
                                tipTxt.setText(getString(R.string.m_ui6_float_window_tip_txt));
                                break;

                            default:
                                finish();
                                break;
                        }
                    } else {
                        finish();
                    }
                }
            }

            if (startData == 0x4e1d) {
                if (HuaweiUtil.isHuawei()) {
                    tipTxt.setText(getString(R.string.huawei_start_selt_tip_txt));
                } else {
                    String versionName = XiaomiUtil.getProp("ro.miui.ui.version.name");
                    if (versionName != null) {
                        switch (versionName) {
                            case "V5":
                                tipTxt.setText(getString(R.string.m_ui5_allow_start_slef));
                                break;

                            case "V6":
                            case "V7":
                            case "V8":
                            case "V9":
                                tipTxt.setText(getString(R.string.m_ui6_allow_start_slef));
                                break;

                            default:
                                finish();
                                break;
                        }
                    } else {
                        finish();
                    }
                }
            }

            super.onCreate(savedInstanceState);
        }
        finish();
    }
}
