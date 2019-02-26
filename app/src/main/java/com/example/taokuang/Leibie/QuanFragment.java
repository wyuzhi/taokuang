package com.example.taokuang.Leibie;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.taokuang.Adapter.TaoAdapter;
import com.example.taokuang.R;
import com.example.taokuang.entity.TaoKuang;
import com.example.taokuang.tool.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class QuanFragment extends BaseFragment {
    private RecyclerView tRecyclerView;
    private TaoAdapter tAdapter;
    private SwipeRefreshLayout mSwipeRefresh;
    private StaggeredGridLayoutManager layoutManager;
    private List<TaoKuang> mTaolist;
    //TaoAdapter.OnItemClickListener listener = (TaoAdapter.OnItemClickListener) getContext();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_quan_fragment, container, false);
        initView(view);
        return view;
    }

    private void initView(final View v) {
        mTaolist = new ArrayList<>();
        tRecyclerView = v.findViewById(R.id.recycler_tao);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        tRecyclerView.setLayoutManager(layoutManager);
        mSwipeRefresh = v.findViewById(R.id.swipe_refresh);
       /*mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });*/
        loadDate();
    }

    private void loadDate() {
        BmobQuery<TaoKuang> tQuery = new BmobQuery<>();
        tQuery.addWhereDoesNotExists("goumai");
        tQuery.findObjects(new FindListener<TaoKuang>() {
            @Override
            public void done(List<TaoKuang> list, BmobException e) {
                if (e == null) {
                    mTaolist = list;
                    tAdapter = new TaoAdapter(getContext(), list);
                    tRecyclerView.setAdapter(tAdapter);
                    //tAdapter.setOnItemClickListener(listener);
                    Log.d("查询", "查询成功" + list);
                    mSwipeRefresh.setRefreshing(false);
                } else {
                    Log.d("查询", "查询失败:" + e);
                }
            }
        });
    }


    /*@Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(getContext(),DetailActivity.class);
        TaoKuang taoItem = mTaolist.get(position);
        detailIntent.putExtra("1图片",taoItem.getPicyi().getFileUrl());
        detailIntent.putExtra("2图片",taoItem.getPicyi().getFileUrl());
        detailIntent.putExtra("3图片",taoItem.getPicyi().getFileUrl());
        detailIntent.putExtra("价格",taoItem.getJiage());
        detailIntent.putExtra("标题",taoItem.getBiaoti());
        detailIntent.putExtra("描述",taoItem.getMiaoshu());
        detailIntent.putExtra("位置",taoItem.getWeizhi());

        startActivity(detailIntent);

    }*/
}
