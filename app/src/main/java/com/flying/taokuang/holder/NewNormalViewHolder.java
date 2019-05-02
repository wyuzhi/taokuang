package com.flying.taokuang.holder;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.flying.baselib.utils.ui.UiUtils;
import com.flying.taokuang.DetailActivity;
import com.flying.taokuang.UserPageActivity;
import com.flying.taokuang.R;
import com.flying.taokuang.entity.TaoKuang;
import com.flying.taokuang.entity.User;
import com.flying.taokuang.ui.AsyncImageView;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class NewNormalViewHolder extends RecyclerView.ViewHolder {

    private Context mContext;
    private AsyncImageView mIvCoverImage;
    private AsyncImageView mIvUserAvatar;
    private TextView mTvTitle;
    private TextView mTvPrice;
    private TextView mTvUserName;

    public NewNormalViewHolder(@NonNull View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        mIvCoverImage = itemView.findViewById(R.id.item_fm);
        mTvTitle = itemView.findViewById(R.id.item_bt);
        mTvPrice = itemView.findViewById(R.id.item_jg);
        mTvUserName = itemView.findViewById(R.id.feed_item_user_name);
        mIvUserAvatar = itemView.findViewById(R.id.feed_item_avatar);
    }


    public void bindViewHolder(final TaoKuang taoItem) {
        if (taoItem == null) {
            return;
        }
        String title = taoItem.getBiaoti();
        String price = taoItem.getJiage();
        String nickname = taoItem.getFabu().getNicheng();
        BmobFile avatar = taoItem.getFabu().getIcon();
        List<String> pic = taoItem.getPic();

        mIvCoverImage.setUrl(pic.get(0), (int) UiUtils.dp2px(200), (int) UiUtils.dp2px(120));
        mTvUserName.setText(nickname);
        if (avatar != null) {
            mIvUserAvatar.setUrl(taoItem.getFabu().getIcon().getUrl(), (int) UiUtils.dp2px(36), (int) UiUtils.dp2px(36));
        } else {
            mIvUserAvatar.setImageResource(R.drawable.ic_default_avatar);
        }
        UiUtils.setOnTouchBackground(mIvUserAvatar);
        UiUtils.setOnTouchBackground(mTvUserName);
        UiUtils.expandClickRegion(mIvUserAvatar, (int) UiUtils.dp2px(5));
        UiUtils.expandClickRegion(mTvUserName, (int) UiUtils.dp2px(5));
        mIvUserAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserPageActivity.go(mContext, taoItem.getFabu().getObjectId());
            }
        });
        mTvUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserPageActivity.go(mContext, taoItem.getFabu().getObjectId());
            }
        });
        mTvTitle.setText(title);
        mTvPrice.setText("￥" + price);
        itemView.setOnClickListener
                (new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         Intent detailIntent = new Intent(mContext, DetailActivity.class);
                         detailIntent.putExtra("图片", taoItem.getPic().toArray(new String[taoItem.getPic().size()]));
                         detailIntent.putExtra("价格", taoItem.getJiage());
                         detailIntent.putExtra("标题", taoItem.getBiaoti());
                         detailIntent.putExtra("描述", taoItem.getMiaoshu());
                         detailIntent.putExtra("位置", taoItem.getWeizhi());
                         detailIntent.putExtra("联系", taoItem.getLianxi());
                         detailIntent.putExtra("发布", taoItem.getFabu().getObjectId());
                         String a = taoItem.getFabu().getObjectId();
                         String c = "哈哈";
                         if (taoItem.getGoumai() != null) {
                             detailIntent.putExtra("购买", taoItem.getGoumai().getObjectId());
                             c = taoItem.getGoumai().getObjectId();
                         }

                         String b = BmobUser.getCurrentUser(User.class).getObjectId();
                         if (a.equals(b) && taoItem.getGoumai() != null) {
                             detailIntent.putExtra("购买name", taoItem.getGoumai().getNicheng());
                             detailIntent.putExtra("购买phone", taoItem.getGoumai().getPhone());
                             detailIntent.putExtra("鸽子id", taoItem.getGoumai().getObjectId());
                         }
                         if (c.equals(b)) {
                             detailIntent.putExtra("购买成功", "成功");
                         }


                         if (taoItem.getFabu().getIcon() != null) {
                             detailIntent.putExtra("发布icon", taoItem.getFabu().getIcon().getFileUrl());
                         }
                         detailIntent.putExtra("昵称", taoItem.getFabu().getNicheng());
                         detailIntent.putExtra("ID", taoItem.getObjectId());
                         mContext.startActivity(detailIntent);
                     }
                 }
                );

    }

}
