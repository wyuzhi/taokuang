package com.flying.taokuang;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.flying.taokuang.Adapter.TaoAdapter;
import com.flying.taokuang.entity.TaoKuang;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class SearchActivity extends Activity {
    private RecyclerView sRecyclerView;
    private TaoAdapter sAdapter;
    private SwipeRefreshLayout sSwipeRefresh;
    private StaggeredGridLayoutManager slayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ImageView img=findViewById(R.id.img_return);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intents = getIntent();
        final String sstj =intents.getStringExtra("搜索");
        sRecyclerView = findViewById(R.id.recycler_tao);
        slayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        sRecyclerView.setLayoutManager(slayoutManager);
        sSwipeRefresh = findViewById(R.id.swipe_refresh);
       /*mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });*/
        BmobQuery<TaoKuang> tQuery = new BmobQuery<>();
        //tQuery.addWhereContains("biaoti","sstj");
        tQuery.addWhereDoesNotExists("goumai");
        tQuery.include("fabu");
        tQuery.findObjects(new FindListener<TaoKuang>() {
            @Override
            public void done(List<TaoKuang> list, BmobException e) {
                if (e == null) {
                    String key= sstj.toString().trim();
                    List<TaoKuang> datas = new ArrayList<TaoKuang>();
                    for(int i =0;i<list.size();i++){
                        String actbt = list.get(i).getBiaoti();
                        String actfb = list.get(i).getFabu().getNicheng();
                        String actms = list.get(i).getMiaoshu();
                        if( actbt.contains(key)){
                            datas.add(list.get(i));
                        }
                        else if( actfb.contains(key)){
                            datas.add(list.get(i));
                        }
                        else if(actms.contains(key)){
                            datas.add(list.get(i));
                        }
                    }

                    sAdapter = new TaoAdapter(SearchActivity.this, datas);
                    sRecyclerView.setAdapter(sAdapter);
                    //tAdapter.setOnItemClickListener(listener);
                    Log.d("查询", "查询成功" + list);
                    sSwipeRefresh.setRefreshing(false);
                } else {
                    Log.d("查询", "查询失败:" + e);
                }
            }
        });

    }
}
