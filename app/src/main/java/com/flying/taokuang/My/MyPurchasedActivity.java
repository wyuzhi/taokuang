package com.flying.taokuang.My;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.flying.baselib.utils.app.LogUtils;
import com.flying.baselib.utils.ui.UiUtils;
import com.flying.taokuang.Adapter.PersonalSellingRecyclerviewAdapter;
import com.flying.taokuang.R;
import com.flying.taokuang.base.BaseToolbarActivity;
import com.flying.taokuang.entity.TaoKuang;
import com.flying.taokuang.entity.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MyPurchasedActivity extends BaseToolbarActivity {
    private RecyclerView gRecyclerView;
    private PersonalSellingRecyclerviewAdapter gAdapter;
    private ImageView mIvBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    public int getContentViewResId() {
        return R.layout.activity_my_purchased;
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
        //toolbar.setTitle("我购买的");
        gRecyclerView = findViewById(R.id.recycler_wo_gm);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        gRecyclerView.setLayoutManager(layoutManager);
        loadData();
    }

    private void loadData() {
        if (BmobUser.isLogin()) {
            BmobQuery<TaoKuang> query = new BmobQuery<>();
            query.addWhereEqualTo("goumai", BmobUser.getCurrentUser(User.class));
            query.addWhereEqualTo("buy", false);
            query.order("-updatedAt");
            //包含作者信息
            query.include("fabu");
            query.findObjects(new FindListener<TaoKuang>() {

                @Override
                public void done(List<TaoKuang> object, BmobException e) {
                    if (e == null) {
                        gAdapter = new PersonalSellingRecyclerviewAdapter(MyPurchasedActivity.this, object);
                        gRecyclerView.setAdapter(gAdapter);
                        //Snackbar.make(gRecyclerView, "查询成功", Snackbar.LENGTH_LONG).show();
                    } else {
                        LogUtils.e("BMOB", e.toString());
                        Snackbar.make(gRecyclerView, e.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                }

            });
        } else {
            Snackbar.make(gRecyclerView, "请先登录", Snackbar.LENGTH_LONG).show();
        }
    }
}
