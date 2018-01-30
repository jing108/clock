package www.theboy.wang.clock.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by wands_wang on 2017/9/21.
 */

public abstract class BaseActivity extends FragmentActivity {

    protected abstract Activity getActivity();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getActivity().getWindow().addFlags(0x4000000);
            View view = LayoutInflater.from(getActivity()).inflate(layoutResID, null);
            int identifier = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (identifier > 0) {
                int size = getResources().getDimensionPixelSize(identifier);
                view.setPadding(0, size, 0, 0);
            }
            super.setContentView(view);
        }

        super.setContentView(layoutResID);

    }
}
