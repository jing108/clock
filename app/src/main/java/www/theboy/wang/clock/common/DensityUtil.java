package www.theboy.wang.clock.common;

import android.content.Context;

/**
 * Created by wands_wang on 2017/9/26.
 */

public class DensityUtil {

    public static int dp2px(Context context, float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5);
    }
}
