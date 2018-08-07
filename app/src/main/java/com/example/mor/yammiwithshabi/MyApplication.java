package com.example.mor.yammiwithshabi;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class MyApplication extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        Log.d("TAG", "MyApplication.onCreate: start");
        super.onCreate();
        context = getApplicationContext();
        Log.d("TAG", "MyApplication.onCreate: end, context = " + context);
    }
}
