package www.theboy.wang.clock.sputils;

import android.content.Context;
import android.content.SharedPreferences;

import www.theboy.wang.clock.common.Constant;
import www.theboy.wang.clock.common.SPReflector;

/**
 * Created by wands_wang on 2017/9/21.
 */

public class SpOption {
    public static boolean checkHoneyTip(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Constant.SP_NAME_CLOCK, Context.MODE_PRIVATE);
        return sp.getBoolean(Constant.SP_KEY_HONEY_TIP, false);
    }

    public static void addUsedAppSp(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Constant.SP_NAME_CLOCK, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(Constant.SP_KEY_USED_APP, true);
        SPReflector.deal(editor);
    }
}
