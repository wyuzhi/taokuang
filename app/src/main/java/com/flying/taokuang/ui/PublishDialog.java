package com.flying.taokuang.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.RelativeLayout;

import com.flying.baselib.utils.ui.UiUtils;
import com.flying.taokuang.R;

public class PublishDialog extends Dialog {
    private RelativeLayout mRoot;
    private View mSell;
    private View mBuy;
    private View mClose;

    public PublishDialog(Context context) {
        super(context, R.style.main_publishdialog_style);
        setContentView(R.layout.dialog_publish);
        init();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);
    }

    private void init() {
        mRoot = findViewById(R.id.publish_main_rlmian);
        mSell = findViewById(R.id.Publish_dialog_fabu);
        mBuy = findViewById(R.id.publish_dialog_huishou);
        mClose = findViewById(R.id.publish_dialog_iv_close);
        mRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void show() {
        super.show();
        ObjectAnimator rootAnim = ObjectAnimator.ofFloat(mRoot, "alpha", 0F, 1F).setDuration(300);
        ObjectAnimator sellAnim = ObjectAnimator.ofFloat(mSell, "translationY", UiUtils.dp2px(80), 0F).setDuration(600);
        ObjectAnimator buyAnim = ObjectAnimator.ofFloat(mBuy, "translationY", UiUtils.dp2px(80), 0F).setDuration(600);
        ObjectAnimator closeAnim = ObjectAnimator.ofFloat(mClose, "rotation", 0F, 270F).setDuration(600);
        buyAnim.setStartDelay(100);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(1000);
        set.setInterpolator(new AnticipateOvershootInterpolator());
        set.playTogether(rootAnim, closeAnim, sellAnim, buyAnim);
        set.start();
    }

    @Override
    public void dismiss() {
        ObjectAnimator rootAnim = ObjectAnimator.ofFloat(mRoot, "alpha", 1F, 0F).setDuration(200);
        ObjectAnimator sellAnim = ObjectAnimator.ofFloat(mSell, "translationY", 0F, UiUtils.dp2px(80)).setDuration(300);
        ObjectAnimator buyAnim = ObjectAnimator.ofFloat(mBuy, "translationY", 0F, UiUtils.dp2px(80)).setDuration(300);
        rootAnim.setStartDelay(200);
        buyAnim.setStartDelay(100);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(400);
        set.setInterpolator(new AnticipateOvershootInterpolator());
        set.playTogether(rootAnim, sellAnim, buyAnim);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                PublishDialog.super.dismiss();
            }
        });
        set.start();
    }


    public PublishDialog setSellClickListener(View.OnClickListener clickListener) {
        mSell.setOnClickListener(clickListener);
        return this;

    }

    public PublishDialog setBuyClickListener(View.OnClickListener clickListener) {
        mBuy.setOnClickListener(clickListener);
        return this;

    }
}
