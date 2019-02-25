package com.example.taokuang.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.taokuang.DetailActivity;
import com.example.taokuang.Fragement.TaoFragment;
import com.example.taokuang.R;
import com.example.taokuang.entity.TaoKuang;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;

public class TaoAdapter extends RecyclerView.Adapter<TaoAdapter.ViewHolder> {
    private List<TaoKuang> mTaoList;
    private Context mContext;
    //private OnItemClickListener mListener;

    public TaoAdapter(Context context, List<TaoKuang> TaoList) {
        mContext = context;
        mTaoList = TaoList;
      //  this.mListener = mListener;
    }
   /* public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }*/

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.tao_item, viewGroup, false);
        final ViewHolder eViewholder = new ViewHolder(view);
        return eViewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final TaoKuang taoItem = mTaoList.get(position);
        if (taoItem == null) {
            return;
        }
        String biaoti = taoItem.getBiaoti();
        String jiage = taoItem.getJiage();
        final BmobFile fengmian = taoItem.getPicyi();

//这里解析图片
       new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = getPicture(fengmian.getFileUrl());
                try{
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                holder.mfengmian.post(new Runnable() {
                    @Override
                    public void run() {
                        holder.mfengmian.setImageBitmap(bitmap);
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
        //    holder.mQrs.setText("缺人数： " + qrs);

        holder.mbiaoti.setText("标题： " + biaoti);
        holder.mjiage.setText("价格： " + jiage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailIntent = new Intent(mContext,DetailActivity.class);
                detailIntent.putExtra("1图片",taoItem.getPicyi().getFileUrl());
                detailIntent.putExtra("2图片",taoItem.getPicer().getFileUrl());
                detailIntent.putExtra("3图片",taoItem.getPicsan().getFileUrl());
                detailIntent.putExtra("价格",taoItem.getJiage());
                detailIntent.putExtra("标题",taoItem.getBiaoti());
                detailIntent.putExtra("描述",taoItem.getMiaoshu());
                detailIntent.putExtra("位置",taoItem.getWeizhi());
                detailIntent.putExtra("联系",taoItem.getLianxi());
                detailIntent.putExtra("发布",taoItem.getFabu());
                detailIntent.putExtra("ID",taoItem.getObjectId());
                mContext.startActivity(detailIntent);
//点击事件
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTaoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mfengmian;
        private TextView mbiaoti;
        private TextView mjiage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mfengmian = itemView.findViewById(R.id.item_fm);
            mbiaoti = itemView.findViewById(R.id.item_bt);
            mjiage = itemView.findViewById(R.id.item_jg);
            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener!=null){
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                }
            });  */
        }
    }
}