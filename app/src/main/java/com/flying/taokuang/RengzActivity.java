package com.flying.taokuang;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.drawee.drawable.ScalingUtils;
import com.flying.baselib.utils.app.MainThread;
import com.flying.baselib.utils.collection.CollectionUtils;
import com.flying.baselib.utils.ui.UiUtils;
import com.flying.taokuang.entity.User;
import com.flying.taokuang.ui.AsyncImageView;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class RengzActivity extends TakePhotoActivity {
    private Button sc;
    private EditText xh;
    private EditText sjh;
    private AsyncImageView xsz;
    private File xyk;
    private ImageView mIvBack;

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

        mIvBack = findViewById(R.id.img_return);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        UiUtils.setOnTouchBackground(mIvBack);

        xsz = findViewById(R.id.rz_xsz);
        xsz.setRoundingRadius(UiUtils.dp2px(5));
        xsz.setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY);
        xsz.setPlaceholderImage(R.drawable.ic_upload_photo, ScalingUtils.ScaleType.CENTER_INSIDE);
        xsz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseSchoolCardFromGallery();
            }
        });
        sc = findViewById(R.id.rz_sc);
        sc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String xha = String.valueOf(xh.getText());
                final String sjha = String.valueOf(sjh.getText());
                if (!TextUtils.isEmpty(xha) && !TextUtils.isEmpty(sjha) && xyk != null) {
                    final BmobFile xsza = new BmobFile(xyk);
                    xsza.uploadblock(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                User user = BmobUser.getCurrentUser(User.class);
                                user.setPhone(sjha);
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

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        if (result == null || CollectionUtils.isEmpty(result.getImages())) {
            return;
        }
        xyk = new File(result.getImages().get(0).getCompressPath());
        MainThread.post(new Runnable() {
            @Override
            public void run() {
                if (xyk == null) {
                    return;
                }
                xsz.setUrl(Uri.fromFile(xyk).toString(), (int) UiUtils.dp2px(300), (int) UiUtils.dp2px(200));
            }
        });
    }

    private void chooseSchoolCardFromGallery() {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        Uri imageUri = Uri.fromFile(file);

        TakePhoto takePhoto = getTakePhoto();
        CompressConfig config = new CompressConfig.Builder().setMaxSize(100 * 1024)
                .setMaxPixel(600)
                .enableReserveRaw(false)
                .create();
        takePhoto.onEnableCompress(config, true);

        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        builder.setWithOwnGallery(true);
        builder.setCorrectImage(true);
        takePhoto.setTakePhotoOptions(builder.create());

        CropOptions cropOptions = new CropOptions.Builder().setAspectX(3).setAspectY(2).setWithOwnCrop(true).create();
        takePhoto.onPickFromGalleryWithCrop(imageUri, cropOptions);
    }
}
