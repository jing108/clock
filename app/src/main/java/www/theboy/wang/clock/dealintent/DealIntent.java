package www.theboy.wang.clock.dealintent;

import android.content.Intent;
import android.os.Build;

/**
 * Created by wands_wang on 2017/9/20.
 */

public class DealIntent {

    private static boolean c;

    public static String lockScreenPkg;
    public static String lockScreenInvokeCls;

    public static boolean a(Intent intent) {
        int flag = 0x100000;
        if (intent != null) {
            String action = intent.getStringExtra("start_action");
            if ("start_timepiece".equals(action)) {
                int sdk = Build.VERSION.SDK_INT;
                if (sdk < 0x15) {
                    if (sdk < 0xb) {
                        int flags = intent.getFlags();
                        flags &= flag;
                        if (flags == flag) {
                            c = false;
                            return false;
                        }
                    } else {
                        int flags = intent.getFlags();
                        flags &= 0x4000;
                        if (flags == 0x4000) {
                            c = false;
                            return false;
                        }
                    }
                } else {
                    int flags = intent.getFlags();
                    flags &= flag;
                    if (flags == flag) {
                        c = false;
                        return false;
                    }
                }

                lockScreenPkg = intent.getStringExtra("lockscreen_pkg");
                lockScreenInvokeCls = intent.getStringExtra("lockscreen_invoke_cls");
                c = true;
                return true;
            }
        }

        c = false;
        return false;
    }
}
