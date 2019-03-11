package com.example.taokuang.wo;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Window;

import com.example.taokuang.Adapter.TaoAdapter;
import com.example.taokuang.R;
import com.example.taokuang.entity.TaoKuang;
import com.example.taokuang.entity.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class WofbActivity extends Activity {
    private RecyclerView fRecyclerView;
    private TaoAdapter fAdapter;
    private LinearLayoutManager flayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wofb);
        initView();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.wo_fb_toolbar);
        //toolbar.setTitle("我发布的");
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
                        fAdapter = new TaoAdapter(WofbActivity.this, object);
                        fRecyclerView.setAdapter(fAdapter);
                        //Snackbar.make(fRecyclerView, "查询成功", Snackbar.LENGTH_LONG).show();
                    } else {
                        Log.e("BMOB", e.toString());
                        Snackbar.make(fRecyclerView, e.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                }

            });
        } else {
            Snackbar.make(fRecyclerView, "请先登录", Snackbar.LENGTH_LONG).show();
        }
    }
}
