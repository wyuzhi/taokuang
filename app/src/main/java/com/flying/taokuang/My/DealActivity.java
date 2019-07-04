package com.flying.taokuang.My;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
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
    }

    @Override
    protected void onResume() {
        int id = getIntent().getIntExtra("id", 0);
        switch (id) {
            case (2):
                Fragment fragment2 = new MySoldFragment();
                FragmentManager fmanger2 = getSupportFragmentManager();
                FragmentTransaction transaction2 = fmanger2.beginTransaction();
                transaction2.replace(R.id.mViewPager, fragment2);
                transaction2.commit();
                mViewPager.setCurrentItem(1);
                break;
            case (3):
                Fragment fragment3 = new MyPurchasedFragment();
                FragmentManager fmanger3 = getSupportFragmentManager();
                FragmentTransaction transaction3 = fmanger3.beginTransaction();
                transaction3.replace(R.id.mViewPager, fragment3);
                transaction3.commit();
                mViewPager.setCurrentItem(2);
                break;
            case (4):
                Fragment fragment4 = new MyCollectionFragment();
                FragmentManager fmanger4 = getSupportFragmentManager();
                FragmentTransaction transaction4 = fmanger4.beginTransaction();
                transaction4.replace(R.id.mViewPager, fragment4);
                transaction4.commit();
                mViewPager.setCurrentItem(3);
                break;
        }
        super.onResume();
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
        mToolbar.setBackgroundColor(getResources().getColor(R.color.commonColorGrey10));
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
