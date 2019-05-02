package com.flying.taokuang.Leibie;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flying.baselib.utils.app.MainThread;
import com.flying.baselib.utils.collection.CollectionUtils;
import com.flying.baselib.utils.ui.ToastUtils;
import com.flying.taokuang.Adapter.HomeRecyclerViewAdapter;
import com.flying.taokuang.R;
import com.flying.taokuang.entity.TaoKuang;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ArticleListFragment extends Fragment implements PullLoadMoreRecyclerView.PullLoadMoreListener {
    private static final int MAX_NUM_PER_PAGE = 5;
    private static final String ARTICLE_LIST_TYPE = "list_type";

    private int mSkipPages;
    private String mTypeStr;
    private HomeRecyclerViewAdapter mRecyclerViewAdapter;
    private RecyclerView mRecyclerView;
    private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;


    public static ArticleListFragment newInstance(String type) {
        ArticleListFragment fragment = new ArticleListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARTICLE_LIST_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.article_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(final View view) {
        mPullLoadMoreRecyclerView = view.findViewById(R.id.pullLoadMoreRecyclerView);
        mRecyclerView = mPullLoadMoreRecyclerView.getRecyclerView();
        mRecyclerView.setVerticalScrollBarEnabled(true);
        mPullLoadMoreRecyclerView.setRefreshing(true);
        mPullLoadMoreRecyclerView.setColorSchemeResources(R.color.commonColorYellow, R.color.commonColorOrange, R.color.commonColorBlue);
        mPullLoadMoreRecyclerView.setFooterViewText("加载中...");
        mPullLoadMoreRecyclerView.setLinearLayout();

        mPullLoadMoreRecyclerView.setOnPullLoadMoreListener(this);
        mRecyclerViewAdapter = new HomeRecyclerViewAdapter(getActivity());
        mPullLoadMoreRecyclerView.setAdapter(mRecyclerViewAdapter);

        mSkipPages = 0;
        mTypeStr = getArguments().getString(ARTICLE_LIST_TYPE);
        loadData();
    }

    private void loadData() {
        BmobQuery<TaoKuang> tQuery = new BmobQuery<>();
        tQuery.addWhereDoesNotExists("goumai");
        if (!TextUtils.isEmpty(mTypeStr)) {
            tQuery.addWhereEqualTo("leibie", mTypeStr);
        }
        tQuery.order("-updatedAt");
        tQuery.include("fabu");
        tQuery.setLimit(MAX_NUM_PER_PAGE);
        tQuery.setSkip(mSkipPages * MAX_NUM_PER_PAGE);
        tQuery.findObjects(new FindListener<TaoKuang>() {
            @Override
            public void done(List<TaoKuang> list, BmobException e) {
                if (!CollectionUtils.isEmpty(list)) {
                    mRecyclerViewAdapter.addData(list);
                    mSkipPages++;
                }
                if (e != null) {
                    ToastUtils.show("没有更多了...");
                }
                MainThread.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                    }
                }, 600);
            }
        });

    }

    @Override
    public void onRefresh() {
        mSkipPages = 0;
        mRecyclerViewAdapter.clearData();
        loadData();
    }

    @Override
    public void onLoadMore() {
        loadData();
    }
}
