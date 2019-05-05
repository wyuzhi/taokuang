package com.flying.baselib.utils.ui;

import java.util.concurrent.atomic.AtomicInteger;

public class IdUtils {
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    //生成唯一的view id
    public static int generateViewId() {

        for (; ; ) {
            final int result = sNextGeneratedId.get();
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

}
