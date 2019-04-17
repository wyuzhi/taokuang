/*package com.example.com.flying.taokuang;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.com.flying.taokuang.Adapter.TaoAdapter;
import com.example.com.flying.taokuang.entity.TaoKuang;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class TaoActivity extends AppCompatActivity {
    private Context tC;
    private RecyclerView tRecyclerView;
    private TaoAdapter tAdapter;
    private SwipeRefreshLayout mSwipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tao);
        initView();
    }

    private void initView() {
        tRecyclerView = findViewById(R.id.recycler_tao);
        mSwipeRefresh = findViewById(R.id.swipe_refresh);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDate();
            }
        });
        loadDate();
    }

    private void loadDate() {
        BmobQuery<TaoKuang> tQuery = new BmobQuery<>();
        tQuery.findObjects(new FindListener<TaoKuang>() {
            @Override
            public void done(List<TaoKuang> list, BmobException e) {
                if (e == null) {

                    tAdapter = new TaoAdapter(tC,list);
                    tRecyclerView.setAdapter(tAdapter);
                    Log.d("查询", "查询成功"+list);
                    mSwipeRefresh.setRefreshing(false);
                } else {
                    Log.d("查询", "查询失败:" + e);
                }
            }
        });
    }
}*/
