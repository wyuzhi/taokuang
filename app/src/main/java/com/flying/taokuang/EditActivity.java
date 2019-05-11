package com.flying.taokuang;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.flying.baselib.utils.app.ApplicationUtils;
import com.flying.baselib.utils.app.LogUtils;
import com.flying.baselib.utils.collection.CollectionUtils;
import com.flying.baselib.utils.ui.ToastUtils;
import com.flying.baselib.utils.ui.UiUtils;
import com.flying.taokuang.Adapter.UploadImgAdapter;
import com.flying.taokuang.entity.TaoKuang;
import com.flying.taokuang.entity.User;
import com.flying.taokuang.ui.AsyncImageView;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;

public class EditActivity extends TakePhotoActivity implements View.OnClickListener {
    private static final int MIN_CLICK_DELAY_TIME = 10000;
    private static long lastClickTime;

    private static final int REQUEST_CODE_CHOOSE = 99;
    private int s = 4;

    private String id;
    private List<String> iList;
    private List<String> cList = new ArrayList<>();
    private List<String> zList = new ArrayList<>();

    private RecyclerView iRecyclerView;
    private UploadImgAdapter iAdapter;
    private EditText biaoti;
    private EditText miaoshu;
    private EditText weizhi;
    private EditText jiage;
    private EditText lianxi;
    private ImageView im1;
    private AsyncImageView im2;
    private ImageView mIvBack;
    private Button fabu;
    private Spinner leibie;
    private String tleibie;
    private String tbiaoti;
    private String tlianxi;
    private String tmiaoshu;

    private String tweizhi;
    private String tjiage;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
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

    private void initView() {

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
        iRecyclerView.setLayoutManager(new GridLayoutManager
                (this, 4, GridLayoutManager.VERTICAL, false));

        mIvBack = findViewById(R.id.img_return);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        UiUtils.setOnTouchBackground(mIvBack);

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
                    iAdapter = new UploadImgAdapter(EditActivity.this, iList);
                    iRecyclerView.setAdapter(iAdapter);
                    iAdapter.setM1(new UploadImgAdapter.M1() {
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
                    iAdapter.setM2(new UploadImgAdapter.M2() {
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
                chooseImagesFromGallery();
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


        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setTitle("正在发布");
        pDialog.setCancelable(false);
        pDialog.show();

        if (cList.size() == 0) {
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
                        ToastUtils.show(e == null ? "发布成功" : "发布失败");
                        pDialog.dismiss();
                        finish();
                    }
                });
                //do something
            } else {
                pDialog.cancel();
                Toast.makeText(EditActivity.this, "请先登陆或认证",
                        Toast.LENGTH_LONG).show();

            }
        } else {

            final String[] paths = cList.toArray(new String[0]);


            BmobFile.uploadBatch(paths, new UploadBatchListener() {
                @Override
                public void onSuccess(List<BmobFile> files, List<String> urls) {


                    if (urls.size() == paths.length) {//如果数量相等，则代表文件全部上传完成
                        LogUtils.d("图片", "图片成功");
                        zList.addAll(urls);
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
                            fb.setJiaoyi(false);
                            fb.setBuy(false);
                            fb.setGengxin(1);
                            fb.setPic(zList);
                            fb.setFabu(user);

                            fb.update(id, new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    ToastUtils.show(e == null ? "发布成功" : "发布失败");
                                    pDialog.dismiss();
                                    finish();
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
        iAdapter.setOnItemClickListener(new UploadImgAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(File file, int position) {
                im2.setVisibility(View.VISIBLE);
                im2.setUrl(Uri.fromFile(file).toString(), UiUtils.getScreenWidth(ApplicationUtils.getApplication()), UiUtils.getScreenHeight(ApplicationUtils.getApplication()));
            }


        });
        iRecyclerView.setAdapter(iAdapter);
        s = 4 - (iAdapter.mList.size());
        if (s <= 0) {
            im1.setVisibility(View.INVISIBLE);
        }

    }


}
