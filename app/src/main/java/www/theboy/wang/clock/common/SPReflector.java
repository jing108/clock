package www.theboy.wang.clock.common;

import android.content.SharedPreferences;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * Created by wands_wang on 2017/9/21.
 */

public class SPReflector {

    private static Method apply;

    private static Method getStringSet;

    private static Method putStringSet;

    static {
        try {
            apply = SharedPreferences.Editor.class.getMethod("apply");
            getStringSet = SharedPreferences.class.getMethod("getStringSet", String.class, Set.class);
            putStringSet = SharedPreferences.Editor.class.getMethod("putStringSet", String.class, Set.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            apply = null;
            getStringSet = null;
            putStringSet = null;
        }
    }

    public static void deal(SharedPreferences.Editor editor) {
        if (apply != null) {
            try {
                apply.invoke(editor);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                editor.commit();
            }
        }
    }
}
