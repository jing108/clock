package www.theboy.wang.clock.common.log;

import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import www.theboy.wang.clock.sputils.SPValue;

/**
 * Created by wands_wang on 2017/10/19.
 */

public class Logger {
    public static void log(String log) {
        if (SPValue.isLog) {
            StringBuilder stringBuffer = new StringBuilder();
            StringBuilder logSb = new StringBuilder();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = new Date(Long.parseLong(String.valueOf(System.currentTimeMillis() / 1000)) * 1000);

            logSb.append(dateFormat.format(date));
            logSb.append("     ");
            logSb.append(log);
            logSb.append("\n");

            stringBuffer.append(logSb.toString());

            if ("mounted".equals(Environment.getExternalStorageState())) {
                StringBuilder path = new StringBuilder();
                path.append(Environment.getExternalStorageDirectory());
                path.append(File.separator);
                path.append("clock_local_log");
                File file = new File(path.toString());

                if (!file.exists()) {
                    file.mkdirs();
                }

                StringBuilder sb2 = new StringBuilder();
                sb2.append(path.toString());
                sb2.append("/ring.txt");

                try {

                    FileWriter fileWriter = new FileWriter(sb2.toString(), true);
                    fileWriter.write(stringBuffer.toString());
                    fileWriter.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
