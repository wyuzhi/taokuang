package com.flying.taokuang;

import android.content.Intent;
import android.os.Bundle;

import com.flying.baselib.utils.app.MainThread;
import com.flying.taokuang.base.BaseActivity;

public class SplashActivity extends BaseActivity {
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        MainThread.postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();//结束本Activity
            }
        }, 1000);//设置执行时间
    }

    @Override
    public int getContentViewResId() {
        return R.layout.activity_splash;
    }

    @Override
    public int getBackgroundResId() {
        return R.mipmap.bg_splash_fullscreen;
    }
}

