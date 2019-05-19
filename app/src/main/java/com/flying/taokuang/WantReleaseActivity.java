package com.flying.taokuang;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.flying.baselib.utils.app.LogUtils;
import com.flying.baselib.utils.app.UploadImageUtils;
import com.flying.baselib.utils.collection.CollectionUtils;
import com.flying.baselib.utils.ui.ToastUtils;
import com.flying.baselib.utils.ui.UiUtils;
import com.flying.taokuang.Adapter.UploadImgAdapter;
import com.flying.taokuang.entity.TaoKuang;
import com.flying.taokuang.entity.User;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
import com.tendcloud.tenddata.TCAgent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class WantReleaseActivity extends TakePhotoActivity implements View.OnClickListener {
    private static final int MIN_CLICK_DELAY_TIME = 10000;
    private static long lastClickTime;
    private int s = 4;

    private static final int REQUEST_CODE_CHOOSE = 99;

    private List<String> iList;
    private List<String> zList = new ArrayList<>();
    List<String> iurls = new ArrayList<>();


    private RecyclerView iRecyclerView;
    private UploadImgAdapter iAdapter;


    private Boolean CONTINUE = false;
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
    private ImageView mIvBack;

    private Toolbar mToolbar;
    ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_want_publish);
        initToolBar();
        initView();
    }

    private void initToolBar() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.commonColorGrey3));
        mToolbar.setFitsSystemWindows(true);
        ViewGroup.LayoutParams layoutParams = mToolbar.getLayoutParams();
        layoutParams.height = UiUtils.dp2px(50) + UiUtils.getStatusBarHeight(this);
        mToolbar.setLayoutParams(layoutParams);
    }

    @Override
    protected void onPause() {
        super.onPause();
        TCAgent.onPageEnd(this, "发布页");
    }

    @Override
    protected void onResume() {
        super.onResume();
        TCAgent.onPageStart(this, "发布页");
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
        iList = new ArrayList<>();
        im2 = findViewById(R.id.expanded_image);
        im2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                im2.setVisibility(View.INVISIBLE);
            }
        });

        leibie = findViewById(R.id.spinner_leibie);
        biaoti = findViewById(R.id.edit_biaoti);
        miaoshu = findViewById(R.id.edit_description);
        weizhi = findViewById(R.id.edit_weizhi);
        jiage = findViewById(R.id.edit_jiage);
        lianxi = findViewById(R.id.edit_lianxi);
        im1 = findViewById(R.id.im_1);
        fabu = findViewById(R.id.fabu);
        im1.setOnClickListener(this);
        fabu.setOnClickListener(this);

        iRecyclerView = findViewById(R.id.image_RecyclerView);
        iRecyclerView.setLayoutManager(new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false));

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.im_1:
                chooseImagesFromGallery();
                break;
            case R.id.fabu:

                long curClickTime = System.currentTimeMillis();
                if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
                    // 超过点击间隔后再将lastClickTime重置为当前点击时间
                    lastClickTime = curClickTime;
                    pDialog = new ProgressDialog(this);
                    pDialog.setTitle("正在发布");
                    pDialog.setCancelable(false);
                    pDialog.show();
                    if (CONTINUE) {
                        zList = iAdapter.getImage();
                        final String[] paths = zList.toArray(new String[0]);
                        new WantReleaseActivity.AsynTask().execute(paths);
                    } else {
                        ToastUtils.show("请至少添加一张图片");
                        pDialog.dismiss();
                    }
                }
                break;
        }

    }

    class AsynTask extends AsyncTask<String[], Integer, String> {


        @Override
        protected String doInBackground(String[]... strings) {
            for (int i = 0; i < strings[0].length; i++) {
                iurls.add(UploadImageUtils.postFile("https://school.chpz527.cn/api/upload",
                        null, new File(String.valueOf(strings[0][i])), getApplicationContext()));
            }
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            Fabu();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }


    }


    private void Fabu() {
        if (CollectionUtils.isEmpty(iList)) {
            ToastUtils.show("请至少添加一张图片");
            return;
        }


        tleibie = String.valueOf(leibie.getSelectedItem());
        tlianxi = String.valueOf(lianxi.getText().toString());
        tbiaoti = String.valueOf(biaoti.getText().toString());
        tmiaoshu = String.valueOf(miaoshu.getText().toString());
        tweizhi = String.valueOf(weizhi.getText().toString());
        tjiage = String.valueOf(jiage.getText().toString());

        if (TextUtils.isEmpty(tlianxi) || TextUtils.isEmpty(tbiaoti) || TextUtils.isEmpty(tmiaoshu) || TextUtils.isEmpty(tweizhi) || TextUtils.isEmpty(tjiage)) {
            ToastUtils.show("请将信息填写完整");
            return;
        }
        if (tleibie == null || tleibie.equals("类别")) {
            ToastUtils.show("请选择类别");
            return;
        }


        final String[] paths = zList.toArray(new String[0]);


//        BmobFile.uploadBatch(paths, new UploadBatchListener() {
//            @Override
//            public void onSuccess(List<BmobFile> files, List<String> urls) {


        if (iurls.size() == paths.length) {//如果数量相等，则代表文件全部上传完成
            LogUtils.d("图片", "图片成功");
            //Toast.makeText(ReleaseActivity.this, "图片成功",
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
                fb.setJiaoyi(true);
                fb.setBuy(false);
                fb.setGengxin(1);
                fb.setPic(iurls);
                fb.setFabu(user);
                fb.setGoumai(user);
                fb.setType("1");


                fb.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        ToastUtils.show(e == null ? "发布成功" : "发布失败");
                        pDialog.dismiss();
                        finish();
                    }
                });

                //do something
            } else {
                pDialog.cancel();
                ToastUtils.show("请先登陆或认证");

            }
        }

    }


//            @Override
//            public void onProgress(int i, int i1, int i2, int i3) {
//
//            }
//
//            @Override
//            public void onError(int i, String s) {
//                LogUtils.d("图片", "图片失败" + s);
//            }

//        });


//    }

    private void chooseImagesFromGallery() {
        TakePhoto takePhoto = getTakePhoto();
        CompressConfig config = new CompressConfig.Builder().setMaxSize(250 * 1024)
                .setMaxPixel(1500)
                .enableReserveRaw(false)
                .create();
        takePhoto.onEnableCompress(config, true);

        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        builder.setWithOwnGallery(true);
        builder.setCorrectImage(true);
        takePhoto.setTakePhotoOptions(builder.create());

        CropOptions cropOptions = new CropOptions.Builder().setAspectX(4).setAspectY(3).setWithOwnCrop(true).create();
        takePhoto.onPickMultipleWithCrop(4, cropOptions);
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        if (result == null || CollectionUtils.isEmpty(result.getImages())) {
            return;
        }
        for (TImage image : result.getImages()) {
            iList.add(image.getCompressPath());
        }
        iAdapter = new UploadImgAdapter(this, iList);
        CONTINUE = true;
        iAdapter.setOnItemClickListener(new UploadImgAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(File file, int position) {
                im2.setVisibility(View.VISIBLE);
                Glide.with(WantReleaseActivity.this).load(file).into(im2);
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
                im1.setVisibility(View.VISIBLE);
            }

        });

    }


}
