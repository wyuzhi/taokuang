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
        initDefaultEmptyView();
    }

    public void checkIfEmpty() {
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
            }, 50);
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
    }

}
