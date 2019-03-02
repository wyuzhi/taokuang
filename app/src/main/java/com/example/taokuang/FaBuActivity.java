package com.example.taokuang;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.taokuang.entity.TaoKuang;
import com.example.taokuang.entity.User;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;


public class FaBuActivity extends Activity implements View.OnClickListener {
    public static final int CHOOSE_PHOTO = 2;
    private static final int MIN_CLICK_DELAY_TIME = 60000;
    private static long lastClickTime;

    private static final int REQUEST_CODE_CHOOSE = 99;

    private EditText biaoti;
    private EditText miaoshu;
    private EditText weizhi;
    private EditText jiage;
    private EditText lianxi;
    private ImageView im1;
    private ImageView im2;
    private ImageView im3;
    private Button fabu;
    private Spinner leibie;
    private Toolbar tbfb;

    private String tleibie;
    private String tbiaoti;
    private String tlianxi;
    private String tmiaoshu;
    private String tfabu;
    private String tweizhi;
    private String tjiage;
    private File file1;
    private File file2;
    private File file3;
    private String im1path;
    private String im3path;
    private String im2path;

    private Uri im1Uri;
  /*  private File tim1;
    private File tim2;
    private File tim3;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fabu);
        initView();
    }

    private void initView() {
        //tbfb =findViewById(R.id.toolbar_fabu);
        //toolbarfabu.setTitle("发布你的闲置好物");
        leibie = findViewById(R.id.spinner_leibie);
        biaoti = findViewById(R.id.edit_biaoti);
        miaoshu = findViewById(R.id.edit_miaoshu);
        weizhi = findViewById(R.id.edit_weizhi);
        jiage = findViewById(R.id.edit_jiage);
        lianxi = findViewById(R.id.edit_lianxi);
        im1 = findViewById(R.id.im_1);
        im2 = findViewById(R.id.im_2);
        im3 = findViewById(R.id.im_3);
        fabu = findViewById(R.id.fabu);
        im1.setOnClickListener(this);
        im2.setOnClickListener(this);
        im3.setOnClickListener(this);
        fabu.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.im_1:
                Toast.makeText(this, "添加图片", Toast.LENGTH_SHORT).show();
                Choose();
                break;

            case R.id.fabu:

                long curClickTime = System.currentTimeMillis();
                if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
                    // 超过点击间隔后再将lastClickTime重置为当前点击时间
                    lastClickTime = curClickTime;
                    Fabu();
                }
                break;
        }

    }

    private void Fabu() {
       /* BmobFile bim1 = new BmobFile(new File(im1path));
        BmobFile bim2 = new BmobFile(new File(String.valueOf(im2)));
        BmobFile bim3 = new BmobFile(new File(String.valueOf(im3)));*/
        //  fb.setPicer(bim1);
        //   fb.setPicer(bim2);
        // fb.setPicer(bim3);
        tleibie = String.valueOf(leibie.getSelectedItem());
        tlianxi = String.valueOf(lianxi.getText());
        tbiaoti = String.valueOf(biaoti.getText());
        tmiaoshu = String.valueOf(miaoshu.getText());
        tweizhi = String.valueOf(weizhi.getText());
        tjiage = String.valueOf(jiage.getText());

        //im1path = file1.getPath();
        //im2path = file2.getPath();
        //im3path = file3.getPath();

