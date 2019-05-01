package com.flying.taokuang;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.flying.baselib.utils.app.LogUtils;
import com.flying.baselib.utils.collection.CollectionUtils;
import com.flying.baselib.utils.ui.UiUtils;
import com.flying.taokuang.Adapter.UploadImgAdapter;
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

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class FaBuActivity extends Activity implements View.OnClickListener {
    public static final int CHOOSE_PHOTO = 2;
    private static final int MIN_CLICK_DELAY_TIME = 10000;
    private static long lastClickTime;
    private int s = 4;

    private static final int REQUEST_CODE_CHOOSE = 99;


    private List<String> imglist;
    private List<String> iList;
    private List<String> zList = new ArrayList<>();
    private List<String> lubanList = new ArrayList<>();


    private RecyclerView iRecyclerView;
    private UploadImgAdapter iAdapter;


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
    private File file1b;
    private File file2b;
    private File file3b;
    private String im1path;
    private String im3path;
    private String im2path;
    private String im1pathb;
    private String im3pathb;
    private String im2pathb;
    private ImageView mIvBack;

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
        mIvBack = findViewById(R.id.image_fabu_return);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        UiUtils.setOnTouchBackground(mIvBack);
        iList = new ArrayList<String>();
        im2 = findViewById(R.id.expanded_image);
        im2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                im2.setVisibility(View.INVISIBLE);
            }
        });


        //tbfb =findViewById(R.id.toolbar_fabu);
        //toolbarfabu.setTitle("发布你的闲置好物");
        leibie = findViewById(R.id.spinner_leibie);
        biaoti = findViewById(R.id.edit_biaoti);
        miaoshu = findViewById(R.id.edit_miaoshu);
        weizhi = findViewById(R.id.edit_weizhi);
        jiage = findViewById(R.id.edit_jiage);
        lianxi = findViewById(R.id.edit_lianxi);
        im1 = findViewById(R.id.im_1);
        //im2 = findViewById(R.id.im_2);
        //im3 = findViewById(R.id.im_3);
        fabu = findViewById(R.id.fabu);
        im1.setOnClickListener(this);
        //im2.setOnClickListener(this);
        //im3.setOnClickListener(this);
        fabu.setOnClickListener(this);
        /*imggrid = findViewById(R.id.grid_view);
        imggrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Choose();
            }
        });*/
        iRecyclerView = findViewById(R.id.image_RecyclerView);
        iRecyclerView.setLayoutManager(new GridLayoutManager
                (this, 4, GridLayoutManager.VERTICAL, false));

//        if (s == 100) {
//            iAdapter.setM2(new UploadImgAdapter.M2() {
//                @Override
//                public void onDeliteClick(List list) {
//                    s = 3 - list.size();
//                    im1.setVisibility(View.VISIBLE);
//                }
//            });
//        }

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
        if (CollectionUtils.isEmpty(iList)) {
            Toast.makeText(FaBuActivity.this, "请至少添加一张图片", Toast.LENGTH_SHORT).show();
            return;
        }
        zList = iAdapter.getImage();


        tleibie = String.valueOf(leibie.getSelectedItem());
        tlianxi = String.valueOf(lianxi.getText().toString());
        tbiaoti = String.valueOf(biaoti.getText().toString());
        tmiaoshu = String.valueOf(miaoshu.getText().toString());
        tweizhi = String.valueOf(weizhi.getText().toString());
        tjiage = String.valueOf(jiage.getText().toString());

        if (TextUtils.isEmpty(tlianxi) || TextUtils.isEmpty(tbiaoti) || TextUtils.isEmpty(tmiaoshu) || TextUtils.isEmpty(tweizhi) || TextUtils.isEmpty(tjiage)) {
            Toast.makeText(FaBuActivity.this, "请将信息填写完整", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tleibie == null || tleibie.equals("类别")) {
            Toast.makeText(FaBuActivity.this, "请选择类别", Toast.LENGTH_SHORT).show();
            return;
        }




        /*im1pathb = file1b.getPath();
        im2pathb = file2b.getPath();
        im3pathb = file3b.getPath();*/

        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#3f72af"));
        pDialog.setTitleText("正在发布");
        pDialog.setCancelable(false);
        pDialog.show();


        final String[] paths = zList.toArray(new String[0]);

        /*paths[0] = im1pathb;
        paths[1] = im2pathb;
        paths[2] = im3pathb;
        final BmobFile bim1 = new BmobFile(new File(im1pathb));
        final BmobFile bim2 = new BmobFile(new File(im2pathb));
        final BmobFile bim3 = new BmobFile(new File(im3pathb));*/
        BmobFile.uploadBatch(paths, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> files, List<String> urls) {


                if (urls.size() == paths.length) {//如果数量相等，则代表文件全部上传完成
                    LogUtils.d("图片", "图片成功");
                    //Toast.makeText(FaBuActivity.this, "图片成功",
                    //       Toast.LENGTH_SHORT).show();
                    if (BmobUser.isLogin() && BmobUser.getCurrentUser(User.class).getRenz()) {
                        TaoKuang fb = new TaoKuang();
                        User user = BmobUser.getCurrentUser(User.class);
                        tfabu = user.getNicheng();
                        fb.setLeibie(tleibie);
                        fb.setBiaoti(tbiaoti);
                        fb.setMiaoshu(tmiaoshu);
                        fb.setWeizhi(tweizhi);
                        fb.setLianxi(tlianxi);
                        fb.setJiage(tjiage);
                        fb.setJiaoyi(false);
                        fb.setBuy(false);
                        fb.setGengxin(1);
                        fb.setPic(urls);
                        fb.setFabu(user);

                        fb.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {
                                    LogUtils.d("发布", "发布成功");
                                    Toast.makeText(FaBuActivity.this, "发布成功",
                                            Toast.LENGTH_SHORT).show();
                                    pDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                    Intent fbcg = new Intent(FaBuActivity.this, MainActivity.class);
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
                                    LogUtils.d("发布", "发布失败:" + e);
                                    pDialog.cancel();
                                    Toast.makeText(FaBuActivity.this, "发布失败",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        //do something
                    } else {
                        pDialog.cancel();
                        Toast.makeText(FaBuActivity.this, "请先登陆或认证",
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


    public void Choose() {
        Matisse.from(FaBuActivity.this)
                .choose(MimeType.ofImage())
                .capture(true)  //是否可以拍照
                .captureStrategy(//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                        new CaptureStrategy(true, "com.flying.taokuang.fileprovider"))
                .countable(true)
                .maxSelectable(s)
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
            iAdapter = new UploadImgAdapter(this, iList);
            iAdapter.setOnItemClickListener(new UploadImgAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(File file, int position) {
                    im2.setVisibility(View.VISIBLE);
                    Glide.with(FaBuActivity.this).load(file).into(im2);
                }


            });
            iRecyclerView.setAdapter(iAdapter);
            s = 4 - (iAdapter.mList.size());
            if (s <= 0) {
                im1.setVisibility(View.INVISIBLE);
            }
            iAdapter.setM2(new UploadImgAdapter.M2() {
                @Override
                public void onDeliteClick(List list) {
                    iAdapter.notifyDataSetChanged();
                    s = 4 - list.size();
                    im1.setVisibility(View.VISIBLE);}

            });


        }

    }


}
