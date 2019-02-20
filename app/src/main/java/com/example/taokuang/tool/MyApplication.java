package com.example.taokuang.tool;

import android.app.Application;

public class MyApplication extends Application {
    private static MyApplication mInstance;
    /**
     * 获取context
     * @return
     */
    public static MyApplication getInstance() {
        if (mInstance == null) {
            mInstance = new MyApplication();
        }
        return mInstance;
    }

}

