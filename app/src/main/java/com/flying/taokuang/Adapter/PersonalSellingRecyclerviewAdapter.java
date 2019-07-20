package com.flying.taokuang.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flying.baselib.utils.collection.CollectionUtils;
import com.flying.baselib.utils.ui.UiUtils;
import com.flying.taokuang.DetailActivity;
import com.flying.taokuang.R;
import com.flying.taokuang.entity.TaoKuang;
import com.flying.taokuang.ui.AsyncImageView;

import java.util.ArrayList;
import java.util.List;

public class PersonalSellingRecyclerviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<TaoKuang> mTaoList;
    private Context mContext;


    public PersonalSellingRecyclerviewAdapter(Context context) {
        mContext = context;
        mTaoList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new PersonalSellingViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.personal_selling_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (mTaoList == null || i < 0 || i >= mTaoList.size()) {
            return;
        }
        TaoKuang data = mTaoList.get(i);
        ((PersonalSellingViewHolder) viewHolder).bindViewHolder(data);
    }

    public void addData(List<TaoKuang> dataList) {
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        if (CollectionUtils.isEmpty(mTaoList)) {
            mTaoList = dataList;
            notifyDataSetChanged();
            return;
        }
        int oldSize = mTaoList.size();
        mTaoList.addAll(dataList);
        notifyItemRangeInserted(oldSize, dataList.size());
    }

    public void clearData() {
        mTaoList.clear();
    }

    @Override
    public int getItemCount() {
        return mTaoList.size();
    }

    public class PersonalSellingViewHolder extends RecyclerView.ViewHolder {
        private AsyncImageView mIvCoverImage;
        private TextView mTvPrice;
        private TextView mTvContent;
        private View mRoot;

        public PersonalSellingViewHolder(@NonNull View itemView) {
            super(itemView);
            mIvCoverImage = itemView.findViewById(R.id.item_cover_img);
            mTvPrice = itemView.findViewById(R.id.item_price);
            mTvContent = itemView.findViewById(R.id.item_content);
            mRoot = itemView;
        }

        public void bindViewHolder(final TaoKuang data) {
            if (data == null) {
                return;
            }
            mRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DetailActivity.go(mContext, data.getObjectId());
                }
            });
            mIvCoverImage.setRoundingRadius(UiUtils.dp2px(3));
            mIvCoverImage.setUrl(data.getPic().get(0), UiUtils.dp2px(137), UiUtils.dp2px(89));
            mTvPrice.setText("¥" + data.getJiage());
            mTvContent.setText(data.getMiaoshu());
        }
    }
}
