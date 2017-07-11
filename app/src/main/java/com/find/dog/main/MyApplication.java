package com.find.dog.main;

import android.content.Context;
import android.util.DisplayMetrics;

public class MyApplication extends android.app.Application {

    private DisplayMetrics displayMetrics = null;
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getInstance() {
        return mContext;
    }

}
