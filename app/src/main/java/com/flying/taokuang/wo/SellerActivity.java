package com.flying.taokuang.wo;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.flying.taokuang.Adapter.SellerFragmentAdapter;
import com.flying.taokuang.Leibie.EvaluateFragment;
import com.flying.taokuang.R;

import java.util.ArrayList;
import java.util.List;

public class SellerActivity extends AppCompatActivity {
    private List<Fragment> sellerfragments = new ArrayList<>();
    private TabLayout sellerTab;
    private Toolbar sellerToolbar;
    private CollapsingToolbarLayout sellerCollapsingToolbarLayout;
    private ImageView sellerImageView;
    private TextView name;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);
        initUI();
        name=findViewById(R.id.name);
        name.setAlpha(0f);
    }


    private void initUI() {
        ViewPager sellerViewPager = findViewById(R.id.viewpager_seller);
        sellerTab=findViewById(R.id.tab_seller);
        sellerToolbar=findViewById(R.id.toolbar_seller);
        sellerImageView=findViewById(R.id.imge_seller);
        sellerCollapsingToolbarLayout=findViewById(R.id.collapsing_toolbar_seller);
        sellerTab.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        sellerfragments.add(new EvaluateFragment());
//        sellerfragments.add(new SellingFragment());
        SellerFragmentAdapter mSellerFragmentAdapter=new SellerFragmentAdapter(getSupportFragmentManager(),sellerfragments);
        sellerViewPager.setAdapter(mSellerFragmentAdapter);
        sellerViewPager.setOffscreenPageLimit(1);
        sellerViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        sellerTab.setupWithViewPager(sellerViewPager);

    }
}

