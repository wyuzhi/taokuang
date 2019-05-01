package com.flying.taokuang.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.flying.taokuang.R;
import com.flying.taokuang.entity.TaoKuang;
import com.flying.taokuang.holder.NormalViewHolder;

import java.util.ArrayList;
import java.util.List;

public class PersonalSellingRecyclerviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TaoKuang> mTaoList;
    private Context mContext;

    public PersonalSellingRecyclerviewAdapter(Context context, List<TaoKuang> list) {
        mContext = context;
        mTaoList = list;
    }

    public PersonalSellingRecyclerviewAdapter(Context context) {
        mContext = context;
        mTaoList = new ArrayList<>();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        return new NormalViewHolder(LayoutInflater.from(mContext).
                inflate(R.layout.tao_item, viewGroup, false));

    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (mTaoList == null || i < 0 || i >= mTaoList.size()) {
            return;
        }
        TaoKuang data = mTaoList.get(i);

        ((NormalViewHolder) viewHolder).bindViewHolder(data);

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

    @Override
    public int getItemCount() {
        return mTaoList.size();
    }

}