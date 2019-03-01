package com.example.taokuang;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taokuang.entity.TaoKuang;
import com.example.taokuang.entity.User;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class DetailActivity extends AppCompatActivity {

    private static final int MIN_CLICK_DELAY_TIME = 6000;
    private static long lastClickTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        final String id =intent.getStringExtra("ID");
        Button dgm = findViewById(R.id.detail_gm);
        dgm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long curClickTime = System.currentTimeMillis();
                if((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
                    // 超过点击间隔后再将lastClickTime重置为当前点击时间
                    lastClickTime = curClickTime;

                if (BmobUser.isLogin()) {
                    final TaoKuang gm = new TaoKuang();

                    BmobQuery<TaoKuang> gml =new BmobQuery<>();
                    gml.getObject(id, new QueryListener<TaoKuang>() {
                        @Override
                        public void done(TaoKuang taoKuang, BmobException e) {
                            if(e==null){
                                String o = taoKuang.getFabu().getObjectId();
                                String p = BmobUser.getCurrentUser(User.class).getObjectId();
                                if (taoKuang.getGoumai()==null&& !o.equals(p)){
                                    gm.setGoumai(BmobUser.getCurrentUser(User.class));
                                    gm.update(id, new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
                                                Toast.makeText(DetailActivity.this, "GOT’EM，请尽快联系卖家，当面交易",
                                                        Toast.LENGTH_LONG).show();

                                                Intent gmcg = new Intent(DetailActivity.this, MainActivity.class);
                                                gmcg.putExtra("购买成功", "购买成功");
                                                startActivity(gmcg);
                                            } else {
                                                Log.d("购买", "购买失败:" + e);
                                                Toast.makeText(DetailActivity.this, "购买失败",
                                                        Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });

                                }
                                else{
                                    Toast.makeText(DetailActivity.this, "已经售出或该商品是你发布的",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

                }

                else{
                    Toast.makeText(DetailActivity.this, "请先登陆",
                            Toast.LENGTH_LONG).show();

                }
                }
            }
        });
        TextView djg = findViewById(R.id.detail_jg);
        TextView dlx = findViewById(R.id.detail_lx);
        TextView dbt = findViewById(R.id.detail_bt);
        TextView dms = findViewById(R.id.detail_ms);
        TextView dwz = findViewById(R.id.detail_wz);
        TextView dnc = findViewById(R.id.detail_nc);
        final ImageView di1 = findViewById(R.id.detail_im1);
        final ImageView di2 = findViewById(R.id.detail_im2);
        final ImageView di3 = findViewById(R.id.detail_im3);


        String ncd =intent.getStringExtra("昵称");
        dnc.setText(ncd);
        String jgd =intent.getStringExtra("价格");
        djg.setText("￥"+jgd);
        djg.setTextColor(Color.RED);
        String btd =intent.getStringExtra("标题");
        dbt.setText(btd);
        String lxd =intent.getStringExtra("联系");
        dlx.setText(lxd);
        String msd =intent.getStringExtra("描述");
        dms.setText(msd);
        String wzd =intent.getStringExtra("位置");
        dwz.setText(wzd);
        final String d1 =intent.getStringExtra("1图片");
        final String d2 =intent.getStringExtra("2图片");
        final String d3 =intent.getStringExtra("3图片");

        //这里解析图片
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap1 = getPicture(d1);
                final Bitmap bitmap2 = getPicture(d2);
                final Bitmap bitmap3 = getPicture(d3);
                try{
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                di1.post(new Runnable() {
                    @Override
                    public void run() {
                        di1.setImageBitmap(bitmap1);
                    }
                });
                di2.post(new Runnable() {
                    @Override
                    public void run() {
                        di2.setImageBitmap(bitmap2);
                    }
                });
                di3.post(new Runnable() {
                    @Override
                    public void run() {
                        di3.setImageBitmap(bitmap3);
                    }
                });
            }

            private Bitmap getPicture(String fileUrl) {
                Bitmap bm=null;
                try{
                    URL url=new URL(fileUrl);
                    URLConnection connection=url.openConnection();
                    connection.connect();
                    InputStream inputStream=connection.getInputStream();
                    bm= BitmapFactory.decodeStream(inputStream);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return  bm;
            }
        }).start();
    }
}
