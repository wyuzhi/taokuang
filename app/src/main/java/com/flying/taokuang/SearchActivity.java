package com.flying.taokuang;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
    private static final String TAG_SEARCH_KEY = "search_key";
    private RecyclerView sRecyclerView;
    private HomeRecyclerViewAdapter sAdapter;
    private EmptyRecyclerViewHelper mEmptyRecyclerViewHelper;

    public static void go(Context context, String key) {
        if (context == null || TextUtils.isEmpty(key)) {
            return;
        }
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(TAG_SEARCH_KEY, key);
        context.startActivity(intent);
    }

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
        sRecyclerView = findViewById(R.id.recycler_tao);
        sRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        sAdapter = new HomeRecyclerViewAdapter(SearchActivity.this);
        sRecyclerView.setAdapter(sAdapter);
        sRecyclerView.setHasFixedSize(true);
        mEmptyRecyclerViewHelper = new EmptyRecyclerViewHelper(sRecyclerView);

        final String key = getIntent().getStringExtra(TAG_SEARCH_KEY).trim();
        if (TextUtils.isEmpty(key)) {
            finish();
            return;

        }
        BmobQuery<TaoKuang> tQuery = new BmobQuery<>();
        tQuery.addWhereDoesNotExists("goumai");
        tQuery.include("fabu");
        tQuery.order("-updatedAt");
        tQuery.setLimit(60);

        tQuery.findObjects(new FindListener<TaoKuang>() {
            @Override
            public void done(List<TaoKuang> list, BmobException e) {
                if (e == null) {
                    List<TaoKuang> datas = new ArrayList<>();
                    for (TaoKuang bean : list) {
                        if (bean.getBiaoti().contains(key)) {
                            datas.add(bean);
                        }
                    }
                    sAdapter.addData(datas);
                }
                if (mEmptyRecyclerViewHelper != null) {
                    mEmptyRecyclerViewHelper.checkIfEmpty();
                }
            }
        });
    }

    @Override
    public int getContentViewResId() {
        return R.layout.activity_search;
    }
}
