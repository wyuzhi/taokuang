package com.flying.taokuang;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.flying.baselib.utils.app.LogUtils;
import com.flying.baselib.utils.device.NetworkUtils;
import com.flying.baselib.utils.ui.ToastUtils;
import com.flying.taokuang.base.BaseBackgroundActivity;
import com.flying.taokuang.entity.User;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class SignUpActivity extends BaseBackgroundActivity {
    private Button mBtnSignup;
    private TextInputEditText mEtUserNickName;
    private TextInputEditText mEtLoginID;
    private TextInputEditText mEtPassword;
    private TextInputEditText mEtInviter;
    private CheckBox mCbAgree;
    private TextView mTvAgreement;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    public int getContentViewResId() {
        return R.layout.activity_signup;
    }

    @Override
    public int getBackgroundResId() {
        return R.mipmap.bg_sign_up;
    }

    private void initView() {
        mBtnSignup = findViewById(R.id.ljzc);
        mEtUserNickName = findViewById(R.id.yhm);
        mEtLoginID = findViewById(R.id.zh);
        mEtPassword = findViewById(R.id.mm);
        mEtInviter = findViewById(R.id.inviter);
        mCbAgree = findViewById(R.id.checkbox);
        mTvAgreement = findViewById(R.id.text_xieyi);

        mCbAgree.setChecked(true);
        mBtnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCbAgree.isChecked() == true) {
                    signup();
                } else {
                    ToastUtils.show(getBaseContext().getResources().getString(R.string.signup_agree_tips));
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
        String inviter = mEtInviter.getText().toString().replaceAll(" ", "");
        String nickName = mEtUserNickName.getText().toString().replaceAll(" ", "");
        String loginId = mEtLoginID.getText().toString().replaceAll(" ", "");
        String passWord = mEtPassword.getText().toString().replaceAll(" ", "");
        if (TextUtils.isEmpty(nickName) || nickName.length() < 4 || nickName.length() > 16) {
            ToastUtils.show(getBaseContext().getResources().getString(R.string.signup_nickname_fail_tips));
            return;
        }
        if (TextUtils.isEmpty(loginId) || loginId.length() < 8 || loginId.length() > 16) {
            ToastUtils.show(getBaseContext().getResources().getString(R.string.signup_loginid_fail_tips));
            return;
        }
        if (TextUtils.isEmpty(passWord) || passWord.length() < 4 || passWord.length() > 16) {
            ToastUtils.show(getBaseContext().getResources().getString(R.string.signup_password_fail_tips));
            return;
        }
        if (!NetworkUtils.isNetworkConnected(getApplicationContext())) {
            ToastUtils.show(getBaseContext().getResources().getString(R.string.signup_network_fail));
        }

        final User user = new User();
        user.setUsername(loginId);
        user.setInviter(inviter);
        user.setPassword(passWord);
        user.setNicheng(nickName);
        user.setRenz(false);
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    ToastUtils.show(getBaseContext().getResources().getString(R.string.signup_success_tips));
                    LogUtils.d("注册", "注册成功");
                    finish();
                } else {
                    LogUtils.d("注册", "注册失败:" + e);
                    ToastUtils.show(getBaseContext().getResources().getString(R.string.signup_fail_tips));
                }
            }
        });
    }
}