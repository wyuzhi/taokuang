package com.flying.taokuang.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flying.taokuang.R;
import com.flying.taokuang.entity.Comment;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    public List<Comment> mList;
    private Context mContext;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    //public List<String> zList = new ArrayList<>();


    public CommentAdapter(Context context, List<Comment> list) {
        mContext = context;
        mList = list;
    }


    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.comment_item, viewGroup, false);
        CommentAdapter.ViewHolder viewHolder = new CommentAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Comment comment = mList.get(i);
        viewHolder.evaluator.setText(comment.getAuthor().getNicheng());
        viewHolder.content.setText(comment.getContent());
        viewHolder.time.setText(comment.getCreatedAt());

    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView evaluator;
        private TextView content;
        private TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            evaluator = itemView.findViewById(R.id.comment_evaluator);
            content = itemView.findViewById(R.id.comment_content);
            time = itemView.findViewById(R.id.comment_time);
        }
    }
}
