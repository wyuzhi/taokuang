package com.flying.taokuang;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.flying.taokuang.Fragement.EvaluationFragment;
import com.flying.taokuang.Fragement.SellingFragment;
import com.flying.taokuang.entity.User;
import com.flying.taokuang.ui.AsyncImageView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class PersonalActivity extends AppCompatActivity {
    private AsyncImageView icon;
    private CollapsingToolbarLayout collapsingToolbar;

    public String fabuID;
    public String goumaiID;

    public String getFabuID() {
        return fabuID;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing);
        collapsingToolbar.setCollapsedTitleTypeface(Typeface.DEFAULT_BOLD);
        //从DetailActivity获得数据
        Intent intent = getIntent();
        fabuID = intent.getStringExtra("发布");
        goumaiID = intent.getStringExtra("购买");
        initView();
        loadData();
    }

    private void loadData() {
        BmobQuery<User> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(fabuID, new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    collapsingToolbar.setTitle(user.getNicheng());
                    BmobFile i = user.getIcon();
                    if (i != null) {
                        icon.setUrl(i.getFileUrl());
                    }
                }
            }
        });
    }

    private void initView() {
        icon = findViewById(R.id.icon);
        icon.setPlaceholderImage(R.drawable.ic_default_avatar);
        icon.setRoundAsCircle();
        collapsingToolbar = findViewById(R.id.collapsing);
        collapsingToolbar.setCollapsedTitleTypeface(Typeface.DEFAULT_BOLD);
        ViewPager viewPager = findViewById(R.id.viewpager_p);
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getSupportFragmentManager()
                , FragmentPagerItems.with(this)
                .add("在售", SellingFragment.class)
                .add("评价", EvaluationFragment.class)
                .create());
        viewPager.setAdapter(adapter);
        SmartTabLayout viewPagerTab = findViewById(R.id.view_tab_p);
        viewPagerTab.setViewPager(viewPager);

    }
}
