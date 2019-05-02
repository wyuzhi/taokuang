package com.flying.taokuang.Fragement;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.flying.baselib.utils.collection.CollectionUtils;
import com.flying.baselib.utils.ui.ToastUtils;
import com.flying.baselib.utils.ui.UiUtils;
import com.flying.taokuang.CommentActivity;
import com.flying.taokuang.LoginActivity;
import com.flying.taokuang.UserPageActivity;
import com.flying.taokuang.R;
import com.flying.taokuang.RengzActivity;
import com.flying.taokuang.debug.DebugActivity;
import com.flying.taokuang.entity.User;
import com.flying.taokuang.ui.AsyncImageView;
import com.flying.taokuang.wo.AboutActivity;
import com.flying.taokuang.wo.MySellingActivity;
import com.flying.taokuang.wo.WogmActivity;
import com.flying.taokuang.wo.WomcActivity;
import com.flying.taokuang.wo.WoscActivity;
import com.flying.taokuang.wo.WozlActivity;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoFragment;
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

public class WoFragment extends TakePhotoFragment {
    private static final int SHOW_DEBUG_ACTIVITY_NUMS = 2;
    private AsyncImageView woicon;
    private View wofb;
    private View wogm;
    private View womc;
    private View wozx;
    private View worz;
    private View wozl;
    private View wosc;
    private View woo_fb;
    private File iconfile;
    private View aboutus;
    private ImageView mIvBottomLogo;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_wo_fragment, container, false);
        initView(view);

        return view;
    }


    private void initView(View v) {
        mIvBottomLogo = v.findViewById(R.id.iv_fs_logo);
        mIvBottomLogo.setOnLongClickListener(new View.OnLongClickListener() {
            private int i = 0;
            @Override
            public boolean onLongClick(View v) {
                i++;
                if (i >= SHOW_DEBUG_ACTIVITY_NUMS) {
                    Intent intent = new Intent(getContext(), DebugActivity.class);
                    startActivity(intent);
                    i = 0;
                }
                return true;
            }
        });

        woo_fb = v.findViewById(R.id.woo_fb);
        UiUtils.setOnTouchBackground(woo_fb);
        woo_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentsc1 = new Intent(getContext(), CommentActivity.class);
                startActivity(intentsc1);
            }
        });

        wosc = v.findViewById(R.id.wo_sc);
        UiUtils.setOnTouchBackground(wosc);
        wosc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentsc = new Intent(getContext(), WoscActivity.class);
                startActivity(intentsc);
            }
        });

        wozl = v.findViewById(R.id.wo_zl);
        UiUtils.setOnTouchBackground(wozl);
        wozl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentzl = new Intent(getContext(), WozlActivity.class);
                startActivity(intentzl);

            }
        });
        womc = v.findViewById(R.id.wo_mc);
        UiUtils.setOnTouchBackground(womc);
        womc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentmc = new Intent(getContext(), WomcActivity.class);
                startActivity(intentmc);
            }
        });
        worz = v.findViewById(R.id.wo_rz);
        UiUtils.setOnTouchBackground(worz);
        worz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BmobUser.isLogin()) {
                    User user = BmobUser.getCurrentUser(User.class);
                    if (user.getRenz()) {
                        Toast.makeText(getContext(), "已认证成功",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Intent rz = new Intent(getContext(), RengzActivity.class);
                        startActivity(rz);
                    }
                } else Toast.makeText(getContext(), "请先登陆",
                        Toast.LENGTH_SHORT).show();

            }
        });
        wofb = v.findViewById(R.id.wo_fb);
        UiUtils.setOnTouchBackground(wofb);
        wofb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentfb = new Intent(getContext(), MySellingActivity.class);
                startActivity(intentfb);
            }
        });

        wogm = v.findViewById(R.id.wo_gm);
        UiUtils.setOnTouchBackground(wogm);
        wogm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentgm = new Intent(getContext(), WogmActivity.class);
                startActivity(intentgm);
            }
        });
        wozx = v.findViewById(R.id.wo_zx);
        UiUtils.setOnTouchBackground(wozx);
        wozx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobUser.logOut();
                Toast.makeText(getContext(), "注销成功",
                        Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });

        woicon = v.findViewById(R.id.wo_icon);
        UiUtils.setOnTouchBackground(woicon);
        woicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BmobUser.isLogin()) {
                    chooseAvatarFromGallery();
                } else {
                    ToastUtils.show("请先登陆");
                }
            }
        });

        aboutus = v.findViewById(R.id.wo_about_us);
        UiUtils.setOnTouchBackground(aboutus);
        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AboutActivity.class);
                startActivity(intent);
            }
        });


        Toolbar toolbar = v.findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbar = v.findViewById(R.id.collapsing_toolbar);
        //setSupportActionBar
        //collapsingToolbar = v.findViewById(R.id.wo_name);
        collapsingToolbar.setCollapsedTitleTypeface(Typeface.DEFAULT_BOLD);
        collapsingToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BmobUser.isLogin()) {
                    User user = BmobUser.getCurrentUser(User.class);
                    UserPageActivity.go(getContext(), user.getObjectId());
                } else {
                    Intent in = new Intent(getContext(), LoginActivity.class);
                    startActivity(in);
                }
            }
        });
        woicon.setPlaceholderImage(R.drawable.ic_default_avatar);
        woicon.setRoundAsCircle();
        if (BmobUser.isLogin()) {
            User user = BmobUser.getCurrentUser(User.class);
            collapsingToolbar.setTitle(user.getNicheng());
            BmobFile icon = user.getIcon();
            if (icon != null) {
                woicon.setUrl(icon.getFileUrl(), (int) UiUtils.dp2px(100), (int) UiUtils.dp2px(100));
            }
        }


    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        if (result == null || CollectionUtils.isEmpty(result.getImages())) {
            return;
        }
        iconfile = new File(result.getImages().get(0).getCompressPath());
        final BmobFile ic = new BmobFile(iconfile);
        ic.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    User user = BmobUser.getCurrentUser(User.class);
                    user.setIcon(ic);
                    user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                ToastUtils.show("修改成功");
                                woicon.setUrl(BmobUser.getCurrentUser(User.class).getIcon().getFileUrl());
                            } else {
                                ToastUtils.show("修改失败");
                            }
                        }
                    });
                } else {
                    ToastUtils.show("修改失败");
                }
            }
        });
    }

    private void chooseAvatarFromGallery() {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        Uri imageUri = Uri.fromFile(file);

        TakePhoto takePhoto = getTakePhoto();
        CompressConfig config = new CompressConfig.Builder().setMaxSize(60 * 1024)
                .setMaxPixel(300)
                .enableReserveRaw(false)
                .create();
        takePhoto.onEnableCompress(config, true);

        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        builder.setWithOwnGallery(true);
        builder.setCorrectImage(true);
        takePhoto.setTakePhotoOptions(builder.create());

        CropOptions cropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create();
        takePhoto.onPickFromGalleryWithCrop(imageUri, cropOptions);
    }
}
