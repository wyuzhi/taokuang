package com.flying.taokuang.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.drawable.ScalingUtils;
import com.flying.baselib.utils.collection.CollectionUtils;
import com.flying.baselib.utils.ui.UiUtils;
import com.flying.taokuang.R;
import com.flying.taokuang.ui.AsyncImageView;

import java.util.ArrayList;
import java.util.List;

public class DetailImageAdapter extends RecyclerView.Adapter<DetailImageAdapter.ViewHolder> {
    public List<String> mList;
    private Context mContext;

    public DetailImageAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<>();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.img_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        viewHolder.img.setUrl(mList.get(position), UiUtils.dp2px(250), UiUtils.dp2px(185));
        viewHolder.img.setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);
        viewHolder.img.setRoundingRadius(UiUtils.dp2px(3));
    }

    public void setData(List data) {
        if (CollectionUtils.isEmpty(data)) {
            return;
        }
        mList = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private AsyncImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.detail_im);
            img.setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY);
        }
    }
}