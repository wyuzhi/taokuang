package com.flying.taokuang.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flying.baselib.utils.collection.CollectionUtils;
import com.flying.baselib.utils.ui.UiUtils;
import com.flying.taokuang.DetailActivity;
import com.flying.taokuang.R;
import com.flying.taokuang.entity.CollectionBean;
import com.flying.taokuang.ui.AsyncImageView;

import java.util.ArrayList;
import java.util.List;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.ViewHolder> {
    private List<CollectionBean> collections;
    private Context context;

    public CollectionAdapter(Context context) {
        this.collections = new ArrayList<>();
        this.context = context;
    }

    public CollectionAdapter(List<CollectionBean> collections, Context context) {
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
        viewHolder.price.setText("¥" + collection.getPrice());
        viewHolder.content.setText(collection.getContent());
        viewHolder.image.setUrl(collection.getImage(), UiUtils.dp2px(172), UiUtils.dp2px(227));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailActivity.go(context, collection.getGood());
            }
        });
    }

    public void addData(List<CollectionBean> data) {
        if (CollectionUtils.isEmpty(data)) {
            return;
        }
        if (CollectionUtils.isEmpty(collections)) {
            collections = data;
            notifyDataSetChanged();
            return;
        }
        int oldSize = collections.size();
        collections.addAll(data);
        notifyItemRangeInserted(oldSize, data.size());
    }

    @Override
    public int getItemCount() {
        return collections.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title, price, content;
        private AsyncImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_title);
            price = itemView.findViewById(R.id.item_price);
            image = itemView.findViewById(R.id.item_cover_img);
            content = itemView.findViewById(R.id.item_content);
        }
    }
}
