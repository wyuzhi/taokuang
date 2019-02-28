package com.example.taokuang.Fragement;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.taokuang.R;
import com.example.taokuang.RengzActivity;
import com.example.taokuang.entity.User;
import com.example.taokuang.tool.BaseFragment;
import com.example.taokuang.wo.WofbActivity;
import com.example.taokuang.wo.WogmActivity;
import com.example.taokuang.wo.WomcActivity;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.TResult;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class WoFragment extends BaseFragment {
    private ImageView woicon;
    private TextView woname;
    private ImageView wofb;
    private ImageView wogm;
    private ImageView womc;
    private ImageView wozx;
    private ImageView worz;
    private File iconfile;
    private ImageLoader imageLoader = ImageLoader.getInstance();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_wo_fragment, container, false);
        initView(view);

        return view;
    }

    private void initView(View v) {
        womc=v.findViewById(R.id.wo_mc);
        womc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentmc = new Intent(getContext(), WomcActivity.class);
                startActivity(intentmc);
            }
        });
        worz=v.findViewById(R.id.wo_rz);
        worz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BmobUser.isLogin()) {
                    User user = BmobUser.getCurrentUser(User.class);
                    if(user.getRenz()){
                        Toast.makeText(getContext(), "已认证成功",
                                Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent rz = new Intent(getContext(), RengzActivity.class);
                        startActivity(rz);
                    }
                }
                else Toast.makeText(getContext(), "请先登陆",
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
                Log.d("注销", "注销成功");
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

        woname = v.findViewById(R.id.wo_name);
        if (BmobUser.isLogin()) {
            User user = BmobUser.getCurrentUser(User.class);
            woname.setText(user.getNicheng());
            BmobFile icon = user.getIcon();
            if (icon != null){
                // 创建DisplayImageOptions对象并进行相关选项配置
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .showImageOnLoading(R.drawable.hdb)// 设置图片下载期间显示的图片
                        .showImageForEmptyUri(R.drawable.hdb)// 设置图片Uri为空或是错误的时候显示的图片
                        .showImageOnFail(R.drawable.hdb)// 设置图片加载或解码过程中发生错误显示的图片
                        .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                        .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
                        .displayer(new RoundedBitmapDisplayer(20))// 设置成圆角图片
                        .build();// 创建DisplayImageOptions对象
                // 使用ImageLoader加载图片
                imageLoader.displayImage(icon.getFileUrl(),
                        woicon, options);

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
                                Log.d("修改图片", "修改失败:" + e);
                            }
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "修改失败" + e,
                            Toast.LENGTH_LONG).show();
                    Log.d("修改图片", "修改失败:" + e);
                }
            }
        });

        Glide.with(this).load(new File(result.getImages().get(0).getCompressPath())).into(woicon);
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
