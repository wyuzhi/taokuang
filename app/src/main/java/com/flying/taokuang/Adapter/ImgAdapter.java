package com.flying.taokuang.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.flying.baselib.utils.app.LogUtils;
import com.flying.taokuang.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class ImgAdapter extends RecyclerView.Adapter<ImgAdapter.ViewHolder> {
    public List<String> mList;
    private Context mContext;
    private List<String> local = new ArrayList<>();
    private List<String> remote = new ArrayList<>(0);
    private List<String> zList = new ArrayList<>();
    private int doitp = 0;
    private Boolean doit=true;


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


    public ImgAdapter(Context context, List<String> list) {
        mContext = context;
        mList = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.grid_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
        final int p = mList.size();
        if (doit) {
            if (mList.get(position).startsWith("http") || mList.get(position).startsWith("https")) {
                remote.add(mList.get(position));
            } else {
                //压缩图片
                Luban.with(mContext)
                        .load(mList.get(position))
                        //.ignoreBy(100)
                        //.setTargetDir(file_pathss)
                        .filter(new CompressionPredicate() {
                            @Override
                            public boolean apply(String path) {
                                return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                            }
                        })
                        .setCompressListener(new OnCompressListener() {
                            @Override
                            public void onStart() {
                            }

                            @Override
                            public void onSuccess(File file) {
                                zList.add(file.getPath());
                                local.add(file.getPath());
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        }).launch();
            }
            doitp++;
            if (doitp==p){doit=false;}
        }
        LogUtils.d("IMAGE_URL", "onBindViewHolder: " + mList.get(position));
//        imageLoader.displayImage(mList.get(position), viewHolder.img,options);
        if (mList.get(position).startsWith("http") || mList.get(position).startsWith("https")) {
            Glide.with(mContext).load(mList.get(position)).into(viewHolder.img);
        } else {
            Glide.with(mContext).load(new File(mList.get(position))).into(viewHolder.img);
        }
        viewHolder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mList.get(position).startsWith("http") || mList.get(position).startsWith("https")) {
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

//回调接口

//                notifyDataSetChanged();
            }
        });
        viewHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mList.get(position).startsWith("http") || mList.get(position).startsWith("https")) {
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
        private ImageView img;
        private Button del;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.grid_img);
            del = itemView.findViewById(R.id.grid_dl);
        }
    }
}
