package com.flying.taokuang.debug;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.flying.baselib.utils.app.DebugSpUtils;
import com.flying.taokuang.R;

public class DebugActivity extends AppCompatActivity {
    private static final String PASSWORD = "fs2018fs";
    private Switch mSwcDokit;
    private Switch mSwcImage;
    private Switch mSwcManager;
    private Switch mSwcLog;
    private Button mBtnSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
        mSwcDokit = findViewById(R.id.swc_debug_select_dokit);
        mSwcImage = findViewById(R.id.swc_debug_select_image);
        mSwcManager = findViewById(R.id.swc_debug_select_manager);
        mSwcLog = findViewById(R.id.swc_debug_select_log);
        mBtnSet = findViewById(R.id.btn_set);
        mSwcDokit.setChecked(DebugSpUtils.isDokitEnable());
        mSwcImage.setChecked(DebugSpUtils.isImageEnable());
        mSwcManager.setChecked(DebugSpUtils.isManageEnable());
        mSwcLog.setChecked(DebugSpUtils.isLogEnable());

        mBtnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DebugSpUtils.setDokitEnable(mSwcDokit.isChecked());
                DebugSpUtils.setImageEnable(mSwcImage.isChecked());
                DebugSpUtils.setManageEnable(mSwcManager.isChecked());
                DebugSpUtils.setLogEnable(mSwcLog.isChecked());
                finish();
            }
        });

        final EditText enterDebug = new EditText(this);
        enterDebug.setSingleLine(true);
        new AlertDialog.Builder(this).setTitle("请输入密码")
                .setView(enterDebug)
                .setCancelable(false)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!PASSWORD.equals(enterDebug.getText().toString())) {
                            finish();
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .create()
                .show();
    }
}
