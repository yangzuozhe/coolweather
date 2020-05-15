package com.coolweather.android;

import android.app.Application;
import android.content.Context;

/**
 * Created by yzz on 2020/5/12.
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        context=getApplicationContext();
    }
    public static Context getContext(){
        return context;
    }
}