        if (!tlianxi.equals("")
                && !tbiaoti.equals("") && !tmiaoshu.equals("")
                && !tweizhi.equals("") && !tjiage.equals("")) {
            if (!tleibie.equals("类别")){

            final String[] paths = new String[3];
            paths[0] = im1path;
            paths[1] = im2path;
            paths[2] = im3path;
            final BmobFile bim1 = new BmobFile(new File(im1path));
            final BmobFile bim2 = new BmobFile(new File(im2path));
            final BmobFile bim3 = new BmobFile(new File(im3path));
            BmobFile.uploadBatch(paths, new UploadBatchListener() {
                @Override
                public void onSuccess(List<BmobFile> files, List<String> urls) {

                    if (files.size() == paths.length) {//如果数量相等，则代表文件全部上传完成
                        Log.d("图片", "图片成功");
                        Toast.makeText(FaBuActivity.this, "图片成功",
                                Toast.LENGTH_SHORT).show();
                        if (BmobUser.isLogin()) {
                            TaoKuang fb = new TaoKuang();
                            User user = BmobUser.getCurrentUser(User.class);
                            tfabu = user.getNicheng();
                            fb.setLeibie(tleibie);
                            fb.setBiaoti(tbiaoti);
                            fb.setMiaoshu(tmiaoshu);
                            fb.setWeizhi(tweizhi);
                            fb.setLianxi(tlianxi);
                            fb.setJiage(tjiage);
                            fb.setGengxin(1);
                            fb.setPicyi(files.get(0));
                            fb.setPicer(files.get(1));
                            fb.setPicsan(files.get(2));
                            fb.setFabu(user);

                            fb.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if (e == null) {
                                        Log.d("发布", "发布成功");
                                        Toast.makeText(FaBuActivity.this, "发布成功",
                                                Toast.LENGTH_SHORT).show();
                                        Intent fbcg = new Intent(FaBuActivity.this, MainActivity.class);
                                        fbcg.putExtra("发布成功", "发布成功");
                                        startActivity(fbcg);
                                    /*Runtime runtime = Runtime.getRuntime();
                                    try {
                                        runtime.exec("input keyevent " + KeyEvent.KEYCODE_BACK);
                                    } catch (IOException z) {
                                        z.printStackTrace();
                                    }*/

                                    } else {
                                        Log.d("发布", "发布失败:" + e);
                                        Toast.makeText(FaBuActivity.this, "发布失败",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            //do something
                        } else {
                            Toast.makeText(FaBuActivity.this, "请先登陆",
                                    Toast.LENGTH_LONG).show();

                        }
                    }
                }

                @Override
                public void onProgress(int i, int i1, int i2, int i3) {

                }

                @Override
                public void onError(int i, String s) {
                    Log.d("图片", "图片失败" + s);
                }

            /*@Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.d("图片", "图片成功");
                    Toast.makeText(FaBuActivity.this, "图片成功",
                            Toast.LENGTH_SHORT).show();
                    Fabu2();

                } else {
                    Log.d("图片", "图片发布失败:" + e);
                    Toast.makeText(FaBuActivity.this, "图片发布失败",
                            Toast.LENGTH_SHORT).show();
                }
            }*/
            });
        }else Toast.makeText(FaBuActivity.this, "请选择类别",
                Toast.LENGTH_SHORT).show();
        } else Toast.makeText(FaBuActivity.this, "请将信息填写完整",
                Toast.LENGTH_SHORT).show();

    }

  /*private void Fabu2() {
        tbiaoti = String.valueOf(biaoti.getText());
        tmiaoshu = String.valueOf(miaoshu.getText());
        tweizhi = String.valueOf(weizhi.getText());
        tjiage = String.valueOf(jiage.getText());

        TaoKuang fb = new TaoKuang();
        fb.setBiaoti(tbiaoti);
        fb.setMiaoshu(tmiaoshu);
        fb.setWeizhi(tweizhi);
        fb.setJiage(tjiage);
        fb.setPicyi(bim1);
                    fb.setPicyi(bim2);
                    fb.setPicyi(bim3);
        fb.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Log.d("发布", "发布成功");
                    Toast.makeText(FaBuActivity.this, "发布成功",
                            Toast.LENGTH_SHORT).show();
                    Intent iet = new Intent(FaBuActivity.this, TaoFragment.class);
                    iet.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(iet);
                } else {
                    Log.d("发布", "发布失败:" + e);
                    Toast.makeText(FaBuActivity.this, "发布失败",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }***/

    public void Choose() {
        Matisse.from(FaBuActivity.this)
                .choose(MimeType.ofImage())
                .countable(true)
                .maxSelectable(3)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            String[] pathsx = Matisse.obtainPathResult(data).toArray(new String[0]);
            if (pathsx.length == 1) {
                im1path = Matisse.obtainPathResult(data).get(0);
                Glide.with(this).load(new File(im1path)).into(im1);
            }
            if (pathsx.length == 2) {
                im1path = Matisse.obtainPathResult(data).get(0);
                Glide.with(this).load(new File(im1path)).into(im1);
                im2path = Matisse.obtainPathResult(data).get(1);
                Glide.with(this).load(new File(im2path)).into(im2);
                im2.setVisibility(View.VISIBLE);
            }
            if (pathsx.length == 3) {
                im1path = Matisse.obtainPathResult(data).get(0);
                Glide.with(this).load(new File(im1path)).into(im1);
                im2path = Matisse.obtainPathResult(data).get(1);
                Glide.with(this).load(new File(im2path)).into(im2);
                im2.setVisibility(View.VISIBLE);
                im3path = Matisse.obtainPathResult(data).get(2);
                Glide.with(this).load(new File(im3path)).into(im3);
                im3.setVisibility(View.VISIBLE);
                // List<Uri> result = Matisse.obtainResult(data);
            }
        }

    }
    /*@Override
    public void takeSuccess(TResult result) {//第一种方法 成功
        im1path = result.getImages().get(0).getCompressPath();
        im2path = result.getImages().get(1).getCompressPath();
        im1path = result.getImages().get(3).getCompressPath();
        super.takeSuccess(result);


        file1 = new File(result.getImages().get(0).getCompressPath());
        file2 = new File(result.getImages().get(1).getCompressPath());
        file3 = new File(result.getImages().get(2).getCompressPath());
        Glide.with(this).load(
                file1)
                .into(im1);
        Glide.with(this).load(new File(result.getImages().get(1).getCompressPath())).into(im2);
        Glide.with(this).load(new File(result.getImages().get(2).getCompressPath())).into(im3);
        //成功选择图片之后用glide加载到imgView中

        biaoti.setText(im1path);
        miaoshu.setText(im3path);
        weizhi.setText(im2path);
    }


    @Override
    public void takeFail(TResult result, String msg) {//第二种 失败
        super.takeFail(result, msg);
    }

    @Override
    public void takeCancel() {//第三种退出 主要看第一种成功
        super.takeCancel();
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
    }*/


}
