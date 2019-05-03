package com.flying.taokuang;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.flying.baselib.utils.app.ApplicationUtils;
import com.flying.baselib.utils.app.LogUtils;
import com.flying.baselib.utils.app.MainThread;
import com.flying.baselib.utils.device.NetworkUtils;
import com.flying.taokuang.Adapter.FragmentAdapter;
import com.flying.taokuang.Fragement.HomeFragment;
import com.flying.taokuang.Fragement.MyFragment;
import com.flying.taokuang.base.BaseToolbarActivity;
import com.pgyersdk.feedback.PgyerFeedbackManager;
import com.pgyersdk.update.PgyUpdateManager;
import com.tendcloud.tenddata.TCAgent;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FetchUserInfoListener;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;


public class MainActivity extends BaseToolbarActivity {
    private List<Fragment> fragments = new ArrayList<>();
    private ViewPager viewPager;
    private View search;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_dashboard:
                    Intent intent = new Intent(getBaseContext(), ReleaseActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_notifications:
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!BmobUser.isLogin()) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        initView();
        delayInit();
        search = findViewById(R.id.search_layout);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GoSearchActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public int getContentViewResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onPause() {
        super.onPause();
        TCAgent.onPageEnd(this, "主页");
    }

    @Override
    protected void onResume() {
        super.onResume();
        TCAgent.onPageStart(this, "主页");
    }

    private void delayInit() {
        //不是很重要的操作,优先让主线程绘制UI
        MainThread.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (NetworkUtils.isNetworkConnected(ApplicationUtils.getApplication())) {
                    fetchUserInfo();
                    checkUpdate();
                }
                new PgyerFeedbackManager.PgyerFeedbackBuilder().builder().register();
                PermissionGen.with(MainActivity.this).addRequestCode(100)
                        .permissions(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA)
                        .request();
            }
        }, 6000);
    }

    private void fetchUserInfo() {
        BmobUser.fetchUserInfo(new FetchUserInfoListener<BmobUser>() {
            @Override
            public void done(BmobUser user, BmobException e) {
                if (e != null) {
                    LogUtils.d(e.toString());
                }
            }
        });
    }

    private void checkUpdate() {

        new PgyUpdateManager.Builder()
                .setForced(false)//设置是否强制提示更新
                // v3.0.4+ 以上同时可以在官网设置强制更新最高低版本；网站设置和代码设置一种情况成立则提示强制更新
                .setUserCanRetry(false)//失败后是否提示重新下载
                .setDeleteHistroyApk(false)     // 检查更新前是否删除本地历史 Apk， 默认为true
                .register();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @PermissionSuccess(requestCode = 100)
    public void doSomething() {
        //Toast.makeText(this, "已授权", Toast.LENGTH_SHORT).show();
    }

    @PermissionFail(requestCode = 100)
    public void doFailSomething() {
        //Toast.makeText(MainActivity.this, "将", Toast.LENGTH_LONG).show();
    }


    private void initView() {
        // mToolbar.setTitle("淘矿");
        setSupportActionBar(mToolbar);
        viewPager = findViewById(R.id.view_pager);
        //分类导航栏

        //
        final BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //向viewpager中添加页面
        fragments.add(new HomeFragment());
        fragments.add(new MyFragment());
        FragmentAdapter myAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(myAdapter);
        viewPager.setOffscreenPageLimit(2);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i == 1) {
                    i++;
                }

                navigation.getMenu().getItem(i).setChecked(true);

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

}