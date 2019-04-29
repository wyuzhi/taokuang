package com.flying.taokuang.holder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.flying.baselib.utils.ui.UiUtils;
import com.flying.taokuang.DetailActivity;
import com.flying.taokuang.R;
import com.flying.taokuang.entity.TaoKuang;
import com.flying.taokuang.entity.User;
import com.flying.taokuang.ui.AsyncImageView;

import java.util.List;

import cn.bmob.v3.BmobUser;

public class NormalViewHolder extends RecyclerView.ViewHolder {

    private Context mContext;
    public AsyncImageView sIvCoverImage;
    public TextView sTvTitle;
    public TextView sTvPrice;

    public NormalViewHolder(@NonNull View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        sIvCoverImage = itemView.findViewById(R.id.item_fm);
        sTvTitle = itemView.findViewById(R.id.item_bt);
        sTvPrice = itemView.findViewById(R.id.item_jg);
    }


    public void bindViewHolder(final TaoKuang taoItem) {
        if (taoItem == null) {
            return;
        }
        String biaoti = taoItem.getBiaoti();
        String jiage = taoItem.getJiage();
        List<String> pic = taoItem.getPic();

        sIvCoverImage.setUrl(pic.get(0));
        sIvCoverImage.setRoundingRadius(UiUtils.dp2px(5));
        sTvTitle.setText("     " + biaoti);
        sTvPrice.setText("    ￥" + jiage);
        sTvPrice.setTextColor(Color.RED);
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
