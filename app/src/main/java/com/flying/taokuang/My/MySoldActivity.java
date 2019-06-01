package com.flying.taokuang.My;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.flying.baselib.utils.ui.UiUtils;
import com.flying.taokuang.Adapter.PersonalSellingRecyclerviewAdapter;
import com.flying.taokuang.R;
import com.flying.taokuang.base.BaseToolbarActivity;
import com.flying.taokuang.entity.TaoKuang;
import com.flying.taokuang.entity.User;
import com.flying.taokuang.ui.EmptyRecyclerViewHelper;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MySoldActivity extends BaseToolbarActivity {
    private RecyclerView mRecyclerView;
    private PersonalSellingRecyclerviewAdapter mAdapter;
    private EmptyRecyclerViewHelper mEmptyRecyclerViewHelper;
    private ImageView mIvBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    public int getContentViewResId() {
        return R.layout.activity_my_sold;
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
        mRecyclerView = findViewById(R.id.recycler_wo_mc);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new PersonalSellingRecyclerviewAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mEmptyRecyclerViewHelper = new EmptyRecyclerViewHelper(mRecyclerView);
        loadData();

    }

    private void loadData() {
        if (BmobUser.isLogin()) {
            BmobQuery<TaoKuang> query = new BmobQuery<>();
            query.addWhereExists("goumai");
            query.addWhereNotEqualTo("type", "1");
            query.addWhereEqualTo("fabu", BmobUser.getCurrentUser(User.class));
            query.addWhereEqualTo("jiaoyi", false);
            query.order("-updatedAt");
            query.include("fabu,goumai");
            query.findObjects(new FindListener<TaoKuang>() {

                @Override
                public void done(List<TaoKuang> object, BmobException e) {
                    if (mEmptyRecyclerViewHelper != null) {
                        mEmptyRecyclerViewHelper.checkIfEmpty();
                    }
                    if (e == null) {
                        mAdapter.addData(object);
                    }
                }

            });
        } else {
            Snackbar.make(mRecyclerView, "请先登录", Snackbar.LENGTH_LONG).show();
        }
    }
}
