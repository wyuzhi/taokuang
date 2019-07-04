package com.flying.taokuang.holder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.flying.baselib.utils.app.LogUtils;
import com.flying.baselib.utils.ui.ToastUtils;
import com.flying.baselib.utils.ui.UiUtils;
import com.flying.taokuang.DetailActivity;
import com.flying.taokuang.PublishActivity;
import com.flying.taokuang.R;
import com.flying.taokuang.entity.TaoKuang;
import com.flying.taokuang.entity.User;
import com.flying.taokuang.ui.AsyncImageView;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class MySellViewHolder extends RecyclerView.ViewHolder {

    private static final int MIN_CLICK_DELAY_TIME = 3600;
    private static long lastClickTime;

    private Context mContext;
    private AsyncImageView mIvCoverImage;
    private TextView mTvTitle;
    private TextView mTvPrice;
    private TextView mTvContent;
    private Button mBtnEdit;
    private Button mBtnDelete;
    private Button mBtnRefresh;

    public MySellViewHolder(@NonNull View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        mIvCoverImage = itemView.findViewById(R.id.item_cover_img);
        mTvTitle = itemView.findViewById(R.id.item_bt_zs);
        mTvPrice = itemView.findViewById(R.id.item_jg_zs);
        mTvContent = itemView.findViewById(R.id.item_content);
        mBtnEdit = itemView.findViewById(R.id.item_bj_zs);
        mBtnDelete = itemView.findViewById(R.id.item_sc_zs);
        mBtnRefresh = itemView.findViewById(R.id.item_sc_cl);
    }

    public void bindViewHolder(final TaoKuang taoItem) {
        if (taoItem == null) {
            return;
        }
        String biaoti = taoItem.getBiaoti();
        String jiage = taoItem.getJiage();
        String content = taoItem.getMiaoshu();
        List<String> pic = taoItem.getPic();

        mIvCoverImage.setUrl(pic.get(0), (int) UiUtils.dp2px(172), (int) UiUtils.dp2px(227));
        mIvCoverImage.setRoundingRadius(UiUtils.dp2px(5));
        mTvTitle.setText(biaoti);
        mTvPrice.setText("¥" + jiage);
        mTvPrice.setTextColor(Color.RED);
        mTvContent.setText(content);
        mBtnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long curClickTime = System.currentTimeMillis();
                if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
                    // 超过点击间隔后再将lastClickTime重置为当前点击时间
                    lastClickTime = curClickTime;
                    TaoKuang cl = new TaoKuang();
                    int i = taoItem.getGengxin();
                    i++;
                    cl.setGengxin(i);
                    cl.update(taoItem.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                LogUtils.d("擦亮", "擦亮成功");
                                ToastUtils.show("擦亮成功");
                                mBtnRefresh.setBackgroundColor(Color.GRAY);
                            } else {
                                LogUtils.d("擦亮", e.toString());
                                ToastUtils.show("擦亮失败" + e.toString());
                            }
                        }
                    });
                }
            }
        });
        mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaoKuang sc = new TaoKuang();
                sc.delete(taoItem.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            LogUtils.d("删除", "删除成功");
                            ToastUtils.show("删除成功");

                        } else {
                            LogUtils.d("删除", e.toString());
                            ToastUtils.show("删除失败");
                        }
                    }
                });
            }
        });

        mBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublishActivity.go(mContext, taoItem.getObjectId(), PublishActivity.TYPE_SELL);
            }
        });

        mIvCoverImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailIntent = new Intent(mContext, DetailActivity.class);
                detailIntent.putExtra("图片", taoItem.getPic().toArray(new String[0]));
                detailIntent.putExtra("价格", taoItem.getJiage());
                detailIntent.putExtra("标题", taoItem.getBiaoti());
                detailIntent.putExtra("描述", taoItem.getMiaoshu());
                detailIntent.putExtra("位置", taoItem.getWeizhi());
                detailIntent.putExtra("联系", taoItem.getLianxi());
                detailIntent.putExtra("发布", taoItem.getFabu().getObjectId());
                if (taoItem.getFabu().getIcon() != null) {
                    detailIntent.putExtra("发布icon", taoItem.getFabu().getIcon().getFileUrl());
                }
                /////
                String c = "哈哈";
                String b = BmobUser.getCurrentUser(User.class).getObjectId();
                if (taoItem.getGoumai() != null) {
                    detailIntent.putExtra("购买", taoItem.getGoumai().getObjectId());
                    c = taoItem.getGoumai().getObjectId();
                }
                if (c.equals(b)) {
                    detailIntent.putExtra("购买成功", "成功");
                }
                detailIntent.putExtra("昵称", taoItem.getFabu().getNicheng());
                detailIntent.putExtra("ID", taoItem.getObjectId());
                mContext.startActivity(detailIntent);
            }
        });

    }
}
