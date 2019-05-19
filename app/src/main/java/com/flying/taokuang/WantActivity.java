package com.flying.taokuang;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.support.v7.widget.SearchView;
import android.widget.TextView;

import com.flying.baselib.utils.ui.UiUtils;
import com.flying.taokuang.Adapter.HomeRecyclerViewAdapter;
import com.flying.taokuang.base.BaseToolbarActivity;
import com.flying.taokuang.entity.TaoKuang;
import com.flying.taokuang.ui.EmptyRecyclerViewHelper;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class WantActivity extends BaseToolbarActivity {
    private RecyclerView gRecyclerView;
    private HomeRecyclerViewAdapter mAdapter;
    private ImageView mIvBack;
    private TextView tvtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
//        setSupportActionBar(mToolbar);
        initView();
    }

    @Override
    public int getContentViewResId() {
        return R.layout.activity_my_purchased;
    }

    private void initView() {
        tvtitle = findViewById(R.id.tv_title);
        tvtitle.setText("求购");
        tvtitle.setTextSize(22);
        mIvBack = findViewById(R.id.img_return);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        UiUtils.setOnTouchBackground(mIvBack);
        gRecyclerView = findViewById(R.id.recycler_wo_gm);
        gRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mAdapter = new HomeRecyclerViewAdapter(this);
        gRecyclerView.setAdapter(mAdapter);
        gRecyclerView.setHasFixedSize(true);
        EmptyRecyclerViewHelper.with(gRecyclerView);
        loadData();
    }

    private void loadData() {
        if (BmobUser.isLogin()) {
            BmobQuery<TaoKuang> query = new BmobQuery<>();
            query.addWhereEqualTo("type", "1");
            query.order("-updatedAt");
            //包含作者信息
            query.include("fabu");
            query.findObjects(new FindListener<TaoKuang>() {

                @Override
                public void done(List<TaoKuang> object, BmobException e) {
                    if (e == null) {
                        mAdapter.addData(object);
                    }
                }

            });
        } else {
            Snackbar.make(gRecyclerView, "请先登录", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu); //找到searchView
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setTitle("");
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("输入你想查找的");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(WantActivity.this, SearchActivity.class);
                intent.putExtra("搜索", query);
                intent.putExtra("ha", "ha");
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }
}