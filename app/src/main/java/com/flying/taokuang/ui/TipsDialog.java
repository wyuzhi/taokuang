package com.flying.taokuang.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Message;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.flying.baselib.WeakHandler;
import com.flying.baselib.utils.collection.CollectionUtils;
import com.flying.taokuang.R;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

public class TipsDialog extends Dialog {

    private static final int DEFAULTT_MILLIS = 2500;
    private static WeakHashMap<WeakReference<Activity>, TipsDialog> sDialogActivityMap = new WeakHashMap<>();
    private ImageView mIvIcon;
    private TextView mTvTitle;
    private WeakHandler mHandler;
    private int mShowMillis = DEFAULTT_MILLIS;

    @SuppressLint("HandlerLeak")
    public TipsDialog(Activity context) {
        super(context, R.style.Dialog);
        setContentView(R.layout.tips_dialog_layout);
        setCanceledOnTouchOutside(true);
        mIvIcon = findViewById(R.id.dialog_img);
        mTvTitle = findViewById(R.id.dialog_title);
        mHandler = new WeakHandler(context) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                dismiss();
            }
        };
    }

    public void setTitle(@StringRes int id) {
        if (mTvTitle == null) {
            return;
        }
        mTvTitle.setText(id);
    }

    public void setTitle(String title) {
        if (mTvTitle == null || TextUtils.isEmpty(title)) {
            return;
        }
        mTvTitle.setText(title);
    }

    public void setIcon(@DrawableRes int id) {
        if (mIvIcon == null) {
            return;
        }
        mIvIcon.setImageResource(id);
    }

    public void setShowMillis(int millis) {
        mShowMillis = millis;
    }

    @Override
    public void show() {
        super.show();
        if (mHandler != null) {
            mHandler.removeMessages(0);
            mHandler.sendEmptyMessageDelayed(0, mShowMillis);
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }


    public static void show(Activity context, @StringRes int strId, @DrawableRes int imgId) {
        show(context, strId, imgId, DEFAULTT_MILLIS);
    }

    public static void show(Activity context, @StringRes int strId, @DrawableRes int imgId, int millis) {
        if (context == null) {
            return;
        }
        if (!CollectionUtils.isEmpty(sDialogActivityMap)) {
            TipsDialog dialog = null;
            for (WeakReference ref : sDialogActivityMap.keySet()) {
                if (ref.get() != null && ref.get().equals(context)) {
                    dialog = sDialogActivityMap.get(ref);
                    break;
                }
            }
            if (dialog != null) {
                dialog.setTitle(strId);
                dialog.setIcon(imgId);
                dialog.show();
                return;
            }
        }
        final TipsDialog dialog = new TipsDialog(context);
        dialog.setTitle(strId);
        dialog.setIcon(imgId);
        dialog.setShowMillis(millis);
        dialog.show();
        if (sDialogActivityMap != null) {
            sDialogActivityMap.put(new WeakReference<>(context), dialog);
        }
    }
}
