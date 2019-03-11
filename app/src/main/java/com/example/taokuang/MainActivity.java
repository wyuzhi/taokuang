package com.example.taokuang;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.taokuang.Adapter.FragmentAdapter;
import com.example.taokuang.Fragement.TaoFragment;
import com.example.taokuang.Fragement.WoFragment;
import com.pgyersdk.update.PgyUpdateManager;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FetchUserInfoListener;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;



public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private List<Fragment> fragments = new ArrayList<>();
    private ViewPager viewPager;
    private int ret;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_dashboard:
                    Intent intent = new Intent(getBaseContext(), FaBuActivity.class);
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
        setContentView(R.layout.activity_main);
        Bmob.initialize(this,"7c28cec5766e668a48a5ea7d719d8e08");
        fetchUserInfo();
        checkUpdate();
        if(BmobUser.isLogin()){
            initView();//页面布局初始化
        }
        else {Intent intentl =new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intentl);
        }
        // 申请权限
        PermissionGen.with(MainActivity.this).addRequestCode(100)
                .permissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        Manifest.permission.REQUEST_INSTALL_PACKAGES,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .request();


    }
    private void fetchUserInfo() {
        BmobUser.fetchUserInfo(new FetchUserInfoListener<BmobUser>() {
            @Override
            public void done(BmobUser user, BmobException e) {
                if (e == null) {
                    //final User myUser = BmobUser.getCurrentUser(User.class);
                    //Toast.makeText(MainActivity.this, "更新用户本地缓存信息成功",
                     //       Toast.LENGTH_SHORT).show();
                } else {
                    Runtime runtime = Runtime.getRuntime();
                    try {
                        Process p = runtime.exec("ping -c 3 www.baidu.com");
                        ret = p.waitFor();
                        Log.i("Avalible", "Process:"+ret);
                    } catch (Exception o) {
                        o.printStackTrace();
                    }
                    if (ret == 0){
                        Toast.makeText(MainActivity.this, e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        Log.e("error",e.getMessage());
                        BmobUser.logOut();
                    }
                    else Toast.makeText(MainActivity.this, "请检查网络状况",
                            Toast.LENGTH_SHORT).show();




                }
            }
        });
    }

    private void checkUpdate() {

        new PgyUpdateManager.Builder()
                .setForced(true)//设置是否强制提示更新
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

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("淘矿");
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.view_pager);
        //分类导航栏

        //
        final BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //向viewpager中添加页面
        fragments.add(new TaoFragment());
        fragments.add(new WoFragment());
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu); //找到searchView
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("输入你想查找的");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("搜索", query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }


}