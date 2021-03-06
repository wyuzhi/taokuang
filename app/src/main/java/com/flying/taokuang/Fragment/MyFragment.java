package com.flying.taokuang.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flying.baselib.commonui.edit.EditorCallback;
import com.flying.baselib.commonui.edit.FloatEditorActivity;
import com.flying.baselib.commonui.edit.InputCheckRule;
import com.flying.baselib.utils.collection.CollectionUtils;
import com.flying.baselib.utils.ui.ToastUtils;
import com.flying.baselib.utils.ui.UiUtils;
import com.flying.taokuang.IdentifyActivity;
import com.flying.taokuang.LoginActivity;
import com.flying.taokuang.My.AboutActivity;
import com.flying.taokuang.My.MyChangePasswordActivity;
import com.flying.taokuang.My.MyWantActivity;
import com.flying.taokuang.My.DealActivity;
import com.flying.taokuang.R;
import com.flying.taokuang.UserPageActivity;
import com.flying.taokuang.entity.User;
import com.flying.taokuang.ui.AlertDialog;
import com.flying.taokuang.ui.AsyncImageView;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoFragment;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;

import java.io.File;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class MyFragment extends TakePhotoFragment {
    private Toolbar mToolbar;
    private AsyncImageView mIcon;
    private View mPublish;
    private View mPurchased;
    private View mSold;
    private View mCancel;
    private View mIdentify;
    private View mChange;
    private View mCollection;
    private View mWanted;
    private View mAbout;
    private TextView woinviter;
    private View mUserInfoView;
    private User mUser;
    private TextView mTvUserNickName;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_my_fragment, container, false);
        initView(view);

        return view;
    }


    private void initView(View v) {
        mToolbar = v.findViewById(R.id.toolbar);
        mToolbar.setFitsSystemWindows(true);
        ViewGroup.LayoutParams layoutParams = mToolbar.getLayoutParams();
        layoutParams.height = UiUtils.dp2px(50) + UiUtils.getStatusBarHeight(getContext());
        mToolbar.setLayoutParams(layoutParams);

        mUser = BmobUser.getCurrentUser(User.class);
        mTvUserNickName = v.findViewById(R.id.tv_user_nick_name);
        mUserInfoView = v.findViewById(R.id.rl_user_info);
        mUserInfoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUser != null) {
                    UserPageActivity.go(getContext(), mUser.getObjectId());
                } else {
                    Intent in = new Intent(getContext(), LoginActivity.class);
                    startActivity(in);
                }
            }
        });


        mChange = v.findViewById(R.id.wo_zl);
        UiUtils.setOnTouchBackground(mChange);
        mChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentzl = new Intent(getContext(), MyChangePasswordActivity.class);
                startActivity(intentzl);

            }
        });
        mSold = v.findViewById(R.id.wo_mc);
        UiUtils.setOnTouchBackground(mSold);
        mSold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentmc = new Intent(getContext(), DealActivity.class);
                intentmc.putExtra("id",2);
                startActivity(intentmc);
            }
        });
        mPurchased = v.findViewById(R.id.wo_gm);
        UiUtils.setOnTouchBackground(mPurchased);
        mPurchased.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentgm = new Intent(getContext(), DealActivity.class);
                intentgm.putExtra("id",3);
                startActivity(intentgm);
            }
        });
        mCollection = v.findViewById(R.id.wo_sc);
        UiUtils.setOnTouchBackground(mCollection);
        mCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentsc = new Intent(getContext(), DealActivity.class);
                intentsc.putExtra("id",4);
                startActivity(intentsc);
            }
        });
        mWanted = v.findViewById(R.id.wo_want);
        mWanted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentwant = new Intent(getContext(), MyWantActivity.class);
                startActivity(intentwant);
            }
        });
        mIdentify = v.findViewById(R.id.wo_rz);
        UiUtils.setOnTouchBackground(mIdentify);
        mIdentify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUser != null) {
                    if (mUser.getRenz()) {
                        ToastUtils.show("已认证成功");
                    } else {
                        Intent rz = new Intent(getContext(), IdentifyActivity.class);
                        startActivity(rz);
                    }
                } else {
                    Intent in = new Intent(getContext(), LoginActivity.class);
                    startActivity(in);
                }
            }
        });
        mPublish = v.findViewById(R.id.wo_fb);
        UiUtils.setOnTouchBackground(mPublish);
        mPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentfb = new Intent(getContext(), DealActivity.class);
                startActivity(intentfb);
            }
        });


        mCancel = v.findViewById(R.id.wo_zx);
        UiUtils.setOnTouchBackground(mCancel);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUser != null) {
                    final AlertDialog logoutDialog = new AlertDialog(getActivity());
                    logoutDialog.setTitle(R.string.my_log_out_title);
                    logoutDialog.setConfirmButton(R.string.my_log_out_confirm, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            BmobUser.logOut();
                            Intent i = new Intent(getActivity(), LoginActivity.class);
                            startActivity(i);
                            getActivity().finish();
                        }
                    });
                    logoutDialog.setCancelButton(R.string.my_log_out_cancel, null);
                    logoutDialog.show();
                }
            }
        });

        mIcon = v.findViewById(R.id.wo_icon);
        UiUtils.setOnTouchBackground(mIcon);
        mIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUser != null) {
                    chooseAvatarFromGallery();
                }
            }
        });

        mAbout = v.findViewById(R.id.wo_about_us);
        UiUtils.setOnTouchBackground(mAbout);
        UiUtils.expandClickRegion(mIcon, UiUtils.dp2px(10));
        mAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AboutActivity.class);
                startActivity(intent);
            }
        });
        mIcon.setPlaceholderImage(R.drawable.ic_default_avatar);
        mIcon.setRoundAsCircle();
        if (mUser != null) {
            mTvUserNickName.setText(mUser.getNicheng());
            UiUtils.expandClickRegion(mTvUserNickName, 15);
            if (mUser.getIcon() != null) {
                mIcon.setUrl(mUser.getIcon().getFileUrl(), UiUtils.dp2px(44), UiUtils.dp2px(44));
            }
            mTvUserNickName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FloatEditorActivity.openDefaultEditor(getContext(), new EditorCallback.Extend() {
                        @Override
                        public void onSubmit(final String content) {
                            if (mUser != null && !TextUtils.isEmpty(content)) {
                                mUser.setNicheng(content);
                                mUser.update(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null && mTvUserNickName != null) {
                                            mTvUserNickName.setText(content);
                                        } else {
                                            ToastUtils.show(e.getMessage());
                                        }
                                    }
                                });
                            }
                        }
                    }, new InputCheckRule(20, 2));
                }
            });
        }

        woinviter = v.findViewById(R.id.wo_inviter);
        String username = (String) BmobUser.getObjectByKey("username");
        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereEqualTo("inviter", username);
        query.count(User.class, new CountListener() {
            @Override
            public void done(Integer count, BmobException e) {
                if (e == null) {
                    woinviter.setText(count.toString());
                }
            }
        });

    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        if (result == null || CollectionUtils.isEmpty(result.getImages())) {
            return;
        }
        File iconfile = new File(result.getImages().get(0).getCompressPath());
        final BmobFile ic = new BmobFile(iconfile);
        ic.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null && mUser != null) {
                    mUser.setIcon(ic);
                    mUser.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                ToastUtils.show("修改成功");
                                mIcon.setUrl(mUser.getIcon().getFileUrl(), UiUtils.dp2px(44), UiUtils.dp2px(44));
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
