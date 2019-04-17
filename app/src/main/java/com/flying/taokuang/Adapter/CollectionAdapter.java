package com.flying.taokuang.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.flying.taokuang.DetailActivity;
import com.flying.taokuang.R;
import com.flying.taokuang.entity.CollectionBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.ViewHolder> {
    private List<CollectionBean> collections;
    private Context context;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public CollectionAdapter(List<CollectionBean> collections, Context context){
        this.collections = collections;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.tao_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.hdb)// 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.hdb)// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.hdb)// 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
                .resetViewBeforeLoading(true)
                .considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(20))// 设置成圆角图片
                .build();// 创建DisplayImageOptions对象
        final CollectionBean collection = collections.get(i);
        viewHolder.title.setText(collection.getTitle());
        viewHolder.price.setText(collection.getPrice());
        imageLoader.displayImage(collection.getImage(), viewHolder.image
                , options);
//        Glide.with(context).load(collection.getImage()).into(viewHolder.image);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("Collection id", "onClick: " + collection.getTitle());
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("ID", collection.getGood());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return collections.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title, price;
        private ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_bt);
            price = itemView.findViewById(R.id.item_jg);
            image = itemView.findViewById(R.id.item_fm);
        }
    }
}
