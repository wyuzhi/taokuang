package com.flying.taokuang.Fragement;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flying.taokuang.Leibie.ArticleListFragment;
import com.flying.taokuang.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private TabLayout mTabLaout;
    private ViewPager mViewPager;
    private List<ArticleListFragment> mFragments;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_home_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewPager = view.findViewById(R.id.view_pager);
        mTabLaout = view.findViewById(R.id.tab_layout);
        mTabLaout.setTabMode(TabLayout.MODE_SCROLLABLE);

        mFragments = new ArrayList<>();
        FragmentManager childFragmentManager = getChildFragmentManager();
        FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(childFragmentManager) {
            private String[] pageTypes = {null, "书籍", "文具", "化妆品", "生活", "食品", "数码", "外设", "服饰", "鞋子", "其他"};

            @Override
            public int getCount() {
                return pageTypes.length;
            }

            @Override
            public Fragment getItem(int position) {
                if (position >= 0 && position < pageTypes.length) {
                    ArticleListFragment fragment = ArticleListFragment.newInstance(pageTypes[position]);
                    mFragments.add(fragment);
                    return fragment;
                }
                return null;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                if (position > 0 && position < pageTypes.length) {
                    return pageTypes[position];
                }
                return "全部";
            }
        };

        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(1);
        mTabLaout.setupWithViewPager(mViewPager);
    }

    public void notifyAllFragmentsChangeStyle(boolean isLinear) {
        if (!isAdded()) {
            return;
        }
        for (ArticleListFragment fragment : mFragments) {
            fragment.updateLayoutStyle(isLinear);
        }
    }
}