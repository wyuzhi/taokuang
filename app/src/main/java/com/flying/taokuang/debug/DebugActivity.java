package com.flying.taokuang.debug;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.flying.taokuang.R;

public class DebugActivity extends AppCompatActivity {
    private static final String PASSWORD = "fs2018fs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
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
