package com.flying.taokuang.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flying.baselib.utils.ui.IdUtils;
import com.flying.baselib.utils.ui.UiUtils;
import com.flying.taokuang.R;

public class EmptyRecyclerViewHelper {

    private View mEmptyView;
    private Context mContext;
    private RecyclerView mRecyclerView;

    public EmptyRecyclerViewHelper(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        mContext = mRecyclerView.getContext();
    }

    public static void with(RecyclerView recyclerView) {
        if (recyclerView == null) {
            return;
        }
        EmptyRecyclerViewHelper helper = new EmptyRecyclerViewHelper(recyclerView);
        helper.initDefaultEmptyView();
        helper.setAdapterDataObserver();
    }

    private void checkIfEmptyDelayed(int millis) {
        if (mEmptyView != null && mRecyclerView != null && mRecyclerView.getAdapter() != null) {
            mRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mRecyclerView == null || mEmptyView == null) {
                        return;
                    }
                    boolean emptyViewVisible = mRecyclerView.getAdapter().getItemCount() == 0;
                    mEmptyView.setVisibility(emptyViewVisible ? View.VISIBLE : View.GONE);
                    mRecyclerView.setVisibility(emptyViewVisible ? View.GONE : View.VISIBLE);
                }
            }, millis);
        }
    }

    private void setAdapterDataObserver() {
        if (mRecyclerView != null && mRecyclerView.getAdapter() != null) {
            mRecyclerView.getAdapter().registerAdapterDataObserver(observer);
        }
    }

    private void initDefaultEmptyView() {
        if (mEmptyView != null) {
            return;
        }
        RelativeLayout emptyRootView = new RelativeLayout(mContext);
        emptyRootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        TextView textView = new TextView(mContext);
        textView.setText(R.string.empty_data_tips);
        textView.setId(IdUtils.generateViewId());
        textView.setTextColor(mContext.getResources().getColor(R.color.commonColorBlue));
        textView.setTextSize(16);
        RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        textParams.bottomMargin = UiUtils.dp2px(50);
        textView.setLayoutParams(textParams);
        emptyRootView.addView(textView);

        ImageView imageView = new ImageView(mContext);
        imageView.setImageResource(R.drawable.ic_data_empty);
        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imageParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        imageParams.addRule(RelativeLayout.ABOVE, textView.getId());
        imageParams.bottomMargin = UiUtils.dp2px(20);
        imageView.setLayoutParams(imageParams);
        emptyRootView.addView(imageView);

        mEmptyView = emptyRootView;
        mEmptyView.setVisibility(View.GONE);
        ViewParent parent = mRecyclerView.getParent();
        if (parent instanceof ViewGroup) {
            ((ViewGroup) parent).addView(mEmptyView);
        }
        if (parent instanceof LinearLayout) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mEmptyView.getLayoutParams();
            lp.height = -1;
            lp.gravity = Gravity.CENTER;
            mEmptyView.setLayoutParams(lp);
        } else if (parent instanceof RelativeLayout) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mEmptyView.getLayoutParams();
            lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            mEmptyView.setLayoutParams(lp);
        } else if (parent instanceof FrameLayout) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mEmptyView.getLayoutParams();
            lp.height = -1;
            lp.gravity = Gravity.CENTER;
            mEmptyView.setLayoutParams(lp);
        }
        //fragment、activity初始化会调用,延时2.5s 等待请求结果
        checkIfEmptyDelayed(2500);
    }

    final private RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            checkIfEmptyDelayed(10);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            checkIfEmptyDelayed(10);

        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            checkIfEmptyDelayed(10);
        }
    };
}
