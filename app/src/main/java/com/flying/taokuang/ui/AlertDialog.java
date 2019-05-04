package com.flying.taokuang.ui;

import android.app.Activity;
import android.app.Dialog;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.flying.baselib.utils.ui.UiUtils;
import com.flying.taokuang.R;

public class AlertDialog extends Dialog {

    private View.OnClickListener mConfirmOnClickListener;
    private View.OnClickListener mCancelOnClickListener;
    private TextView mTvTitle;
    private Button mBtnConfirm;
    private Button mBtnCancel;

    public AlertDialog(Activity context) {
        super(context, R.style.Dialog);
        setContentView(R.layout.purchase_dialog_layout);
        mTvTitle = findViewById(R.id.tv_title);
        mBtnConfirm = findViewById(R.id.btn_purchase_confirm);
        mBtnCancel = findViewById(R.id.btn_purchase_cancle);
        UiUtils.expandClickRegion(mBtnConfirm, UiUtils.dp2px(10));
        UiUtils.expandClickRegion(mBtnCancel, UiUtils.dp2px(10));
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mConfirmOnClickListener == null) {
                    return;
                }
                mConfirmOnClickListener.onClick(v);
            }
        });
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.this.dismiss();
                if (mCancelOnClickListener == null) {
                    return;
                }
                mCancelOnClickListener.onClick(v);
            }
        });
    }


    public void setTitle(String title) {
        if (mTvTitle != null && !TextUtils.isEmpty(title)) {
            mTvTitle.setText(title);
        }
    }

    public void setTitle(@StringRes int resId) {
        if (mTvTitle != null) {
            mTvTitle.setText(resId);
        }
    }

    public void setConfirmButton(@StringRes int resId, View.OnClickListener confirmOnClickListener) {
        if (mBtnConfirm != null) {
            mBtnConfirm.setText(resId);
        }
        this.mConfirmOnClickListener = confirmOnClickListener;
    }

    public void setCancelButton(@StringRes int resId, View.OnClickListener cancelOnClickListener) {
        if (mBtnCancel != null) {
            mBtnCancel.setText(resId);
        }
        this.mCancelOnClickListener = cancelOnClickListener;
    }

    public void setConfirmButton(String text, View.OnClickListener confirmOnClickListener) {
        if (mBtnConfirm != null && !TextUtils.isEmpty(text)) {
            mBtnConfirm.setText(text);
        }
        this.mConfirmOnClickListener = confirmOnClickListener;
    }

    public void setCancelButton(String text, View.OnClickListener cancelOnClickListener) {
        if (mBtnCancel != null && !TextUtils.isEmpty(text)) {
            mBtnCancel.setText(text);
        }
        this.mCancelOnClickListener = cancelOnClickListener;
    }

}
