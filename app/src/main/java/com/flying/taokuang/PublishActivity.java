package com.flying.taokuang;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
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
import android.widget.TextView;

import com.flying.baselib.utils.app.MainThread;
import com.flying.baselib.utils.app.UploadImageUtils;
import com.flying.baselib.utils.collection.CollectionUtils;
import com.flying.baselib.utils.thread.Thread;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class PublishActivity extends TakePhotoActivity implements View.OnClickListener {
    public static final int TYPE_SELL = 0;
    public static final int TYPE_BUY = 1;
    private static final String TAG_GOODS_ID = "id";
    private static final String TAG_TYPE = "type";
    private static final int MAX_IMAGE_NUMS = 4;

    private String mGoodsId;
    private int mType;

    private RecyclerView mRvImages;
    private UploadImgAdapter mImageAdapter;
    private EditText biaoti;
    private EditText miaoshu;
    private EditText weizhi;
    private EditText jiage;
    private EditText lianxi;
    private ImageView mIvAddImage;
    private ImageView mIvBack;
    private Button mBtnPublish;
    private Spinner leibie;
    private Toolbar mToolbar;
    private TextView mTvTitle;
    private ProgressDialog mProgressDialog;

    public static void go(Context context, int type) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, PublishActivity.class);
        intent.putExtra(TAG_TYPE, type);
        context.startActivity(intent);
    }

    public static void go(Context context, String id, int type) {
        if (context == null || TextUtils.isEmpty(id)) {
            return;
        }
        Intent intent = new Intent(context, PublishActivity.class);
        intent.putExtra(TAG_GOODS_ID, id);
        intent.putExtra(TAG_TYPE, type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);
        Intent intent = getIntent();
        mGoodsId = intent.getStringExtra(TAG_GOODS_ID);
        mType = intent.getIntExtra(TAG_TYPE, TYPE_SELL);
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
        mTvTitle = findViewById(R.id.tv_title);
        leibie = findViewById(R.id.spinner_leibie);
        biaoti = findViewById(R.id.edit_biaoti);
        miaoshu = findViewById(R.id.edit_description);
        weizhi = findViewById(R.id.edit_weizhi);
        jiage = findViewById(R.id.edit_jiage);
        lianxi = findViewById(R.id.edit_lianxi);
        mIvAddImage = findViewById(R.id.im_1);
        mBtnPublish = findViewById(R.id.btn_publish);
        mIvAddImage.setOnClickListener(this);
        mBtnPublish.setOnClickListener(this);
        if (mType == TYPE_BUY) {
            mTvTitle.setText(R.string.publish_buy_tips);
        } else {
            mTvTitle.setText(R.string.publish_sell_tips);
        }
        mRvImages = findViewById(R.id.image_RecyclerView);
        mRvImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mImageAdapter = new UploadImgAdapter(this);
        mRvImages.setAdapter(mImageAdapter);
        mImageAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                check();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                check();
            }

            private void check() {
                if (mImageAdapter == null || mIvAddImage == null) {
                    return;
                }
                if (mImageAdapter.getItemCount() < MAX_IMAGE_NUMS) {
                    mIvAddImage.setVisibility(View.VISIBLE);
                } else {
                    mIvAddImage.setVisibility(View.INVISIBLE);
                }
            }
        });
        mIvBack = findViewById(R.id.img_return);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        UiUtils.setOnTouchBackground(mIvBack);
        if (!TextUtils.isEmpty(mGoodsId)) {
            BmobQuery<TaoKuang> bmobQuery = new BmobQuery<>();
            bmobQuery.getObject(mGoodsId, new QueryListener<TaoKuang>() {
                @Override
                public void done(TaoKuang taoKuang, BmobException e) {
                    if (e != null) {
                        ToastUtils.show(String.valueOf(e.getErrorCode()));
                        finish();
                        return;
                    }
                    biaoti.setText(taoKuang.getBiaoti());
                    miaoshu.setText(taoKuang.getMiaoshu());
                    weizhi.setText(taoKuang.getWeizhi());
                    jiage.setText(taoKuang.getJiage());
                    lianxi.setText(taoKuang.getLianxi());
                    mImageAdapter.addData(taoKuang.getPic());
                }
            });
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.im_1:
                chooseImagesFromGallery();
                break;
            case R.id.btn_publish:
                if (mProgressDialog == null) {
                    mProgressDialog = new ProgressDialog(this);
                    mProgressDialog.setTitle(R.string.publish_publishing_tips);
                    mProgressDialog.setCancelable(false);
                }
                mProgressDialog.show();
                if (!checkInfo()) {
                    mProgressDialog.dismiss();
                    return;
                }
                new Thread() {
                    @Override
                    public void run() {
                        if (mImageAdapter == null) {
                            return;
                        }
                        List<String> localImages = mImageAdapter.getLocal();
                        if (!CollectionUtils.isEmpty(localImages)) {
                            List data = mImageAdapter.getData();
                            for (String path : localImages) {
                                String result = UploadImageUtils.postFile("https://school.chpz527.cn/api/upload", null, new File(path), getApplicationContext());
                                if (!TextUtils.isEmpty(result) && data.contains(path)) {
                                    data.remove(path);
                                    data.add(result);
                                }
                            }
                        }
                        MainThread.post(new Runnable() {
                            @Override
                            public void run() {
                                if (mImageAdapter != null) {
                                    mImageAdapter.notifyDataSetChanged();
                                }
                                publishGoods();
                            }
                        });
                    }
                }.start();
                break;
        }

    }

    private boolean checkInfo() {
        String tleibie = leibie.getSelectedItem().toString();
        String tlianxi = lianxi.getText().toString();
        String tbiaoti = biaoti.getText().toString();
        String tmiaoshu = miaoshu.getText().toString();
        String tweizhi = weizhi.getText().toString();
        String tjiage = jiage.getText().toString();

        if (TextUtils.isEmpty(tlianxi) || TextUtils.isEmpty(tbiaoti) || TextUtils.isEmpty(tmiaoshu) || TextUtils.isEmpty(tweizhi) || TextUtils.isEmpty(tjiage)) {
            ToastUtils.show(getString(R.string.publish_fix_info_tips));
            return false;
        }
        if (tleibie == null || tleibie.equals("类别")) {
            ToastUtils.show(getString(R.string.publish_select_type_tips));
            return false;
        }
        return true;
    }

    private void publishGoods() {
        String tleibie = leibie.getSelectedItem().toString();
        String tlianxi = lianxi.getText().toString();
        String tbiaoti = biaoti.getText().toString();
        String tmiaoshu = miaoshu.getText().toString();
        String tweizhi = weizhi.getText().toString();
        String tjiage = jiage.getText().toString();

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
        fb.setPic(mImageAdapter.getRemote());
        fb.setFabu(user);
        if (mType == TYPE_BUY) {
            fb.setGoumai(user);
            fb.setType("1");
        }
        if (TextUtils.isEmpty(mGoodsId)) {
            fb.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    mProgressDialog.dismiss();
                    if (e == null) {
                        ToastUtils.show(getString(R.string.publish_success_tips));
                        finish();
                    } else {
                        ToastUtils.show(getString(R.string.publish_failure_tips));
                    }
                }
            });
        } else {
            fb.update(mGoodsId, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    mProgressDialog.dismiss();
                    if (e == null) {
                        ToastUtils.show(getString(R.string.publish_success_tips));
                        finish();
                    } else {
                        ToastUtils.show(getString(R.string.publish_failure_tips));
                    }
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
        int num = MAX_IMAGE_NUMS - mImageAdapter.getItemCount();
        if (num > 0) {
            takePhoto.onPickMultipleWithCrop(num, cropOptions);
        }
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        if (result == null || CollectionUtils.isEmpty(result.getImages())) {
            return;
        }
        int newImageNums = MAX_IMAGE_NUMS - mImageAdapter.getItemCount();
        if (newImageNums <= 0) {
            return;
        }
        ArrayList localFile = new ArrayList();
        if (result.getImages().size() > newImageNums) {
            for (TImage image : result.getImages().subList(0, newImageNums)) {
                localFile.add(image.getCompressPath());
            }
        } else {
            for (TImage image : result.getImages()) {
                localFile.add(image.getCompressPath());
            }
        }
        mImageAdapter.addData(localFile);
    }


}
