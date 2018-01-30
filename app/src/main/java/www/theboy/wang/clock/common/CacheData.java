package www.theboy.wang.clock.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Created by wands_wang on 2017/10/23.
 */

public class CacheData {

    public static Object getListData(Context context, Type type) {
        SharedPreferences sp = context.getSharedPreferences("jason_array", Context.MODE_PRIVATE);
        String data = sp.getString("list_data", null);
        if (!TextUtils.isEmpty(data)) {
            return new Gson().fromJson(data, type);
        }

        return null;
    }
}