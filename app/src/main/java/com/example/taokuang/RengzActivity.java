package com.example.taokuang;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class RengzActivity extends TakePhotoActivity {
    private Button sc;
    private EditText xh;
    private EditText sjh;
    private ImageView xsz;
    private File xyk;
    private File xykb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rengz);
        initView();
    }

    private void initView() {

        xh = findViewById(R.id.rz_xh);
        sjh = findViewById(R.id.rz_sjh);

        xsz = findViewById(R.id.rz_xsz);
        xsz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Choose();
            }
        });
        sc = findViewById(R.id.rz_sc);
        sc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String xha = String.valueOf(xh.getText());
                final String sjha = String.valueOf(sjh.getText());
                if (!xha.equals("")&&!sjha.equals("")) {
                    String path = xyk.getPath();
                    final BmobFile xsza = new BmobFile(xyk);
                    xsza.uploadblock(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                User user = BmobUser.getCurrentUser(User.class);
                                user.setMobilePhoneNumber(sjha);
                                user.setXh(xha);
                                user.setXsz(xsza);
                                user.setRenz(true);
                                user.update(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null) {
                                            Toast.makeText(RengzActivity.this, "上传成功,将在24小时内审核完成，审核期不影响应用使用，如无法发布请尝试注销重登", Toast.LENGTH_LONG).show();
                                            Intent aaa = new Intent(RengzActivity.this, MainActivity.class);
                                            aaa.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(aaa);
                                        } else
                                            Toast.makeText(RengzActivity.this, "上传失败" + e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else
                                Toast.makeText(RengzActivity.this, "上传失败" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else
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
        xykb = new File(result.getImages().get(0).getCompressPath());
        super.takeSuccess(result);
        Luban.with(this)
                .load(xykb)
                .ignoreBy(100)
                //.setTargetDir(file_pathss)
                .filter(new CompressionPredicate() {
                    @Override
                    public boolean apply(String path) {
                        return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                    }
                })
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                    }

                    @Override
                    public void onSuccess(File file) {
                        xyk=file;
                        // TODO 压缩成功后调用，返回压缩后的图片文件
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                    }
                }).launch();

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
