package com.flying.taokuang.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.flying.taokuang.R;
import com.flying.taokuang.entity.TaoKuang;
import com.flying.taokuang.holder.NewNormalGridViewHolder;
import com.flying.taokuang.holder.NewNormalViewHolder;

import java.util.ArrayList;
import java.util.List;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int NOMAL_ITEM = 9999;
    private static final int GRID_ITEM = 8888;

    private boolean mLinearStyle = true;
    private List<TaoKuang> mTaoList;
    private Context mContext;

    public HomeRecyclerViewAdapter(Context context, List<TaoKuang> list) {
        mContext = context;
        mTaoList = list;
    }

    public HomeRecyclerViewAdapter(Context context) {
        mContext = context;
        mTaoList = new ArrayList<>();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        switch (i) {
            case NOMAL_ITEM:
                return new NewNormalViewHolder(LayoutInflater.from(mContext).
                        inflate(R.layout.feed_new_item, viewGroup, false));
            case GRID_ITEM:
                return new NewNormalGridViewHolder(LayoutInflater.from(mContext).
                        inflate(R.layout.feed_new_item_grid, viewGroup, false));
        }
        return null;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        int type = getItemViewType(i);
        if (mTaoList == null || i < 0 || i >= mTaoList.size()) {
            return;
        }
        TaoKuang data = mTaoList.get(i);
        switch (type) {
            case NOMAL_ITEM:
                ((NewNormalViewHolder) viewHolder).bindViewHolder(data);
                break;
            case GRID_ITEM:
                ((NewNormalGridViewHolder) viewHolder).bindViewHolder(data);
                break;
        }
    }

    public void addData(List<TaoKuang> dataList) {
        int oldSize = mTaoList.size();
        mTaoList.addAll(dataList);
        notifyItemInserted(oldSize);
    }

    public void clearData() {
        mTaoList.clear();
        notifyDataSetChanged();
    }

    public void setLayoutStyle(boolean style) {
        mLinearStyle = style;
    }

    @Override
    public int getItemCount() {
        return mTaoList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mLinearStyle) {
            return NOMAL_ITEM;
        } else {
            return GRID_ITEM;
        }
    }
}