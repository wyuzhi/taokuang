package com.flying.taokuang;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;


import com.flying.baselib.utils.app.LogUtils;
import com.flying.baselib.utils.ui.ToastUtils;
import com.flying.taokuang.entity.User;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class SignUpActivity extends AppCompatActivity {
    private Button mBtnSignup;
    private TextInputEditText mEtUserNickName;
    private TextInputEditText mEtLoginID;
    private TextInputEditText mEtPassword;
    private CheckBox mCbAgree;
    private TextView mTvAgreement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.mipmap.bg_sign_up);
        setContentView(R.layout.activity_signup);
        initView();
    }

    private void initView() {
        mBtnSignup = findViewById(R.id.ljzc);
        mEtUserNickName = findViewById(R.id.yhm);
        mEtLoginID = findViewById(R.id.zh);
        mEtPassword = findViewById(R.id.mm);
        mCbAgree = findViewById(R.id.checkbox);
        mTvAgreement = findViewById(R.id.text_xieyi);

        mCbAgree.setChecked(true);
        mBtnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCbAgree.isChecked() == true) {
                    signup();
                } else {
                    ToastUtils.show("同意协议才能注册");
                }
            }
        });
        mTvAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, AgreementActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    mCbAgree.setChecked(true);
                }
        }
    }

    private void signup() {
        String nickName = mEtUserNickName.getText().toString().replaceAll(" ", "");
        String loginId = mEtLoginID.getText().toString().replaceAll(" ", "");
        String passWord = mEtPassword.getText().toString().replaceAll(" ", "");
        if (TextUtils.isEmpty(nickName) || nickName.length() < 4 || nickName.length() > 16) {
            ToastUtils.show("昵称字数小于4位或大于16位");
            return;
        }
        if (TextUtils.isEmpty(loginId) || loginId.length() < 8 || loginId.length() > 16) {
            ToastUtils.show("学号位数小于8位或大于16位");
            return;
        }
        if (TextUtils.isEmpty(passWord) || passWord.length() < 4 || passWord.length() > 16) {
            ToastUtils.show("密码位数小于4位或大于16位");
            return;
        }
        final Intent intent = new Intent(this, LoginActivity.class);
        final User user = new User();
        user.setUsername(loginId);
        user.setPassword(passWord);
        user.setNicheng(nickName);
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