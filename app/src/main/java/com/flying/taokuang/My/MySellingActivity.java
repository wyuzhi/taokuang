package com.flying.taokuang.My;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.flying.baselib.utils.ui.UiUtils;

import com.flying.baselib.utils.app.MainThread;
import com.flying.baselib.utils.collection.CollectionUtils;
import com.flying.baselib.utils.ui.ToastUtils;
import com.flying.taokuang.Adapter.SellRecyclerViewAdapter;
import com.flying.taokuang.R;
import com.flying.taokuang.entity.TaoKuang;
import com.flying.taokuang.entity.User;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MySellingActivity extends Activity implements PullLoadMoreRecyclerView.PullLoadMoreListener {
    private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;
    private RecyclerView fRecyclerView;
    private SellRecyclerViewAdapter mAdapter;
    private ImageView mIvBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_selling);
        initView();
    }

    private void initView() {
        mIvBack = findViewById(R.id.img_return);
        mPullLoadMoreRecyclerView = findViewById(R.id.pullLoadMoreRecyclerView);
        fRecyclerView = mPullLoadMoreRecyclerView.getRecyclerView();
        mPullLoadMoreRecyclerView.setRefreshing(true);
        mPullLoadMoreRecyclerView.getFooterViewLayout().setVisibility(View.GONE);
        mPullLoadMoreRecyclerView.setLinearLayout();

        mPullLoadMoreRecyclerView.setOnPullLoadMoreListener(this);
        mAdapter = new SellRecyclerViewAdapter(this);
        mPullLoadMoreRecyclerView.setAdapter(mAdapter);

        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        UiUtils.setOnTouchBackground(mIvBack);
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
                    if (!CollectionUtils.isEmpty(object)) {
                        mAdapter.addData(object);
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
