package com.flying.taokuang.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.flying.taokuang.R;
import com.flying.taokuang.entity.TaoKuang;
import com.flying.taokuang.entity.User;
import com.flying.taokuang.holder.MySellViewHolder;
import com.flying.taokuang.holder.NormalViewHolder;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int NOMAL_ITEM = 9999;
    private static final int MY_SELL_ITEM = 8888;

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
                return new NormalViewHolder(LayoutInflater.from(mContext).
                        inflate(R.layout.tao_item, viewGroup, false));
            case MY_SELL_ITEM:
                return new MySellViewHolder(LayoutInflater.from(mContext).
                        inflate(R.layout.tao_item_zs, viewGroup, false));
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
                ((NormalViewHolder) viewHolder).bindViewHolder(data);
                break;
            case MY_SELL_ITEM:
                ((MySellViewHolder) viewHolder).bindViewHolder(data);
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

    @Override
    public int getItemCount() {
        return mTaoList.size();
    }

    @Override
    public int getItemViewType(int position) {
        TaoKuang taoItem = mTaoList.get(position);
        String o = taoItem.getFabu().getObjectId();
        String p = BmobUser.getCurrentUser(User.class).getObjectId();
        if (o.equals(p) && taoItem.getGoumai() == null) {
            return MY_SELL_ITEM;
        } else
            return NOMAL_ITEM;
    }
}