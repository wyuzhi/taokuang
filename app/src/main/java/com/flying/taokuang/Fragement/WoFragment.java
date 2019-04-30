package com.flying.taokuang.Fragement;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.flying.baselib.utils.app.LogUtils;
import com.flying.baselib.utils.ui.UiUtils;
import com.flying.taokuang.CommentActivity;
import com.flying.taokuang.LoginActivity;
import com.flying.taokuang.PersonalActivity;
import com.flying.taokuang.R;
import com.flying.taokuang.RengzActivity;
import com.flying.taokuang.entity.User;
import com.flying.taokuang.tool.BaseFragment;
import com.flying.taokuang.ui.AsyncImageView;
import com.flying.taokuang.wo.AboutActivity;
import com.flying.taokuang.wo.WofbActivity;
import com.flying.taokuang.wo.WogmActivity;
import com.flying.taokuang.wo.WomcActivity;
import com.flying.taokuang.wo.WoscActivity;
import com.flying.taokuang.wo.WozlActivity;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.TResult;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class WoFragment extends BaseFragment {
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_wo_fragment, container, false);
        initView(view);

        return view;
    }


    private void initView(View v) {
        woo_fb = v.findViewById(R.id.woo_fb);
        woo_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentsc1 = new Intent(getContext(), CommentActivity.class);
                startActivity(intentsc1);
            }
        });


        wosc = v.findViewById(R.id.wo_sc);
        wosc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentsc = new Intent(getContext(), WoscActivity.class);
                startActivity(intentsc);
            }
        });

        wozl = v.findViewById(R.id.wo_zl);
        wozl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentzl = new Intent(getContext(), WozlActivity.class);
                startActivity(intentzl);

            }
        });
        womc = v.findViewById(R.id.wo_mc);
        womc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentmc = new Intent(getContext(), WomcActivity.class);
                startActivity(intentmc);
            }
        });
        worz = v.findViewById(R.id.wo_rz);
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
        wofb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentfb = new Intent(getContext(), WofbActivity.class);
                startActivity(intentfb);
            }
        });

        wogm = v.findViewById(R.id.wo_gm);
        wogm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentgm = new Intent(getContext(), WogmActivity.class);
                startActivity(intentgm);
            }
        });
        wozx = v.findViewById(R.id.wo_zx);
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
        woicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BmobUser.isLogin()) {
                    //User user = BmobUser.getCurrentUser(User.class);
                    Toast.makeText(getContext(), "修改头像", Toast.LENGTH_SHORT).show();
                    iconChoose();
                } else {
                    Toast.makeText(getContext(), "请先登陆",
                            Toast.LENGTH_LONG).show();

                }
            }

            private void iconChoose() {
                TakePhoto takePhoto = getTakePhoto();
                configCompress(takePhoto);
                takePhoto.onPickFromGallery();

            }
        });

        aboutus = v.findViewById(R.id.wo_about_us);
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
                    Intent Personal = new Intent(getContext(), PersonalActivity.class);
                    Personal.putExtra("发布", user.getObjectId());
                    startActivity(Personal);
                } else {
                    Intent in = new Intent(getContext(), LoginActivity.class);
                    startActivity(in);
                }
            }
        });
        woicon.setPlaceholderImage(R.drawable.hdb);
        woicon.setRoundingRadius(UiUtils.dp2px(5));
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
        //final User user = BmobUser.getCurrentUser(User.class);//第一种方法 成功
        super.takeSuccess(result);
        iconfile = new File(result.getImages().get(0).getCompressPath());


        String iconPath = iconfile.getPath();
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
                                Toast.makeText(getContext(), "修改成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "修改失败" + e,
                                        Toast.LENGTH_LONG).show();
                                LogUtils.d("修改图片", "修改失败:" + e);
                            }
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "修改失败" + e,
                            Toast.LENGTH_LONG).show();
                    LogUtils.d("修改图片", "修改失败:" + e);
                }
            }
        });

        Glide.with(this).load(iconfile).into(woicon);
    }

    private void configCompress(TakePhoto takePhoto) {//压缩配置
        int maxSize = Integer.parseInt("102400");//最大 压缩
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
