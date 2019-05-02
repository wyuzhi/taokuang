package com.flying.taokuang.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.flying.baselib.utils.app.LogUtils;
import com.flying.baselib.utils.device.NetworkUtils;
import com.flying.baselib.utils.ui.UiUtils;
import com.flying.taokuang.R;
import com.flying.taokuang.ui.AsyncImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class UploadImgAdapter extends RecyclerView.Adapter<UploadImgAdapter.ViewHolder> {
    public List<String> mList;
    private Context mContext;
    private List<String> local = new ArrayList<>();
    private List<String> remote = new ArrayList<>(0);
    private List<String> zList = new ArrayList<>();
    private int doitp = 0;
    private Boolean doit = true;


    private OnItemClickListener mOnItemClickListener;
    private M1 m1;
    private M2 m2;

    public void setM2(M2 m2) {
        this.m2 = m2;
    }

    public interface M2 {
        void onDeliteClick(List list);
    }

    public interface OnItemClickListener {
        void onItemClick(File file, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface M1 {
        void o(String url, int position);
    }

    public void setM1(M1 onItemClickListener) {
        m1 = onItemClickListener;
    }


    public UploadImgAdapter(Context context, List<String> list) {
        mContext = context;
        mList = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.upload_img_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
        final int p = mList.size();
        if (doit) {
            if (NetworkUtils.isHttpUrl(mList.get(position))) {
                remote.add(mList.get(position));
            } else {
                zList.add(mList.get(position));
                local.add(mList.get(position));
            }
            doitp++;
            if (doitp == p) {
                doit = false;
            }
        }
        LogUtils.d("IMAGE_URL", "onBindViewHolder: " + mList.get(position));
        if (NetworkUtils.isHttpUrl(mList.get(position))) {
            viewHolder.img.setUrl(mList.get(position), (int) UiUtils.dp2px(64), (int) UiUtils.dp2px(64));
        } else {
            viewHolder.img.setUrl(Uri.fromFile(new File(mList.get(position))).toString(), (int) UiUtils.dp2px(64), (int) UiUtils.dp2px(64));
        }
        viewHolder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.isHttpUrl(mList.get(position))) {
                    remote.remove(position);
                    mList.remove(position);
                    notifyItemRemoved(position);
                    m2.onDeliteClick(mList);

                } else {
                    local.remove(position - remote.size());
                    mList.remove(position);
                    zList.remove(position);
                    notifyItemRemoved(position);
                    m2.onDeliteClick(mList);

                }
                notifyDataSetChanged();
            }
        });
        viewHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.isHttpUrl(mList.get(position))) {
                    m1.o(mList.get(position), position);

                } else {
                    mOnItemClickListener.onItemClick(new File(mList.get(position)), position);
                }
            }
        });
    }

    public List<String> getImage() {
        return zList;
    }

    public List<String> getLocal() {
        return local;
    }

    public List<String> getRemote() {
        return remote;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private AsyncImageView img;
        private Button del;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.grid_img);
            del = itemView.findViewById(R.id.grid_dl);
        }
    }
}
