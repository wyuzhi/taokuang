package com.flying.taokuang.My;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.flying.taokuang.Fragment.MyCollectionFragment;
import com.flying.taokuang.Fragment.MyPurchasedFragment;
import com.flying.taokuang.Fragment.MySellingFragment;
import com.flying.taokuang.Fragment.MySoldFragment;
import com.flying.taokuang.R;
import com.flying.taokuang.base.BaseToolbarActivity;

import java.util.ArrayList;
import java.util.List;

public class DealActivity extends BaseToolbarActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private List<String> mTitle;
    private List<Fragment> mFragment;
    private ImageView mIvBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
        goInto();
    }

    private boolean goInto() {
        int id = getIntent().getIntExtra("id", 0);
        switch (id){
            case (2):
                mViewPager.setCurrentItem(1);
                return true;
            case(3):
                mViewPager.setCurrentItem(2);
                return true;
            case (4):
                mViewPager.setCurrentItem(3);
                return true;
        }
        return false;
    }

    private void initData() {
        mTitle = new ArrayList<>();
        mTitle.add("在售");
        mTitle.add("卖出");
        mTitle.add("买到");
        mTitle.add("收藏");

        mFragment = new ArrayList<>();
        mFragment.add(new MySellingFragment());
        mFragment.add(new MySoldFragment());
        mFragment.add(new MyPurchasedFragment());
        mFragment.add(new MyCollectionFragment());
    }

    private void initView() {
//        mToolbar.setBackgroundColor(getResources
//
//        ().getColor(R.color.commonColorGrey8));
        mTabLayout = findViewById(R.id.mTabLayout);
        mViewPager = findViewById(R.id.mViewPager);
        mIvBack = findViewById(R.id.img_return);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //预加载
        mViewPager.setOffscreenPageLimit(mFragment.size());
        //mViewPager滑动监听
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        //设置适配器
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            //选中的item
            @Override
            public Fragment getItem(int i) {
                return mFragment.get(i);
            }

            //返回item的个数
            @Override
            public int getCount() {
                return mFragment.size();
            }

            //设置标题
            @Override
            public CharSequence getPageTitle(int position) {
                return mTitle.get(position);
            }
        });
        //绑定
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public int getContentViewResId() {
        return R.layout.activity_deal;
    }
}
