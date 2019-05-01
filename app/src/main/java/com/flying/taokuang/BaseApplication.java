package com.flying.taokuang;


import android.app.Application;
import android.support.multidex.MultiDexApplication;

import com.facebook.drawee.backends.pipeline.DraweeConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.flying.baselib.utils.app.AppUtils;
import com.flying.baselib.utils.ui.ToastUtils;
import com.pgyersdk.crash.PgyCrashManager;
import com.tendcloud.tenddata.TCAgent;

import org.litepal.LitePal;

import java.lang.reflect.Method;

import cn.bmob.v3.Bmob;


public class BaseApplication extends MultiDexApplication {

    /**
     * Called when the application is starting, before any activity, service, or
     * receiver objects (excluding content providers) have been created.
     * （当应用启动的时候，会在任何activity、Service或者接收器被创建之前调用，所以在这里进行ImageLoader 的配置）
     * 当前类需要在清单配置文件里面的application下进行name属性的设置。
     */
    @Override
    public void onCreate() {
        super.onCreate();
        PgyCrashManager.register();
        LitePal.initialize(this);
        Bmob.initialize(this, "7c28cec5766e668a48a5ea7d719d8e08");
        ImagePipelineConfig.Builder imagePipelineConfigBuilder = ImagePipelineConfig.newBuilder(this)
                .setResizeAndRotateEnabledForNetwork(true)
                .setDownsampleEnabled(true);
        DraweeConfig draweeConfig = DraweeConfig.newBuilder()
                .setDrawDebugOverlay(AppUtils.sIsDebug)
                .build();
        Fresco.initialize(this, imagePipelineConfigBuilder.build(), draweeConfig);
        initDokit();
        TCAgent.LOG_ON = false;
        TCAgent.setReportUncaughtExceptions(false);
        TCAgent.init(this);
    }


    private void initDokit() {
        //DoKit工具,只在debug版本使用
        if (AppUtils.sIsDebug) {
            try {
                Class<?> dokit = Class.forName("com.didichuxing.doraemonkit.DoraemonKit");
                Method install = dokit.getDeclaredMethod("install", Application.class);
                install.invoke(null, this);
            } catch (Exception e) {
                ToastUtils.show("Dokit install failed.");
            }
        }
    }
}