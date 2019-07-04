package com.flying.taokuang.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.flying.baselib.utils.ui.UiUtils;
import com.flying.taokuang.DetailActivity;
import com.flying.taokuang.R;
import com.flying.taokuang.entity.TaoKuang;
import com.flying.taokuang.ui.AsyncImageView;

import java.util.List;

public class NewNormalGridViewHolder extends RecyclerView.ViewHolder {

    private Context mContext;
    private AsyncImageView mIvCoverImage;
    private TextView mTvTitle;
    private TextView mTvPrice;
    private TextView mTvUserName;
    private TextView mTvContent;

    public NewNormalGridViewHolder(@NonNull View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        mIvCoverImage = itemView.findViewById(R.id.item_cover_img);
        mTvTitle = itemView.findViewById(R.id.item_title);
        mTvPrice = itemView.findViewById(R.id.item_price);
        mTvUserName = itemView.findViewById(R.id.item_goods_owner);
        mTvContent = itemView.findViewById(R.id.feed_item_goods_content);
    }


    public void bindViewHolder(final TaoKuang taoItem) {
        if (taoItem == null) {
            return;
        }
        String title = taoItem.getBiaoti();
        String price = taoItem.getJiage();
        String nickname = taoItem.getFabu().getNicheng();
        String content = taoItem.getMiaoshu().replaceAll("\n", "");
        List<String> pic = taoItem.getPic();

        mIvCoverImage.setUrl(pic.get(0), UiUtils.dp2px(150), UiUtils.dp2px(110));
        mIvCoverImage.setRoundingRadius(UiUtils.dp2px(4));
        mTvUserName.setText(nickname);
        mTvContent.setText(content);
        mTvTitle.setText(title);
        mTvPrice.setText("¥" + price);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (taoItem == null) {
                    return;
                }
                DetailActivity.go(mContext, taoItem.getObjectId());
            }
        });

    }

}