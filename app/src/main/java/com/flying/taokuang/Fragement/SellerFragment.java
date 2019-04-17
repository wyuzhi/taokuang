package com.flying.taokuang.Fragement;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flying.taokuang.Leibie.EvaluateFragment;
import com.flying.taokuang.R;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public class SellerFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_seller, container, false);
        ViewPager sellerViewPage = view.findViewById(R.id.viewpager_seller);
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getFragmentManager()
                , FragmentPagerItems.with(getContext())
                .add("在售", SellingFragment.class)
                .add("评价", EvaluateFragment.class).create());
        return view;
    }
}

