package www.theboy.wang.clock.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import www.theboy.wang.clock.R;

/**
 * Created by wands_wang on 2017/9/20.
 */

public class BasAct extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bas);
    }
}
