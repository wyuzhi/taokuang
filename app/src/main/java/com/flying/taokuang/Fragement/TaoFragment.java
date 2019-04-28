package com.flying.taokuang.Fragement;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flying.taokuang.Leibie.HuaFragment;
import com.flying.taokuang.Leibie.MaFragment;
import com.flying.taokuang.Leibie.QiFragment;
import com.flying.taokuang.Leibie.QuanFragment;
import com.flying.taokuang.Leibie.SheFragment;
import com.flying.taokuang.Leibie.ShengFragment;
import com.flying.taokuang.Leibie.ShiFragment;
import com.flying.taokuang.Leibie.ShuFragment;
import com.flying.taokuang.Leibie.WenFragment;
import com.flying.taokuang.Leibie.XieFragment;
import com.flying.taokuang.Leibie.YiFragment;
import com.flying.taokuang.R;
import com.flying.taokuang.tool.BaseFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public class TaoFragment extends BaseFragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {





        View view = inflater.inflate(R.layout.activity_tao_fragment, container, false);

        ViewPager viewPager = view.findViewById(R.id.viewpager);
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getFragmentManager()
                , FragmentPagerItems.with(getContext())
                .add("全部", QuanFragment.class)
                .add("书籍", ShuFragment.class)
                .add("文具", WenFragment.class)
                .add("化妆品", HuaFragment.class)
                .add("生活", ShengFragment.class)
                .add("食品", ShiFragment.class)
                .add("数码", MaFragment.class)
                .add("外设", SheFragment.class)
                .add("服饰", YiFragment.class)
                .add("鞋子", XieFragment.class)
                .add("其他", QiFragment.class)
                .create());

        viewPager.setAdapter(adapter);
        SmartTabLayout viewPagerTab = view.findViewById(R.id.view_tab);
        viewPagerTab.setViewPager(viewPager);
        return view;
    }


}