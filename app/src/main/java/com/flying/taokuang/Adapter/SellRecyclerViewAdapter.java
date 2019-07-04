package com.flying.taokuang.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.flying.baselib.utils.collection.CollectionUtils;
import com.flying.taokuang.R;
import com.flying.taokuang.entity.TaoKuang;
import com.flying.taokuang.holder.MySellViewHolder;

import java.util.ArrayList;
import java.util.List;

public class SellRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TaoKuang> mTaoList;
    private Context mContext;

    public SellRecyclerViewAdapter(Context context, List<TaoKuang> list) {
        mContext = context;
        mTaoList = list;
    }

    public SellRecyclerViewAdapter(Context context) {
        mContext = context;
        mTaoList = new ArrayList<>();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        return new MySellViewHolder(LayoutInflater.from(mContext).
                inflate(R.layout.selling_item, viewGroup, false));

    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (mTaoList == null || i < 0 || i >= mTaoList.size()) {
            return;
        }
        TaoKuang data = mTaoList.get(i);

        ((MySellViewHolder) viewHolder).bindViewHolder(data);


    }

    public void addData(List<TaoKuang> dataList) {
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        if (CollectionUtils.isEmpty(mTaoList)) {
            mTaoList.addAll(dataList);
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

}
