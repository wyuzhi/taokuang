package com.flying.baselib.utils.app;

import android.app.Application;
import android.support.annotation.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class ApplicationUtils {

    private ApplicationUtils() {
        throw new AssertionError("No instances.");
    }

    private static Application sApplication;

    static {
        try {
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Method m_currentActivityThread = activityThread.getDeclaredMethod("currentActivityThread");
            Field f_mInitialApplication = activityThread.getDeclaredField("mInitialApplication");
            f_mInitialApplication.setAccessible(true);
            Object current = m_currentActivityThread.invoke(null);
            Object app = f_mInitialApplication.get(current);
            sApplication = (Application) app;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取全局 Application
     */
    public static Application getApplication() {
        return sApplication;
    }

    /**
     * 设置全局 Application
     */
    public static void setupApplication(@NonNull Application application) {
        if (application == null) {
            throw new NullPointerException("application must not be null");
        }
        sApplication = application;
    }
}
