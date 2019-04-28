package com.flying.taokuang;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.flying.baselib.utils.app.LogUtils;
import com.flying.taokuang.entity.User;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class SignUpActivity extends AppCompatActivity {
    private Button mzc;
    private EditText myhm;
    private EditText mzh;
    private EditText mmm;
    private CheckBox checkBox;
    private TextView xieyi;
    private String yhm;
    private String mm;
    private String zh;
    private Boolean xy = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initView();
    }

    private void initView() {
        mzc = findViewById(R.id.ljzc);
        myhm = findViewById(R.id.yhm);
        mzh = findViewById(R.id.zh);
        mmm = findViewById(R.id.mm);
        checkBox = findViewById(R.id.checkbox);
        xieyi = findViewById(R.id.text_xieyi);
        mzc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(xy==true){
                    signup();
                }else {
                    Toast.makeText(SignUpActivity.this,"同意协议才能注册",Toast.LENGTH_SHORT).show();
                }
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    xy = true;
                }else xy = false;
            }
        });
        xieyi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this,XieyiActivity.class);
                startActivityForResult(intent,1);
            }
        });
    }

    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    checkBox.setChecked(true);
                }
        }
    }

    private void signup() {
        final Intent intent = new Intent(this, LoginActivity.class);
        final User user = new User();
        yhm = String.valueOf(myhm.getText());
        zh = String.valueOf(mzh.getText());
        mm = String.valueOf(mmm.getText());
        user.setUsername(zh);
        user.setPassword(mm);
        user.setNicheng(yhm);
        user.setRenz(false);
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    Toast.makeText(SignUpActivity.this, "注册成功",
                            Toast.LENGTH_SHORT).show();
                    LogUtils.d("注册", "注册成功");
                    startActivity(intent);
                } else {
                    LogUtils.d("注册", "注册失败:" + e);
                    Toast.makeText(SignUpActivity.this, "注册失败",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}