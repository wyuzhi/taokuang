package com.flying.taokuang.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.flying.baselib.utils.collection.CollectionUtils;
import com.flying.baselib.utils.ui.ToastUtils;
import com.flying.baselib.utils.ui.UiUtils;
import com.flying.taokuang.CommentActivity;
import com.flying.taokuang.DetailActivity;
import com.flying.taokuang.R;
import com.flying.taokuang.entity.TaoKuang;
import com.flying.taokuang.entity.User;
import com.flying.taokuang.ui.AlertDialog;
import com.flying.taokuang.ui.AsyncImageView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class PersonalSellingRecyclerviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TaoKuang> mTaoList;
    private Context mContext;

    public PersonalSellingRecyclerviewAdapter(Context context) {
        mContext = context;
        mTaoList = new ArrayList<>();
    }

    public PersonalSellingRecyclerviewAdapter(Context context, List<TaoKuang> list) {
        mContext = context;
        mTaoList = list;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new SellingViewHolder(LayoutInflater.from(mContext).inflate(R.layout.user_selling_item, viewGroup, false));
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (mTaoList == null || i < 0 || i >= mTaoList.size()) {
            return;
        }
        TaoKuang data = mTaoList.get(i);
        ((SellingViewHolder) viewHolder).bindViewHolder(data);

    }

    public void addData(List<TaoKuang> dataList) {
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        if (CollectionUtils.isEmpty(mTaoList)) {
            mTaoList = dataList;
            notifyDataSetChanged();
            return;
        }
        int oldSize = mTaoList.size();
        mTaoList.addAll(dataList);
        notifyItemRangeInserted(oldSize, dataList.size());
    }

    public void clearData() {
        mTaoList.clear();
    }

    @Override
    public int getItemCount() {
        return mTaoList.size();
    }

    public class SellingViewHolder extends RecyclerView.ViewHolder {
        private AsyncImageView mIvCoverImage;
        private TextView mTvPrice;
        private TextView mTvContent;
        private View mRoot;
        private Button mBtnSuccess;
        private Button mBtnUnsuccess;
        private TextView mTvTime;
        private TextView mTvName;
        private TextView mTvUserName;

        public SellingViewHolder(@NonNull View itemView) {
            super(itemView);
            mIvCoverImage = itemView.findViewById(R.id.item_cover_img);
            mTvPrice = itemView.findViewById(R.id.item_price);
            mTvContent = itemView.findViewById(R.id.item_title);
            mRoot = itemView;
            mBtnSuccess = itemView.findViewById(R.id.successful);
            mBtnUnsuccess = itemView.findViewById(R.id.unsuccessful);
            mTvTime = itemView.findViewById(R.id.item_time);
            mTvName = itemView.findViewById(R.id.item_name);
            mTvUserName = itemView.findViewById(R.id.username);
        }

        public void bindViewHolder(final TaoKuang data) {
            if (data == null) {
                return;
            }
            if (BmobUser.getCurrentUser(User.class).getObjectId().equals(data.getFabu().getObjectId()) && data.getGoumai() != null) {
                mBtnSuccess.setVisibility(View.VISIBLE);
                mBtnUnsuccess.setVisibility(View.VISIBLE);
                mTvUserName.setText("买家名称：");
            }
            mIvCoverImage.setRoundingRadius(UiUtils.dp2px(3));
            mIvCoverImage.setUrl(data.getPic().get(0), UiUtils.dp2px(137), UiUtils.dp2px(89));
            mTvPrice.setText("¥" + data.getJiage());
            mTvContent.setText(data.getBiaoti());
            //只能提取用户ID
            User author = new User();
            String id = data.getObjectId();
            author.setObjectId(id);
            mTvName.setText(author.getNicheng());
            mRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DetailActivity.go(mContext, data.getObjectId());
                }
            });
            mTvTime.setText(data.getCreatedAt());

            mBtnSuccess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog success = new AlertDialog((Activity) mContext);
                    success.setTitle("交易成功?");
                    success.setConfirmButton("确认", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TaoKuang cgl = new TaoKuang();
                            cgl.setJiaoyi(true);
                            cgl.update(data.getObjectId(), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        ToastUtils.show("交易成功");
                                        success.dismiss();
                                        notifyDataSetChanged();
                                        Intent intent = new Intent(mContext, CommentActivity.class);
                                        mContext.startActivity(intent);
                                    }
                                }
                            });
                        }
                    });
                    success.setCancelButton(R.string.dialog_purchase_cancel_tips, null);
                    success.setCanceledOnTouchOutside(true);
                    success.show();
                }
            });
            mBtnUnsuccess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog unsuccess = new AlertDialog((Activity) mContext);
                    unsuccess.setTitle("交易被鸽?");
                    unsuccess.setConfirmButton("确认", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TaoKuang gza = new TaoKuang();
                            gza.setGezi(data.getGoumai().getObjectId());
                            gza.update(data.getObjectId(), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        TaoKuang gz = new TaoKuang();
                                        gz.setObjectId(data.getObjectId());
                                        gz.remove("goumai");
                                        gz.update(new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {
                                                if (e == null) {
                                                    unsuccess.dismiss();
                                                    ToastUtils.show("举报成功，商品已重新上架，核实之后将对买家做出惩罚");
                                                }
                                            }
                                        });

                                    }

                                }
                            });

                        }
                    });
                    unsuccess.setCancelButton(R.string.dialog_purchase_cancel_tips, null);
                    unsuccess.setCanceledOnTouchOutside(true);
                    unsuccess.show();


                }
            });
        }

    }

}