package com.flying.taokuang.wo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.flying.baselib.utils.app.LogUtils;
import com.flying.taokuang.R;

public class AboutActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView version = findViewById(R.id.versioncode);
        version.setText("当前版本： V " + getLocalVersionName(this));


        final Button btn = findViewById(R.id.share);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == btn){
                    Intent textIntent = new Intent(Intent.ACTION_SEND);
                    textIntent.setType("text/plain");
                    textIntent.putExtra(Intent.EXTRA_TEXT, "下载地址：https://www.pgyer.com/i7Tp");
                    startActivity(Intent.createChooser(textIntent, "分享"));

                }
            }
        });

        Button btn2 = findViewById(R.id.contect);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkApkExist(AboutActivity.this, "com.tencent.mobileqq")
                        ||checkApkExist(AboutActivity.this, "com.tencent.tim")) {

                    joinQQGroup("KibI3s1CevQ5QECT8wDUnBeFk9Didhts");
                } else {
                    Toast.makeText(AboutActivity.this, "未安装qq，请手动加群", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /****************
     *
     * 发起添加群流程。群号：淘矿(242524777) 的 key 为： KibI3s1CevQ5QECT8wDUnBeFk9Didhts
     * 调用 joinQQGroup(KibI3s1CevQ5QECT8wDUnBeFk9Didhts) 即可发起手Q客户端申请加群 淘矿(242524777)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回false表示呼起失败
     ******************/
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

    public boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private String getLocalVersionName(Context ctx) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionName;
            LogUtils.d("TAG", "当前版本名称：" + localVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }
}