package com.flying.taokuang.My;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.flying.baselib.utils.app.LogUtils;
import com.flying.baselib.utils.ui.UiUtils;
import com.flying.taokuang.MainActivity;
import com.flying.taokuang.R;
import com.flying.taokuang.base.BaseBackgroundActivity;
import com.flying.taokuang.entity.User;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class MyChangePasswordActivity extends BaseBackgroundActivity implements View.OnClickListener {
    private Button mBtConfirm;
    private EditText mEtInitialPassword;
    private EditText mEtPassword;
    private EditText mEtIdentifyPassword;
    private ImageView mIvBack;
    private Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    public int getContentViewResId() {
        return R.layout.activity_password;
    }

    @Override
    public int getBackgroundResId() {
        return R.mipmap.bg_change_password;
    }

    private void initView() {
        mIvBack = findViewById(R.id.img_return);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        UiUtils.setOnTouchBackground(mIvBack);
        mEtInitialPassword = findViewById(R.id.zl_ymm);
        mEtPassword = findViewById(R.id.zl_xmm);
        mEtIdentifyPassword = findViewById(R.id.zl_xmm1);
        mBtConfirm = findViewById(R.id.zl_qr);
        mBtConfirm.setOnClickListener(this);
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setFitsSystemWindows(true);
        mToolbar.setBackgroundColor(Color.TRANSPARENT);
        ViewGroup.LayoutParams layoutParams = mToolbar.getLayoutParams();
        layoutParams.height = UiUtils.dp2px(50) + UiUtils.getStatusBarHeight(this);
        mToolbar.setLayoutParams(layoutParams);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zl_qr:
                String initialPassword = String.valueOf(mEtInitialPassword.getText());
                String password = String.valueOf(mEtPassword.getText());
                String identifyPassword = String.valueOf(mEtIdentifyPassword.getText());

                if (!initialPassword.equals("") && !password.equals("") && !identifyPassword.equals("") && password.equals(identifyPassword)) {
                    BmobUser.updateCurrentUserPassword(initialPassword, password, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                User user = BmobUser.getCurrentUser(User.class);
                                user.setMima(mEtPassword.toString());
                                user.update(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                    }
                                });
                                Toast.makeText(MyChangePasswordActivity.this, "修改成功",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MyChangePasswordActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            } else {
                                Toast.makeText(MyChangePasswordActivity.this, "修改失败" + e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(MyChangePasswordActivity.this, "修改失败" + "请确认原密码及密码确认输入正确",
                            Toast.LENGTH_LONG).show();
                }

                break;
        }
    }

    @Override
    public String toString() {
        return Integer.toHexString(hashCode());
    }
}
