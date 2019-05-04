package com.flying.taokuang;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PurchaseDialog extends Dialog {

    private View.OnClickListener mConfirmOnClickListener;
    private View.OnClickListener mCancelOnClickListener;

    public PurchaseDialog(Activity context) {
        super(context);
        setContentView(R.layout.purchase_dialog_layout);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        Button btn_confirm = findViewById(R.id.btn_purchase_confirm);
        Button btn_cancle = findViewById(R.id.btn_purchase_cancle);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mConfirmOnClickListener == null) {
                    return;
                }
                mConfirmOnClickListener.onClick(v);
            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PurchaseDialog.this.dismiss();
                if (mCancelOnClickListener == null) {
                    return;
                }
                mCancelOnClickListener.onClick(v);
            }
        });
    }

    public void setConfirmOnClickListener(View.OnClickListener confirmOnClickListener) {
        this.mConfirmOnClickListener = confirmOnClickListener;
    }

    public void setCancelOnClickListener(View.OnClickListener cancelOnClickListener) {
        this.mCancelOnClickListener = cancelOnClickListener;
    }

}
