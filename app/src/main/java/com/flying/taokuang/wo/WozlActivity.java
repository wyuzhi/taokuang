package com.flying.taokuang.wo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.flying.taokuang.MainActivity;
import com.flying.taokuang.R;
import com.flying.taokuang.entity.User;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class WozlActivity extends Activity implements View.OnClickListener {
    private Button zlqr;
    private EditText zlymm;
    private EditText zlxmm;
    private EditText zlxmm1;
    private String ymm;
    private String xmm;
    private String xmm1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_wozl);
        initView();
    }

    private void initView() {
        ImageView img=findViewById(R.id.img_return);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        zlymm =findViewById(R.id.zl_ymm);
        zlxmm=findViewById(R.id.zl_xmm);
        zlxmm1=findViewById(R.id.zl_xmm1);
        zlqr=findViewById(R.id.zl_qr);
        zlqr.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zl_qr:
                ymm = String.valueOf(zlymm.getText());
                xmm = String.valueOf(zlxmm.getText());
                xmm1 = String.valueOf(zlxmm1.getText());

                if (!ymm.equals("")&&!xmm.equals("")&&!xmm1.equals("")&&xmm.equals(xmm1)){
                BmobUser.updateCurrentUserPassword(ymm, xmm, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            User user = BmobUser.getCurrentUser(User.class);
                            user.setMima(xmm);
                            user.update(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                }
                            });
                            Toast.makeText(WozlActivity.this, "修改成功",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(WozlActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            //Snackbar.make(Layout, "查询成功", Snackbar.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(WozlActivity.this, "修改失败" + e.getMessage(),
                                    Toast.LENGTH_LONG).show();

                            //Snackbar.make(view, "查询失败：" + e.getMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    }
                });}
                else Toast.makeText(WozlActivity.this, "修改失败"+"请确认原密码及密码确认输入正确",
                        Toast.LENGTH_LONG).show();



                break;

        }
    }
}
