package com.flying.taokuang.Leibie;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flying.baselib.utils.app.LogUtils;
import com.flying.taokuang.Adapter.TaoAdapter;
import com.flying.taokuang.R;
import com.flying.taokuang.entity.TaoKuang;
import com.flying.taokuang.tool.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ShuFragment extends BaseFragment {
    private RecyclerView sRecyclerView;
    private TaoAdapter sAdapter;
    private SwipeRefreshLayout sSwipeRefresh;
    //private StaggeredGridLayoutManager slayoutManager;
    private List<TaoKuang> sTaolist;
    //TaoAdapter.OnItemClickListener listener = (TaoAdapter.OnItemClickListener) getContext();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_shu_fragment, container, false);
        initView(view);
        return view;
    }

    private void initView(final View v) {
        sTaolist = new ArrayList<>();
        sRecyclerView = v.findViewById(R.id.recycler_tao);

        sSwipeRefresh = v.findViewById(R.id.swipe_refresh);
        sSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDate();
            }
        });
        loadDate();
    }

    private void loadDate() {
        BmobQuery<TaoKuang> tQuery = new BmobQuery<>();
        tQuery.addWhereEqualTo("leibie", "书籍");
        tQuery.order("-updatedAt");
        tQuery.addWhereDoesNotExists("goumai");
        tQuery.include("fabu");
        tQuery.findObjects(new FindListener<TaoKuang>() {
            @Override
            public void done(List<TaoKuang> list, BmobException e) {
                if (e == null) {
                    sTaolist = list;

                    StaggeredGridLayoutManager slayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                    sRecyclerView.setLayoutManager(slayoutManager);
                    sAdapter = new TaoAdapter(getContext(), list);
                    sRecyclerView.setAdapter(sAdapter);
                    //tAdapter.setOnItemClickListener(listener);
                    LogUtils.d("查询", "查询成功" + list);
                    sSwipeRefresh.setRefreshing(false);
                } else {
                    LogUtils.d("查询", "查询失败:" + e);
                }
            }
        });
    }

}