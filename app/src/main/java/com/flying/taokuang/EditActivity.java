package com.flying.taokuang;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.flying.baselib.utils.app.LogUtils;
import com.flying.taokuang.Adapter.ImgAdapter;
import com.flying.taokuang.entity.TaoKuang;
import com.flying.taokuang.entity.User;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class EditActivity extends Activity implements View.OnClickListener {
    private static final int MIN_CLICK_DELAY_TIME = 10000;
    private static long lastClickTime;

    private static final int REQUEST_CODE_CHOOSE = 99;
    private int s = 4;


    private String id;
    private List<String> iList;
    private List<String> cList = new ArrayList<>();
    private List<String> zList = new ArrayList<>();



    private RecyclerView iRecyclerView;
    private ImgAdapter iAdapter;


    private EditText biaoti;
    private EditText miaoshu;
    private EditText weizhi;
    private EditText jiage;
    private EditText lianxi;
    private ImageView im1;
    private ImageView im2;
    private Button fabu;
    private Spinner leibie;


    private String tleibie;
    private String tbiaoti;
    private String tlianxi;
    private String tmiaoshu;

    private String tweizhi;
    private String tjiage;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fabu);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        initView();
    }

    private void initView() {

        iList = new ArrayList<>();
        im2=findViewById(R.id.expanded_image);
        im2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                im2.setVisibility(View.INVISIBLE);
            }
        });


        leibie = findViewById(R.id.spinner_leibie);
        biaoti = findViewById(R.id.edit_biaoti);
        miaoshu = findViewById(R.id.edit_miaoshu);
        weizhi = findViewById(R.id.edit_weizhi);
        jiage = findViewById(R.id.edit_jiage);
        lianxi = findViewById(R.id.edit_lianxi);
        im1 = findViewById(R.id.im_1);
        fabu = findViewById(R.id.fabu);
        im1.setOnClickListener(this);
        fabu.setOnClickListener(this);
        iRecyclerView = findViewById(R.id.image_RecyclerView);
        iRecyclerView.setLayoutManager(new GridLayoutManager
                (this, 4, GridLayoutManager.VERTICAL, false));

        //加载原有文件
        BmobQuery<TaoKuang> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(id, new QueryListener<TaoKuang>() {
            @Override
            public void done(TaoKuang taoKuang, BmobException e) {
                if (e == null) {
                    biaoti.setText(taoKuang.getBiaoti());
                    miaoshu.setText(taoKuang.getMiaoshu());
                    weizhi.setText(taoKuang.getWeizhi());
                    jiage.setText(taoKuang.getJiage());
                    lianxi.setText(taoKuang.getLianxi());

                    iList.addAll(taoKuang.getPic());
                    iAdapter=new ImgAdapter(EditActivity.this,iList);
                    iRecyclerView.setAdapter(iAdapter);
                    iAdapter.setM1(new ImgAdapter.M1() {
                        @Override
                        public void o(String url, int position) {
                            im2.setVisibility(View.VISIBLE);
                            Glide.with(EditActivity.this).load(url).into(im2);
                        }
                    });
                    s = 4 - (iAdapter.mList.size());
                    if (s <= 0) {
                        im1.setVisibility(View.INVISIBLE);
                    }
                    iAdapter.setM2(new ImgAdapter.M2() {
                        @Override
                        public void onDeliteClick(List list) {
                                s = 4 - list.size();
                                im1.setVisibility(View.VISIBLE);
                        }

                    });



                } else {

                }
            }
        });

    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.im_1:
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
        cList = iAdapter.getLocal();
        zList = iAdapter.getRemote();



        tleibie = String.valueOf(leibie.getSelectedItem());
        tlianxi = String.valueOf(lianxi.getText().toString());
        tbiaoti = String.valueOf(biaoti.getText().toString());
        tmiaoshu = String.valueOf(miaoshu.getText().toString());
        tweizhi = String.valueOf(weizhi.getText().toString());
        tjiage = String.valueOf(jiage.getText().toString());

        if (TextUtils.isEmpty(tlianxi) || TextUtils.isEmpty(tbiaoti) || TextUtils.isEmpty(tmiaoshu) || TextUtils.isEmpty(tweizhi) || TextUtils.isEmpty(tjiage)) {
            Toast.makeText(EditActivity.this, "请将信息填写完整", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tleibie == null || tleibie.equals("类别")) {
            Toast.makeText(EditActivity.this, "请选择类别", Toast.LENGTH_SHORT).show();
            return;
        }




        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#3f72af"));
        pDialog.setTitleText("正在发布");
        pDialog.setCancelable(false);
        pDialog.show();

        if (cList.size()==0){
            if (BmobUser.isLogin() && BmobUser.getCurrentUser(User.class).getRenz()) {
                TaoKuang fb = new TaoKuang();
                User user = BmobUser.getCurrentUser(User.class);
                fb.setLeibie(tleibie);
                fb.setBiaoti(tbiaoti);
                fb.setMiaoshu(tmiaoshu);
                fb.setWeizhi(tweizhi);
                fb.setLianxi(tlianxi);
                fb.setJiage(tjiage);
                fb.setJiaoyi(false);
                fb.setBuy(false);
                fb.setGengxin(1);
                fb.setPic(zList);
                fb.setFabu(user);

                fb.update(id, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            LogUtils.d("发布", "发布成功");
                            Toast.makeText(EditActivity.this, "编辑并发布成功",
                                    Toast.LENGTH_SHORT).show();
                            pDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            Intent fbcg = new Intent(EditActivity.this, MainActivity.class);
                            fbcg.putExtra("发布成功", "发布成功");
                            fbcg.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(fbcg);
                                    /*Runtime runtime = Runtime.getRuntime();
                                    try {
                                        runtime.exec("input keyevent " + KeyEvent.KEYCODE_BACK);
                                    } catch (IOException z) {
                                        z.printStackTrace();
                                    }*/
                        } else {
                            LogUtils.d("发布", "编辑并发布成功:" + e);
                            pDialog.cancel();
                            Toast.makeText(EditActivity.this, "编辑并发布成功",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //do something
            } else {
                pDialog.cancel();
                Toast.makeText(EditActivity.this, "请先登陆或认证",
                        Toast.LENGTH_LONG).show();

            }
        }
        else {

            final String[] paths = cList.toArray(new String[0]);


            BmobFile.uploadBatch(paths, new UploadBatchListener() {
                @Override
                public void onSuccess(List<BmobFile> files, List<String> urls) {


                    if (urls.size() == paths.length) {//如果数量相等，则代表文件全部上传完成
                        LogUtils.d("图片", "图片成功");
                        zList.addAll(urls);
                        //Toast.makeText(FaBuActivity.this, "图片成功",
                        //       Toast.LENGTH_SHORT).show();
                        if (BmobUser.isLogin() && BmobUser.getCurrentUser(User.class).getRenz()) {
                            TaoKuang fb = new TaoKuang();
                            User user = BmobUser.getCurrentUser(User.class);
                            fb.setLeibie(tleibie);
                            fb.setBiaoti(tbiaoti);
                            fb.setMiaoshu(tmiaoshu);
                            fb.setWeizhi(tweizhi);
                            fb.setLianxi(tlianxi);
                            fb.setJiage(tjiage);
                            fb.setJiaoyi(false);
                            fb.setBuy(false);
                            fb.setGengxin(1);
                            fb.setPic(zList);
                            fb.setFabu(user);

                            fb.update(id, new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        LogUtils.d("发布", "发布成功");
                                        Toast.makeText(EditActivity.this, "编辑并发布成功",
                                                Toast.LENGTH_SHORT).show();
                                        pDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                        Intent fbcg = new Intent(EditActivity.this, MainActivity.class);
                                        fbcg.putExtra("发布成功", "发布成功");
                                        fbcg.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(fbcg);
                                    /*Runtime runtime = Runtime.getRuntime();
                                    try {
                                        runtime.exec("input keyevent " + KeyEvent.KEYCODE_BACK);
                                    } catch (IOException z) {
                                        z.printStackTrace();
                                    }*/
                                    } else {
                                        LogUtils.d("发布", "编辑并发布成功:" + e);
                                        pDialog.cancel();
                                        Toast.makeText(EditActivity.this, "编辑并发布成功",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            //do something
                        } else {
                            pDialog.cancel();
                            Toast.makeText(EditActivity.this, "请先登陆或认证",
                                    Toast.LENGTH_LONG).show();

                        }
                    }
                }

                @Override
                public void onProgress(int i, int i1, int i2, int i3) {

                }

                @Override
                public void onError(int i, String s) {
                    LogUtils.d("图片", "图片失败" + s);
                }

            });

        }
    }


    public void Choose() {
        Matisse.from(EditActivity.this)
                .choose(MimeType.ofImage())
                .capture(true)  //是否可以拍照
                .captureStrategy(//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                        new CaptureStrategy(true, "com.example.com.flying.taokuang.fileprovider"))
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
            /*imglist = Arrays.asList(pathsx);
            List arrayList = new ArrayList(imglist);*/

            iList.addAll(Arrays.asList(pathsx));
            iAdapter = new ImgAdapter(this, iList);
            iAdapter.setOnItemClickListener(new ImgAdapter.OnItemClickListener(){
                @Override
                public void onItemClick(File file, int position) {
                    im2.setVisibility(View.VISIBLE);
                    Glide.with(EditActivity.this).load(file).into(im2);
                }


            });
            iRecyclerView.setAdapter(iAdapter);
            s = 4 - (iAdapter.mList.size());
            if (s <= 0) {
                im1.setVisibility(View.INVISIBLE);
            }

        }

    }



}