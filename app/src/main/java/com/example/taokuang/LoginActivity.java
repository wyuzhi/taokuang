package com.example.taokuang;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.taokuang.entity.User;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private String dlyhm;
    private String dlmm;
    private EditText yhm;
    private EditText mm;
    private Button dl;
    private Button zc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Bmob.initialize(this,"7c28cec5766e668a48a5ea7d719d8e08");
        initView();
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
        dlyhm= String.valueOf(yhm.getText());
        dlmm= String.valueOf(mm.getText());
        final User dluser=new User();
        dluser.setUsername(dlyhm);
        dluser.setPassword(dlmm);
        dluser.login(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent2);
                } else {
                    Toast.makeText(LoginActivity.this, "用户名或密码错误",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        if(BmobUser.isLogin()){
            User user = BmobUser.getCurrentUser(User.class);
            Snackbar.make(v,"当前用户:"+user.getUsername(),Snackbar.LENGTH_LONG).show();
        }
        else{
            Snackbar.make(v,"请登陆:",Snackbar.LENGTH_LONG).show();
        }
    }
}

