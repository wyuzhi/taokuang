package com.flying.taokuang.holder;

import android.content.Context;
import android.graphics.Color;
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

public class NormalViewHolder extends RecyclerView.ViewHolder {

    private Context mContext;
    private AsyncImageView mIvCoverImage;
    private TextView mTvTitle;
    private TextView mTvPrice;

    public NormalViewHolder(@NonNull View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        mIvCoverImage = itemView.findViewById(R.id.item_fm);
        mTvTitle = itemView.findViewById(R.id.item_title);
        mTvPrice = itemView.findViewById(R.id.item_price);
    }


    public void bindViewHolder(final TaoKuang taoItem) {
        if (taoItem == null) {
            return;
        }
        String biaoti = taoItem.getBiaoti();
        String jiage = taoItem.getJiage();
        List<String> pic = taoItem.getPic();

        mIvCoverImage.setUrl(pic.get(0), UiUtils.dp2px(172), UiUtils.dp2px(227));
        mIvCoverImage.setRoundingRadius(UiUtils.dp2px(5));
        mTvTitle.setText(biaoti);
        mTvPrice.setText("￥" + jiage);
        mTvPrice.setTextColor(Color.RED);
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
