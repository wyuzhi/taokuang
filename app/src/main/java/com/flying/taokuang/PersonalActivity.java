package com.flying.taokuang;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.flying.taokuang.Fragement.EvaluationFragment;
import com.flying.taokuang.Fragement.SellingFragment;
import com.flying.taokuang.entity.User;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class PersonalActivity extends AppCompatActivity {
    private ImageView icon;
    private CollapsingToolbarLayout collapsingToolbar;

    public String fabuID;
    public String goumaiID;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.hdb)// 设置图片下载期间显示的图片
            .showImageForEmptyUri(R.drawable.hdb)// 设置图片Uri为空或是错误的时候显示的图片
            .showImageOnFail(R.drawable.hdb)// 设置图片加载或解码过程中发生错误显示的图片
            .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
            .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
//            .displayer(new RoundedBitmapDisplayer(20))// 设置成圆角图片
            .build();// 创建DisplayImageOptions对象

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

        imageLoader.displayImage("drawable://" + R.drawable.hdb, icon, options);
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
                    // 创建DisplayImageOptions对象并进行相关选项配置

                    if (i != null) {
                        // 使用ImageLoader加载图片
                        imageLoader.displayImage(i.getFileUrl(), icon, options);
                    }


                } else {
                }
            }
        });
    }

    private void initView() {
        icon = findViewById(R.id.icon);
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
