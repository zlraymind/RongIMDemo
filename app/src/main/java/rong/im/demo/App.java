package rong.im.demo;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import cn.bmob.v3.Bmob;
import io.rong.imkit.RongIM;

public class App extends Application {
    private static final String TAG = "App";

    @Override
    public void onCreate() {
        super.onCreate();
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))) {
            Bmob.initialize(this, "dbfbf316ee8f47dadf0520cb64c6061d");
        }
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {
            RongIM.init(this);
        }
    }

    private static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }
}
