package com.example.taokuang;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.taokuang.entity.User;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.TResult;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class RengzActivity extends TakePhotoActivity {
    private Button sc;
    private EditText xh;
    private ImageView xsz;
    private File xyk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rengz);
        initView();
    }

    private void initView() {

        xh = findViewById(R.id.rz_xh);
        final String xha=String.valueOf(xh.getText());
        xsz = findViewById(R.id.rz_xsz);
        xsz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Choose();
            }
        });
        sc =findViewById(R.id.rz_sc);
        sc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!xha.equals("")){
                    String path = xyk.getPath();
                    final BmobFile xsza=new BmobFile(xyk);
                    xsza.uploadblock(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                User user = BmobUser.getCurrentUser(User.class);
                                user.setXh(xha);
                                user.setXsz(xsza);
                                user.update(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e==null){
                                            Toast.makeText(RengzActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                                            Intent aaa = new Intent(RengzActivity.this, MainActivity.class);
                                            startActivity(aaa);
                                        }
                                        else Toast.makeText(RengzActivity.this, "上传失败"+e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else Toast.makeText(RengzActivity.this, "上传失败"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                    Toast.makeText(RengzActivity.this, "请填写完整",
                            Toast.LENGTH_LONG).show();
            }
        });
    }

    private void Choose() {
        TakePhoto takePhoto = getTakePhoto();
        configCompress(takePhoto);
        takePhoto.onPickFromGallery();
    }
    @Override
    public void takeSuccess(TResult result) {
        xyk = new File(result.getImages().get(0).getCompressPath());
        super.takeSuccess(result);
        Glide.with(this).load(new File(result.getImages().get(0).getCompressPath())).into(xsz);
    }
    private void configCompress(TakePhoto takePhoto) {//压缩配置
        int maxSize = Integer.parseInt("409600");//最大 压缩
        int width = Integer.parseInt("800");//宽
        int height = Integer.parseInt("800");//高
        CompressConfig config;
        config = new CompressConfig.Builder().setMaxSize(maxSize)
                .setMaxPixel(width >= height ? width : height)
                .enableReserveRaw(false)//拍照压缩后是否显示原图
                .create();
        takePhoto.onEnableCompress(config, false);//是否显示进度条
    }

}
