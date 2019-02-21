package com.example.taokuang;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView djg = findViewById(R.id.detail_jg);
        TextView dbt = findViewById(R.id.detail_bt);
        TextView dms = findViewById(R.id.detail_ms);
        TextView dwz = findViewById(R.id.detail_wz);
        final ImageView di1 = findViewById(R.id.detail_im1);
        final ImageView di2 = findViewById(R.id.detail_im2);
        final ImageView di3 = findViewById(R.id.detail_im3);

        Intent intent = getIntent();
        String jgd =intent.getStringExtra("价格");
        djg.setText(jgd);
        String btd =intent.getStringExtra("标题");
        dbt.setText(btd);
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
