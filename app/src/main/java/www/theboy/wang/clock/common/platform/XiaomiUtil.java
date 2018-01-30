package www.theboy.wang.clock.common.platform;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by wands_wang on 2017/9/21.
 */

public class XiaomiUtil {
    public static boolean isXiaomi() {
        return "Xiaomi".equals(Build.MANUFACTURER);
    }

    public static void setAutoStart(Context context) {
        String prop = getProp("ro.miui.ui.version.name");
        PackageManager manager = context.getPackageManager();
        String pkgName = context.getPackageName();
        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            PackageInfo packageInfo = manager.getPackageInfo(pkgName, 0);
            if (prop != null) {
                switch (prop) {
                    case "V5":
                        intent.setClassName("com.android.settings", "com.miui.securitycenter.permission.AppPermissionsEditor");
                        intent.putExtra("extra_package_uid", packageInfo.applicationInfo.uid);
                        intent.setData(Uri.fromParts("package", pkgName, null));
                        break;

                    default: //V6,V7,V8,V9
                        intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity");
                        intent.putExtra("extra_pkgname", pkgName);
                        break;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (hasActivity(context, intent)) {
            if (context instanceof Activity) {
                context.startActivity(intent);
            }
        }
    }

    public static boolean hasActivity(Context context, Intent intent) {
        PackageManager manager = context.getPackageManager();
        return manager.queryIntentActivities(intent, PackageManager.MATCH_ALL).size() > 0;

    }

    public static String getProp(String key) {
        StringBuilder sb = new StringBuilder("getprop ");
        String result = "";
        sb.append(key);
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(Runtime.getRuntime().exec(sb.toString()).getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader, 0x400);
            result = bufferedReader.readLine();
            bufferedReader.close();
            inputStreamReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
