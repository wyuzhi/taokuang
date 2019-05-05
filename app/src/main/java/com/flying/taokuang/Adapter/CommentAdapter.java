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
import com.flying.taokuang.R;
import com.flying.taokuang.entity.Comment;
import com.flying.taokuang.ui.AsyncImageView;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    public List<Comment> mList;
    private Context mContext;
    //public List<String> zList = new ArrayList<>();

    public CommentAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<>();
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
        viewHolder.evaluator.setText(comment.getContent());
        viewHolder.contentAuthor.setText(comment.getAuthor().getNicheng());
        viewHolder.time.setText(comment.getCreatedAt());


        if (comment.getAuthor().getIcon() == null) {
            viewHolder.mEvaluatorIc.setPlaceholderImage(R.drawable.ic_default_avatar);
        } else {
            viewHolder.mEvaluatorIc.setUrl(comment.getAuthor().getIcon().getFileUrl(), UiUtils.dp2px(36), UiUtils.dp2px(36));
        }

    }

    public void addData(List<Comment> data) {
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

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView evaluator;
        private TextView contentAuthor;
        private TextView time;
        private AsyncImageView mEvaluatorIc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            evaluator = itemView.findViewById(R.id.comment_evaluator);
            contentAuthor = itemView.findViewById(R.id.tv_comment_author);
            time = itemView.findViewById(R.id.comment_time);
            mEvaluatorIc = itemView.findViewById(R.id.comment_user_avatar);
        }
    }
}
