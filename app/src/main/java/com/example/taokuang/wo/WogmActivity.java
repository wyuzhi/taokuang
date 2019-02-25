package com.example.taokuang.wo;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.LinearLayout;

import com.example.taokuang.Adapter.TaoAdapter;
import com.example.taokuang.R;
import com.example.taokuang.entity.TaoKuang;
import com.example.taokuang.entity.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class WogmActivity extends AppCompatActivity {
    private RecyclerView gRecyclerView;
    private TaoAdapter gAdapter;
    private LinearLayoutManager glayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wogm);
        initView();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.wo_gm_toolbar);
        toolbar.setTitle("我购买的");
        gRecyclerView = findViewById(R.id.recycler_wo_gm);
        glayoutManager = new LinearLayoutManager(this);
        gRecyclerView.setLayoutManager(glayoutManager);
        loadData();
    }

    private void loadData() {
        if (BmobUser.isLogin()) {
            BmobQuery<TaoKuang> query = new BmobQuery<>();
            query.addWhereEqualTo("goumai", BmobUser.getCurrentUser(User.class));
            query.order("-updatedAt");
            //包含作者信息
            query.include("goumai");
            query.findObjects(new FindListener<TaoKuang>() {

                @Override
                public void done(List<TaoKuang> object, BmobException e) {
                    if (e == null) {
                        gAdapter = new TaoAdapter(WogmActivity.this, object);
                        gRecyclerView.setAdapter(gAdapter);
                        Snackbar.make(gRecyclerView, "查询成功", Snackbar.LENGTH_LONG).show();
                    } else {
                        Log.e("BMOB", e.toString());
                        Snackbar.make(gRecyclerView, e.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                }

            });
        } else {
            Snackbar.make(gRecyclerView, "请先登录", Snackbar.LENGTH_LONG).show();
        }
    }
}
