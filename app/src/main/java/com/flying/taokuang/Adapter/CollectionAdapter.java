package com.flying.taokuang.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flying.baselib.utils.app.LogUtils;
import com.flying.taokuang.DetailActivity;
import com.flying.taokuang.R;
import com.flying.taokuang.entity.CollectionBean;
import com.flying.taokuang.ui.AsyncImageView;

import java.util.List;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.ViewHolder> {
    private List<CollectionBean> collections;
    private Context context;

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
        final CollectionBean collection = collections.get(i);
        viewHolder.title.setText(collection.getTitle());
        viewHolder.price.setText(collection.getPrice());
        viewHolder.image.setUrl(collection.getImage());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LogUtils.d("Collection id", "onClick: " + collection.getTitle());
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
        private AsyncImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_bt);
            price = itemView.findViewById(R.id.item_jg);
            image = itemView.findViewById(R.id.item_fm);
        }
    }
}
