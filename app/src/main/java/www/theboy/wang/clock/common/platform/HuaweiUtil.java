package www.theboy.wang.clock.common.platform;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import www.theboy.wang.clock.activity.tip.SystemSetTipActivity;
import www.theboy.wang.clock.common.Constant;

/**
 * Created by wands_wang on 2017/9/21.
 */

public class HuaweiUtil {
    public static boolean isHuawei() {
        return "HUAWEI".equals(Build.MANUFACTURER);
    }

    public static void setAutoStart(Context context) {
        if (isHuawei()) {
            Intent intent = new Intent("huawei.intent.action.HSM_BOOTAPP_MANAGER");
//            intent.setClassName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.bootstart.BootStartActivity");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.putExtra("extra_pkgname", context.getPackageName());

            Intent i = new Intent(context, SystemSetTipActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra(Constant.INTENT_KEY_START_DATA, 0x4e1d);

            if (XiaomiUtil.hasActivity(context, intent)) {
                if (context instanceof Activity) {
                    ((Activity) context).startActivityForResult(intent, 0x2);
                    context.startActivity(i);
                }
            }
        }
    }
}
