package com.flying.taokuang.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.flying.taokuang.Fragment.HomeFragment;
import com.flying.taokuang.Fragment.MyFragment;

import java.util.List;

public class FragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;

    public FragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = null;
        if (position == 0) {
            f = new HomeFragment();
        } else if (position == 1) {
            f = new MyFragment();
        }
        fragments.add(f);
        return f;
    }

    @Override
    public int getCount() {
        return 2;
    }
}

