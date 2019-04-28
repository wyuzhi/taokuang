package com.flying.baselib.utils.app;

import android.util.Log;

public class LogUtils {
    private static boolean sIsDebug = AppUtils.sIsDebug;
    private static final String sDebugTag = "FlyingAppLog";

    public static void i(String msg) {
        i(sDebugTag, msg);
    }

    public static void d(String msg) {
        d(sDebugTag, msg);
    }

    public static void e(String msg) {
        e(sDebugTag, msg);
    }

    public static void w(String msg) {
        w(sDebugTag, msg);
    }

    public static void i(String tag, String msg) {
        if (sIsDebug) {
            Log.i(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (sIsDebug) {
            Log.d(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (sIsDebug) {
            Log.e(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (sIsDebug) {
            Log.w(tag, msg);
        }
    }

}
