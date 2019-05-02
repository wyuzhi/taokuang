package com.flying.taokuang.My;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.flying.baselib.utils.ui.UiUtils;
import com.flying.taokuang.MainActivity;
import com.flying.taokuang.R;
import com.flying.taokuang.entity.User;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class MyChangePasswordActivity extends Activity implements View.OnClickListener {
    private Button mBtConfirm;
    private EditText mEtInitialPassword;
    private EditText mEtPassword;
    private EditText mEtIdentifyPassword;
    private ImageView mIvBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(R.mipmap.bg_change_password);
        setContentView(R.layout.activity_password);
        initView();
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zl_qr:
                String initialPassword = mEtInitialPassword.toString();
                String password = mEtPassword.toString();
                String identifyPassword = mEtIdentifyPassword.toString();

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
                                //Snackbar.make(Layout, "查询成功", Snackbar.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(MyChangePasswordActivity.this, "修改失败" + e.getMessage(),
                                        Toast.LENGTH_LONG).show();

                                //Snackbar.make(view, "查询失败：" + e.getMessage(), Snackbar.LENGTH_LONG).show();
                            }
                        }
                    });
                } else Toast.makeText(MyChangePasswordActivity.this, "修改失败" + "请确认原密码及密码确认输入正确",
                        Toast.LENGTH_LONG).show();


                break;

        }
    }
}
