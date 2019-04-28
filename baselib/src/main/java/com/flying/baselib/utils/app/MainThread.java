package com.flying.baselib.utils.app;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

public final class MainThread {

    private MainThread() {
        throw new AssertionError("No instances.");
    }

    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    public static void post(@NonNull Runnable runnable) {
        if (isMainThread()) {
            runnable.run();
        } else {
            HANDLER.post(runnable);
        }
    }

    public static void postDelayed(@NonNull Runnable runnable, long delayMillis) {
        HANDLER.postDelayed(runnable, delayMillis);
    }

    public static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }
}
