package com.flying.taokuang.My;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.flying.baselib.utils.ui.UiUtils;
import com.flying.taokuang.Adapter.CollectionAdapter;
import com.flying.taokuang.R;
import com.flying.taokuang.base.BaseToolbarActivity;
import com.flying.taokuang.entity.CollectionBean;
import com.flying.taokuang.ui.EmptyRecyclerViewHelper;

import org.litepal.LitePal;

import java.util.List;

public class MyCollectionActivity extends BaseToolbarActivity {
    private RecyclerView mRecyclerView;
    private CollectionAdapter mAdapter;
    private LinearLayoutManager mlayoutManager;
    private ImageView mIvBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    public int getContentViewResId() {
        return R.layout.activity_my_collection;
    }

    private void initView() {
        mIvBack = findViewById(R.id.img_return);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        UiUtils.setOnTouchBackground(mIvBack);
        mRecyclerView = findViewById(R.id.recycler_wo_sc);
        mlayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mlayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new CollectionAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        EmptyRecyclerViewHelper.with(mRecyclerView);
        loadData();
    }

    private void loadData() {
        List<CollectionBean> collections = LitePal.findAll(CollectionBean.class);
        mAdapter.addData(collections);
    }
}

