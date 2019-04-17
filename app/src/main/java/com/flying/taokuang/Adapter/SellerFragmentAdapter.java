package com.flying.taokuang.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class SellerFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> sellerFragments;

    public SellerFragmentAdapter(FragmentManager fm,List<Fragment> fragments) {
        super(fm);
        this.sellerFragments=fragments;
    }

    @Override
    public Fragment getItem(int i) {
        return sellerFragments.get(i);
    }

    @Override
    public int getCount() {
        return sellerFragments.size();
    }
}

