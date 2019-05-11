package com.flying.taokuang;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.flying.baselib.utils.ui.UiUtils;
import com.flying.taokuang.Fragement.PersonalEvaluationFragment;
import com.flying.taokuang.Fragement.PersonalSellingFragment;
import com.flying.taokuang.entity.User;
import com.flying.taokuang.ui.AsyncImageView;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;


public class UserPageActivity extends AppCompatActivity {

    private static final String GO_USER_PAGE_TAG = "go_person_page";
    private AsyncImageView icon;

    public String mUserID;
    private TabLayout mTabLaout;
    private ViewPager mViewPager;
    private AppBarLayout mToolbar;


    public static void go(Context context, String id) {
        if (context == null || TextUtils.isEmpty(id)) {
            return;
        }
        Intent intent = new Intent(context, UserPageActivity.class);
        intent.putExtra(GO_USER_PAGE_TAG, id);
        context.startActivity(intent);
    }

    public String getUserID() {
        return mUserID;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        mToolbar = findViewById(R.id.abl_app_bar);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.commonColorGrey2));
        mToolbar.setFitsSystemWindows(true);
        ViewGroup.LayoutParams layoutParams = mToolbar.getLayoutParams();
        layoutParams.height = UiUtils.dp2px(138) + UiUtils.getStatusBarHeight(this);
        mToolbar.setLayoutParams(layoutParams);
        //从DetailActivity获得数据
        Intent intent = getIntent();
        mUserID = intent.getStringExtra(GO_USER_PAGE_TAG);
        if (TextUtils.isEmpty(mUserID)) {
            finish();
            return;
        }
        initView();
        loadData();
    }

    private void loadData() {
        BmobQuery<User> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(mUserID, new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    BmobFile i = user.getIcon();
                    if (i != null) {
                        icon.setUrl(i.getFileUrl(), UiUtils.dp2px(100), UiUtils.dp2px(100));
                    }
                }
            }
        });
    }

    private void initView() {
        mTabLaout = findViewById(R.id.tab_layout);
        icon = findViewById(R.id.icon);
        mViewPager = findViewById(R.id.viewpager_p);

        icon.setPlaceholderImage(R.drawable.ic_default_avatar);
        icon.setRoundAsCircle();
        mTabLaout.setTabMode(TabLayout.MODE_FIXED);
        FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            private String[] pageTypes = {"在售", "评价"};

            @Override
            public int getCount() {
                return pageTypes.length;
            }

            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return new PersonalSellingFragment();
                } else if (position == 1) {
                    return new PersonalEvaluationFragment();
                }
                return null;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                if (position >= 0 && position < pageTypes.length) {
                    return pageTypes[position];
                }
                return "";
            }
        };
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mTabLaout.setupWithViewPager(mViewPager);
    }
}
