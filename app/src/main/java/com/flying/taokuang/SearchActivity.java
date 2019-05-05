package com.flying.taokuang;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.flying.taokuang.Adapter.HomeRecyclerViewAdapter;
import com.flying.taokuang.base.BaseToolbarActivity;
import com.flying.taokuang.entity.TaoKuang;
import com.flying.taokuang.ui.EmptyRecyclerViewHelper;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class SearchActivity extends BaseToolbarActivity {
    private RecyclerView sRecyclerView;
    private HomeRecyclerViewAdapter sAdapter;
    private SwipeRefreshLayout sSwipeRefresh;
    private StaggeredGridLayoutManager slayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        ImageView img = findViewById(R.id.img_return);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intents = getIntent();
        final String sstj = intents.getStringExtra("搜索");
        sRecyclerView = findViewById(R.id.recycler_tao);
        sSwipeRefresh = findViewById(R.id.swipe_refresh);
        slayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        sRecyclerView.setLayoutManager(slayoutManager);
        sAdapter = new HomeRecyclerViewAdapter(SearchActivity.this);
        sRecyclerView.setAdapter(sAdapter);
        sRecyclerView.setHasFixedSize(true);
        EmptyRecyclerViewHelper.with(sRecyclerView);
        BmobQuery<TaoKuang> tQuery = new BmobQuery<>();
        tQuery.addWhereDoesNotExists("goumai");
        tQuery.include("fabu");
        tQuery.findObjects(new FindListener<TaoKuang>() {
            @Override
            public void done(List<TaoKuang> list, BmobException e) {
                if (e == null) {
                    String key = sstj.toString().trim();
                    List<TaoKuang> datas = new ArrayList<TaoKuang>();
                    for (int i = 0; i < list.size(); i++) {
                        String actbt = list.get(i).getBiaoti();
                        String actfb = list.get(i).getFabu().getNicheng();
                        String actms = list.get(i).getMiaoshu();
                        if (actbt.contains(key)) {
                            datas.add(list.get(i));
                        } else if (actfb.contains(key)) {
                            datas.add(list.get(i));
                        } else if (actms.contains(key)) {
                            datas.add(list.get(i));
                        }
                    }
                    sAdapter.addData(datas);
                    sSwipeRefresh.setRefreshing(false);
                }
            }
        });

    }

    @Override
    public int getContentViewResId() {
        return R.layout.activity_search;
    }
}
