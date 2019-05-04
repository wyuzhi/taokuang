package com.flying.baselib;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

public class WeakHandler extends Handler {

    private WeakReference<Activity> reference;

    public WeakHandler(Activity activity) {
        super(Looper.myLooper());
        reference = new WeakReference<>(activity);
    }

    @Override
    public void handleMessage(Message msg) {
        if (reference == null || reference.get() == null) {
            return;
        }
    }

}
