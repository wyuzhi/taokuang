package com.flying.taokuang.wo;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.flying.baselib.utils.app.LogUtils;
import com.flying.baselib.utils.ui.UiUtils;
import com.flying.taokuang.Adapter.HomeRecyclerViewAdapter;
import com.flying.taokuang.R;
import com.flying.taokuang.entity.TaoKuang;
import com.flying.taokuang.entity.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class WofbActivity extends Activity {
    private RecyclerView fRecyclerView;
    private HomeRecyclerViewAdapter fAdapter;
    private LinearLayoutManager flayoutManager;
    private ImageView mIvBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wofb);
        initView();
    }

    private void initView() {
        mIvBack=findViewById(R.id.img_return);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        UiUtils.setOnTouchBackground(mIvBack);
        fRecyclerView = findViewById(R.id.recycler_wo_fb);
        flayoutManager = new LinearLayoutManager(this);
        fRecyclerView.setLayoutManager(flayoutManager);
        loadData();
    }

    private void loadData() {
        if (BmobUser.isLogin()) {
            BmobQuery<TaoKuang> query = new BmobQuery<>();
            query.addWhereDoesNotExists("goumai");
            query.addWhereEqualTo("fabu", BmobUser.getCurrentUser(User.class));
            query.order("-updatedAt");
            //包含作者信息
            query.include("fabu");
            query.findObjects(new FindListener<TaoKuang>() {

                @Override
                public void done(List<TaoKuang> object, BmobException e) {
                    if (e == null) {
                        fAdapter = new HomeRecyclerViewAdapter(WofbActivity.this, object);
                        fRecyclerView.setAdapter(fAdapter);
                        //Snackbar.make(fRecyclerView, "查询成功", Snackbar.LENGTH_LONG).show();
                    } else {
                        LogUtils.e("BMOB", e.toString());
                        Snackbar.make(fRecyclerView, e.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                }

            });
        } else {
            Snackbar.make(fRecyclerView, "请先登录", Snackbar.LENGTH_LONG).show();
        }
    }
}
