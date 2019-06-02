package com.flying.taokuang.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.flying.baselib.utils.collection.CollectionUtils;
import com.flying.baselib.utils.device.NetworkUtils;
import com.flying.baselib.utils.ui.UiUtils;
import com.flying.taokuang.R;
import com.flying.taokuang.ui.AsyncImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UploadImgAdapter extends RecyclerView.Adapter<UploadImgAdapter.ViewHolder> {
    private List<String> mList;
    private Context mContext;

    public UploadImgAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.upload_img_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
        if (NetworkUtils.isHttpUrl(mList.get(position))) {
            viewHolder.img.setUrl(mList.get(position), (int) UiUtils.dp2px(64), (int) UiUtils.dp2px(64));
        } else {
            viewHolder.img.setUrl(Uri.fromFile(new File(mList.get(position))).toString(), (int) UiUtils.dp2px(64), (int) UiUtils.dp2px(64));
        }
        viewHolder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mList.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    public List<String> getLocal() {
        List<String> local = new ArrayList<>();
        for (String url : mList) {
            if (!NetworkUtils.isHttpUrl(url)) {
                local.add(url);
            }
        }
        return local;
    }

    public List<String> getRemote() {
        List<String> local = new ArrayList<>();
        for (String url : mList) {
            if (NetworkUtils.isHttpUrl(url)) {
                local.add(url);
            }
        }
        return local;
    }

    public List<String> getData() {
        return mList;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void addData(List data) {
        if (CollectionUtils.isEmpty(data)) {
            return;
        }
        if (CollectionUtils.isEmpty(mList)) {
            mList = data;
            notifyDataSetChanged();
            return;
        }
        int oldSize = mList.size();
        mList.addAll(data);
        notifyItemRangeInserted(oldSize, data.size());

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private AsyncImageView img;
        private ImageView del;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.grid_img);
            del = itemView.findViewById(R.id.grid_dl);
        }
    }
}
