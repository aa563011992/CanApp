package ru.test.testjni;

import android.app.Application;

/**
 * @author wyl
 * @description:
 * @date :2020/12/31 17:24
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().initHandler();
    }
}
