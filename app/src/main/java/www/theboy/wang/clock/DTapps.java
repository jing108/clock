package www.theboy.wang.clock;

import android.app.Application;
import android.os.Environment;

import java.io.File;

import www.theboy.wang.clock.common.FilePath;

/**
 * Created by wands_wang on 2017/9/20.
 */

public class DTapps extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        StringBuilder sb = new StringBuilder();
        File external = Environment.getExternalStorageDirectory();
        sb.append(external).append(File.separator).append("DtClock");

        FilePath.path = sb.toString();

        File file = new File(FilePath.path);
//        if (file.exists()) {
//
//        } else {
//            file.mkdirs();
//
//        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
