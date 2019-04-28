package com.flying.baselib.utils.storage;

import android.os.Environment;

public final class SdCardUtils {

    private SdCardUtils() {
        throw new AssertionError("No instances.");
    }

    /**
     * @return 外部SD卡是否可用
     */
    public static boolean isSdCardMounted() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
}
