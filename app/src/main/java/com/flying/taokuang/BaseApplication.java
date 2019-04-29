package com.flying.taokuang;


import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.flying.baselib.utils.app.AppUtils;
import com.flying.baselib.utils.ui.ToastUtils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.pgyersdk.crash.PgyCrashManager;

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
        initImageLoader(getApplicationContext());
        initDokit();
    }

    private void initImageLoader(Context context) {
        // TODO Auto-generated method stub
        // 创建DisplayImageOptions对象
        DisplayImageOptions defaulOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true).build();
        // 创建ImageLoaderConfiguration对象
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(
                context).defaultDisplayImageOptions(defaulOptions)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        // ImageLoader对象的配置
        ImageLoader.getInstance().init(configuration);
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