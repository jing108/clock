package www.theboy.wang.clock.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import www.theboy.wang.clock.R;
import www.theboy.wang.clock.sputils.SpOption;
import www.theboy.wang.clock.common.Constant;
import www.theboy.wang.clock.dealintent.DealIntent;


public class MainActivity extends BasAct implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.color.main_color);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_ATTACHED_IN_DECOR);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        Intent intent = getIntent();
        if (!DealIntent.a(intent)) {
            SharedPreferences sp = getSharedPreferences(Constant.SP_NAME_CLOCK, MODE_PRIVATE);
            boolean usedApp = sp.getBoolean(Constant.SP_KEY_USED_APP, false);
            if (!usedApp) {
                Intent getupIntent = new Intent(this, GetupClockActivity.class);
                startActivityForResult(getupIntent, 0x2710);
            }
        }

        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 0x2710:
                if (resultCode == RESULT_FIRST_USER) {
                    if (SpOption.checkHoneyTip(this)) {

                    } else {
                        Intent intent = new Intent(this, HoneyRemind.class);
                        startActivityForResult(intent, 0x2710);
                    }
                }
                break;
        }
    }
}
