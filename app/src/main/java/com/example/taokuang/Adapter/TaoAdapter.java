package com.example.taokuang.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taokuang.DetailActivity;
import com.example.taokuang.R;
import com.example.taokuang.entity.TaoKuang;
import com.example.taokuang.entity.User;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class TaoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<TaoKuang> mTaoList;
    private Context mContext;
    private ImageLoader imageLoader = ImageLoader.getInstance();


    private static final int MIN_CLICK_DELAY_TIME = 3600;
    private static long lastClickTime;

    private static final int NOMAL_ITEM = 9999;
    private static final int ZAIS_ITEM = 8888;
    public static final int ZAIM_ITEM = 4;

    public TaoAdapter(Context context, List<TaoKuang> TaoList) {
        mContext = context;
        mTaoList = TaoList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        switch (i) {
            case NOMAL_ITEM:
                return new nomalViewHolder(LayoutInflater.from(mContext).
                        inflate(R.layout.tao_item, viewGroup, false));
            case ZAIS_ITEM:
                return new zaisViewHolder(LayoutInflater.from(mContext).
                        inflate(R.layout.tao_item_zs, viewGroup, false));
        }
        return null;
    }

    public class nomalViewHolder extends RecyclerView.ViewHolder {
        private ImageView nfengmian;
        private TextView nbiaoti;
        private TextView njiage;


        public nomalViewHolder(@NonNull View itemView) {
            super(itemView);
            nfengmian = itemView.findViewById(R.id.item_fm);
            nbiaoti = itemView.findViewById(R.id.item_bt);
            njiage = itemView.findViewById(R.id.item_jg);
        }
    }

    public class zaisViewHolder extends RecyclerView.ViewHolder {
        private ImageView sfengmian;
        private TextView sbiaoti;
        private TextView sjiage;
        private Button sgj;
        private Button ssc;
        private Button scl;

        public zaisViewHolder(@NonNull View itemView) {
            super(itemView);
            sfengmian = itemView.findViewById(R.id.item_fm_zs);
            sbiaoti = itemView.findViewById(R.id.item_bt_zs);
            sjiage = itemView.findViewById(R.id.item_jg_zs);
            sgj = itemView.findViewById(R.id.item_gj_zs);
            ssc = itemView.findViewById(R.id.item_sc_zs);
            scl = itemView.findViewById(R.id.item_sc_cl);

        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        int type = getItemViewType(i);

        switch (type) {
            case NOMAL_ITEM:
                bineViewHolderNomal((nomalViewHolder) viewHolder, i);
                break;
            case ZAIS_ITEM:
                bineViewHolderZais((zaisViewHolder) viewHolder, i);
                break;

        }
    }

    private void bineViewHolderNomal(nomalViewHolder viewHolder, int i) {
        final TaoKuang taoItem = mTaoList.get(i);

        if (taoItem == null) {
            return;
        }
        String biaoti = taoItem.getBiaoti();
        String jiage = taoItem.getJiage();
        BmobFile fengmian = taoItem.getPicyi();

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.hdb)// 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.hdb)// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.hdb)// 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
                .resetViewBeforeLoading(true)
                .considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(20))// 设置成圆角图片
                .build();// 创建DisplayImageOptions对象
        // 使用ImageLoader加载图片
        imageLoader.displayImage(fengmian.getFileUrl(), viewHolder.nfengmian
                , options);
        viewHolder.nbiaoti.setText("     " + biaoti);
        viewHolder.njiage.setText("    ￥" + jiage);
        viewHolder.njiage.setTextColor(Color.RED);
        viewHolder.itemView.setOnClickListener
                (new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         Intent detailIntent = new Intent(mContext, DetailActivity.class);
                         detailIntent.putExtra("1图片", taoItem.getPicyi().getFileUrl());
                         detailIntent.putExtra("2图片", taoItem.getPicer().getFileUrl());
                         detailIntent.putExtra("3图片", taoItem.getPicsan().getFileUrl());
                         detailIntent.putExtra("价格", taoItem.getJiage());
                         detailIntent.putExtra("标题", taoItem.getBiaoti());
                         detailIntent.putExtra("描述", taoItem.getMiaoshu());
                         detailIntent.putExtra("位置", taoItem.getWeizhi());
                         detailIntent.putExtra("联系", taoItem.getLianxi());
                         detailIntent.putExtra("发布", taoItem.getFabu());
                         String a = taoItem.getFabu().getObjectId();
                         String b = BmobUser.getCurrentUser(User.class).getObjectId();
                         if (a.equals(b) && taoItem.getGoumai() != null) {
                             detailIntent.putExtra("购买name", taoItem.getGoumai().getNicheng());
                             detailIntent.putExtra("购买phone", taoItem.getGoumai().getPhone());
                             detailIntent.putExtra("鸽子id", taoItem.getGoumai().getObjectId());
                         }


                         if (taoItem.getFabu().getIcon() != null) {
                             detailIntent.putExtra("发布icon", taoItem.getFabu().getIcon().getFileUrl());
                         }
                         detailIntent.putExtra("昵称", taoItem.getFabu().getNicheng());
                         detailIntent.putExtra("ID", taoItem.getObjectId());
                         mContext.startActivity(detailIntent);
                     }
                 }
                );

    }

    private void bineViewHolderZais(final zaisViewHolder viewHolder, int i) {
        final TaoKuang taoItem = mTaoList.get(i);
        if (taoItem == null) {
            return;
        }
        String biaoti = taoItem.getBiaoti();
        String jiage = taoItem.getJiage();
        BmobFile fengmian = taoItem.getPicyi();

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.hdb)// 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.hdb)// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.hdb)// 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
                .resetViewBeforeLoading(true)
                .considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(20))// 设置成圆角图片
                .build();// 创建DisplayImageOptions对象
        // 使用ImageLoader加载图片
        imageLoader.displayImage(fengmian.getFileUrl(), viewHolder.sfengmian
                , options);
        viewHolder.sbiaoti.setText("     " + biaoti);
        viewHolder.sjiage.setText("  ￥" + jiage);
        viewHolder.sjiage.setTextColor(Color.RED);
        viewHolder.scl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long curClickTime = System.currentTimeMillis();
                if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
                    // 超过点击间隔后再将lastClickTime重置为当前点击时间
                    lastClickTime = curClickTime;
                    TaoKuang cl = new TaoKuang();
                    int i = taoItem.getGengxin();
                    i++;
                    cl.setGengxin(i);
                    cl.update(taoItem.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Log.d("擦亮", "擦亮成功");
                                Toast.makeText(mContext, "擦亮成功",
                                        Toast.LENGTH_SHORT).show();
                                viewHolder.scl.setBackgroundColor(Color.GRAY);
                            } else {
                                Log.d("擦亮", e.toString());
                                Toast.makeText(mContext, "擦亮失败" + e.toString(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        viewHolder.ssc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaoKuang sc = new TaoKuang();
                sc.delete(taoItem.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Log.d("删除", "删除成功");
                            Toast.makeText(mContext, "删除成功",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            Log.d("删除", e.toString());
                            Toast.makeText(mContext, "删除失败" + e.toString(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        viewHolder.sgj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        viewHolder.sfengmian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailIntent = new Intent(mContext, DetailActivity.class);
                detailIntent.putExtra("1图片", taoItem.getPicyi().getFileUrl());
                detailIntent.putExtra("2图片", taoItem.getPicer().getFileUrl());
                detailIntent.putExtra("3图片", taoItem.getPicsan().getFileUrl());
                detailIntent.putExtra("价格", taoItem.getJiage());
                detailIntent.putExtra("标题", taoItem.getBiaoti());
                detailIntent.putExtra("描述", taoItem.getMiaoshu());
                detailIntent.putExtra("位置", taoItem.getWeizhi());
                detailIntent.putExtra("联系", taoItem.getLianxi());
                detailIntent.putExtra("发布", taoItem.getFabu());
                if (taoItem.getFabu().getIcon() != null) {



                    detailIntent.putExtra("发布icon", taoItem.getFabu().getIcon().getFileUrl());
                }

                detailIntent.putExtra("昵称", taoItem.getFabu().getNicheng());
                detailIntent.putExtra("ID", taoItem.getObjectId());
                mContext.startActivity(detailIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mTaoList.size();
    }

    @Override
    public int getItemViewType(int position) {
        TaoKuang taoItem = mTaoList.get(position);
        String o = taoItem.getFabu().getObjectId();
        String p = BmobUser.getCurrentUser(User.class).getObjectId();
        if (o.equals(p) && taoItem.getGoumai() == null) {
            return ZAIS_ITEM;
        } else
            return NOMAL_ITEM;
    }
}