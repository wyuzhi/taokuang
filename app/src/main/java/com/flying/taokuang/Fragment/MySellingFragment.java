package com.flying.taokuang.Fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flying.baselib.utils.app.MainThread;
import com.flying.baselib.utils.collection.CollectionUtils;
import com.flying.baselib.utils.ui.ToastUtils;
import com.flying.taokuang.Adapter.SellRecyclerViewAdapter;
import com.flying.taokuang.R;
import com.flying.taokuang.entity.TaoKuang;
import com.flying.taokuang.entity.User;
import com.flying.taokuang.ui.EmptyRecyclerViewHelper;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MySellingFragment extends Fragment implements PullLoadMoreRecyclerView.PullLoadMoreListener {
    private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;
    private RecyclerView fRecyclerView;
    private SellRecyclerViewAdapter mAdapter;
    private EmptyRecyclerViewHelper mEmptyRecyclerViewHelper;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_selling_fragment, container, false);
        initView(view);
        return view;
    }
//    @Override
//    public int getContentViewResId() {
//        return R.layout.activity_my_selling;
//    }

    private void initView(View view) {
        mPullLoadMoreRecyclerView = view.findViewById(R.id.pullLoadMoreRecyclerView);
        fRecyclerView = mPullLoadMoreRecyclerView.getRecyclerView();
        mPullLoadMoreRecyclerView.setRefreshing(true);
        mPullLoadMoreRecyclerView.getFooterViewLayout().setVisibility(View.GONE);
        mPullLoadMoreRecyclerView.setLinearLayout();

        mPullLoadMoreRecyclerView.setOnPullLoadMoreListener(this);
        mAdapter = new SellRecyclerViewAdapter(getContext());
        mPullLoadMoreRecyclerView.setAdapter(mAdapter);
        fRecyclerView.setHasFixedSize(true);
        mEmptyRecyclerViewHelper = new EmptyRecyclerViewHelper(fRecyclerView);
        loadData();
    }

    private void loadData() {
        if (BmobUser.isLogin()) {
            BmobQuery<TaoKuang> query = new BmobQuery<>();
            query.addWhereDoesNotExists("goumai");
            query.addWhereEqualTo("fabu", BmobUser.getCurrentUser(User.class));
            query.order("-updatedAt");
            query.include("fabu");
            query.findObjects(new FindListener<TaoKuang>() {

                @Override
                public void done(List<TaoKuang> object, BmobException e) {
                    if (mEmptyRecyclerViewHelper != null) {
                        mEmptyRecyclerViewHelper.checkIfEmpty();
                    }
                    if (!CollectionUtils.isEmpty(object)) {
                        mAdapter.addData(object);
                    }
                    if (e != null) {
                        ToastUtils.show("没有数据了...");
                    }
                    MainThread.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                        }
                    }, 600);
                }

            });
        } else {
            Snackbar.make(fRecyclerView, "请先登录", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRefresh() {
        mAdapter.clearData();
        loadData();
    }

    @Override
    public void onLoadMore() {
    }

}

