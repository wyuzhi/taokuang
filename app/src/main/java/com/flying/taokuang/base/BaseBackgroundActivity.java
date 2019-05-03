package com.flying.taokuang.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

public abstract class BaseBackgroundActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(null);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(getContentViewResId());
        FrameLayout root = findViewById(android.R.id.content);
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(getBackgroundResId());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        root.addView(imageView, 0, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public abstract int getContentViewResId();

    public abstract int getBackgroundResId();
}
