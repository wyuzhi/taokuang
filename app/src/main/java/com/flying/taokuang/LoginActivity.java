package com.flying.taokuang;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.flying.baselib.utils.ui.ToastUtils;
import com.flying.taokuang.base.BaseBackgroundActivity;
import com.flying.taokuang.entity.User;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.jpush.android.api.JPushInterface;

public class LoginActivity extends BaseBackgroundActivity implements View.OnClickListener {
    private String dlyhm;
    private String dlmm;
    private EditText yhm;
    private EditText mm;
    private Button dl;
    private Button zc;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    public int getContentViewResId() {
        return R.layout.activity_login;
    }

    @Override
    public int getBackgroundResId() {
        return -1;
    }

    private void initView() {
        dl = findViewById(R.id.dl_dl);
        dl.setOnClickListener(this);
        zc = findViewById(R.id.dl_zc);
        zc.setOnClickListener(this);
        yhm = findViewById(R.id.dl_yhm);
        mm = findViewById(R.id.dl_mm);
    }

    @Override
    public void onClick(View v) {
        Intent intent1 = new Intent(this, SignUpActivity.class);


        switch (v.getId()) {
            case R.id.dl_zc:
                startActivity(intent1);
                break;

            case R.id.dl_dl:
                loginb(v);
                break;
            default:
                break;
        }
    }

    private void loginb(View v) {

        final Intent intent2 = new Intent(LoginActivity.this, MainActivity.class);
        dlyhm = String.valueOf(yhm.getText());
        dlmm = String.valueOf(mm.getText());
        final User dluser = new User();
        dluser.setUsername(dlyhm);
        dluser.setPassword(dlmm);
        dluser.login(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    user.setMima(dlmm);
                    user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            JPushInterface.setAlias(LoginActivity.this,666,dlyhm);


                        }
                    });
                    intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent2);
                    LoginActivity.this.finish();
                } else {
                    ToastUtils.show("用户名或密码错误");
                }
            }
        });
    }
}

