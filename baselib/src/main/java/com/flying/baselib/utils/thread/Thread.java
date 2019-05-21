package com.flying.baselib.utils.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class Thread implements Runnable {

    private static final ExecutorService sThreadExcutors = Executors.newFixedThreadPool(4);

    public void start() {
        if (sThreadExcutors != null) {
            sThreadExcutors.submit(this);
        }
    }
}
