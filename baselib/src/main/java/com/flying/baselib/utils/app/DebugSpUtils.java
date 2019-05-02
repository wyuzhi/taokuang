package com.flying.baselib.utils.app;

import com.flying.baselib.utils.SharedPreferencesHelper;

public class DebugSpUtils {
    private static final String KEY_DOKIT = "KEY_DOKIT";
    private static final String KEY_IMAGE = "KEY_IMAGE";
    private static final String KEY_MANAGE = "KEY_MANAGE";
    private static final String KEY_LOG = "KEY_LOG";
    private static SharedPreferencesHelper sSpHelpr;

    private static void init() {
        if (sSpHelpr == null) {
            sSpHelpr = new SharedPreferencesHelper(ApplicationUtils.getApplication(), "debug_tool");
        }
    }

    public static boolean isDokitEnable() {
        if (sSpHelpr == null) {
            init();
        }
        return (boolean) sSpHelpr.get(KEY_DOKIT, false);
    }

    public static void setDokitEnable(boolean dokitEnable) {
        if (sSpHelpr == null) {
            init();
        }
        sSpHelpr.put(KEY_DOKIT, dokitEnable);
    }

    public static boolean isImageEnable() {
        if (sSpHelpr == null) {
            init();
        }
        return (boolean) sSpHelpr.get(KEY_IMAGE, false);
    }

    public static void setImageEnable(boolean imageEnable) {
        if (sSpHelpr == null) {
            init();
        }
        sSpHelpr.put(KEY_IMAGE, imageEnable);
    }

    public static boolean isManageEnable() {
        if (sSpHelpr == null) {
            init();
        }
        return (boolean) sSpHelpr.get(KEY_MANAGE, false);
    }

    public static void setManageEnable(boolean managerEnable) {
        if (sSpHelpr == null) {
            init();
        }
        sSpHelpr.put(KEY_MANAGE, managerEnable);
    }

    public static boolean isLogEnable() {
        if (sSpHelpr == null) {
            init();
        }
        return (boolean) sSpHelpr.get(KEY_LOG, false);
    }

    public static void setLogEnable(boolean managerEnable) {
        if (sSpHelpr == null) {
            init();
        }
        sSpHelpr.put(KEY_LOG, managerEnable);
    }
}
