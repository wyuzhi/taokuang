package com.flying.taokuang.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.flying.taokuang.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ViewHolder> {
    public List<String> mList = new ArrayList<>();
    private Context mContext;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    //public List<String> zList = new ArrayList<>();

    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.hdb)// 设置图片下载期间显示的图片
            .showImageForEmptyUri(R.drawable.hdb)// 设置图片Uri为空或是错误的时候显示的图片
            .showImageOnFail(R.drawable.hdb)// 设置图片加载或解码过程中发生错误显示的图片
            .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
            .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
            .resetViewBeforeLoading(true)
            .considerExifParams(true)
            // 设置成圆角图片
            .build();// 创建DisplayImageOptions对象

    public DetailAdapter(Context context,List<String>list){
        mContext = context;
        mList = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.img_item,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {



        imageLoader.displayImage(mList.get(position), viewHolder.img,options);
        //Glide.with(mContext).load(new File(mList.get(position))).into(viewHolder.img);

    }


    @Override
    public int getItemCount() {
        return mList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.detail_im);
        }
    }
}