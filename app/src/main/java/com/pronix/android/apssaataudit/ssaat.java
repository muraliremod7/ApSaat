package com.pronix.android.apssaataudit;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;


import com.pronix.android.apssaataudit.common.Constants;

import java.io.File;

/**
 * Created by ravi on 12/29/2017.
 */

public class ssaat extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        MultiDex.install(this);
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
//                AndroidUtils.showMsg(AndroidUtils.getExceptionRootMessage(throwable), MicroXtend.this.getApplicationContext());
                System.out.println(throwable.getMessage());
                System.exit(0);
            }
        });
        setAppDirectorypath();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public void setAppDirectorypath() {
        try {
            String device = Build.DEVICE.toUpperCase();
            if (device.equals("GENERIC") || device.equals("SDK")) {
                Constants.ROOTDIRECTORYPATH = getFilesDir().getAbsolutePath();
            } else {
                File[] file = this.getExternalFilesDirs(null);
                Constants.ROOTDIRECTORYPATH = file[0].getAbsolutePath();
            }

        } catch (Exception e) {

        }
    }





}
