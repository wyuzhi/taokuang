package com.flying.taokuang.holder;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.flying.baselib.utils.app.DebugSpUtils;
import com.flying.baselib.utils.collection.CollectionUtils;
import com.flying.baselib.utils.ui.ToastUtils;
import com.flying.baselib.utils.ui.UiUtils;
import com.flying.taokuang.DetailActivity;
import com.flying.taokuang.R;
import com.flying.taokuang.entity.CollectionBean;
import com.flying.taokuang.entity.TaoKuang;
import com.flying.taokuang.entity.User;
import com.flying.taokuang.ui.AsyncImageView;

import org.litepal.LitePal;

import java.util.Date;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class NewNormalViewHolder extends RecyclerView.ViewHolder {

    private Context mContext;
    private AsyncImageView mIvCoverImage;
    private TextView mTvTitle;
    private TextView mTvPrice;
    private TextView mTvUserName;
    private TextView mTvContent;
    private ImageView mIvCollect;
    private Button mBtnManage;

    public NewNormalViewHolder(@NonNull View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        mIvCoverImage = itemView.findViewById(R.id.item_cover_img);
        mTvTitle = itemView.findViewById(R.id.item_title);
        mTvPrice = itemView.findViewById(R.id.item_price);
        mTvUserName = itemView.findViewById(R.id.item_goods_owner);
        mTvContent = itemView.findViewById(R.id.feed_item_goods_content);
        mIvCollect = itemView.findViewById(R.id.item_collect);
        mBtnManage = itemView.findViewById(R.id.btn_new_normal_manage_delete);
    }


    public void bindViewHolder(final TaoKuang taoItem) {
        if (taoItem == null) {
            return;
        }
        String title = taoItem.getBiaoti();
        String price = taoItem.getJiage();
        String nickname = taoItem.getFabu().getNicheng();
        String content = taoItem.getMiaoshu();
        List<String> pic = taoItem.getPic();

        mIvCoverImage.setUrl(pic.get(0), UiUtils.dp2px(200), UiUtils.dp2px(120));
        mIvCoverImage.setRoundingRadius(UiUtils.dp2px(4));
        mTvUserName.setText(nickname);
        mTvContent.setText(content);
        mTvTitle.setText(title);
        if (taoItem.getType()!=null){
            mTvTitle.setMaxWidth(320);
            mTvContent.setMaxWidth(370);
        }
        mTvPrice.setText("￥" + price);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (taoItem == null) {
                    return;
                }
                DetailActivity.go(mContext, taoItem.getObjectId());
            }
        });
        List<CollectionBean> collections = LitePal.where("good = ?", taoItem.getObjectId()).find(CollectionBean.class);
        if (CollectionUtils.isEmpty(collections)) {
            mIvCollect.setImageResource(R.mipmap.ic_home_item_collect_unset);
        } else {
            mIvCollect.setImageResource(R.mipmap.ic_home_item_collect_set);
        }
        mIvCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<CollectionBean> collections = LitePal.where("good = ?", taoItem.getObjectId()).find(CollectionBean.class);
                if (!CollectionUtils.isEmpty(collections)) {
                    LitePal.deleteAll(CollectionBean.class, "good = ?", taoItem.getObjectId());
                    mIvCollect.setImageResource(R.mipmap.ic_home_item_collect_unset);
                } else {
                    CollectionBean collectionBean = new CollectionBean();
                    collectionBean.setCreateTime(new Date());
                    collectionBean.setGood(taoItem.getObjectId());
                    collectionBean.setPrice(taoItem.getJiage());
                    collectionBean.setTitle(taoItem.getBiaoti());
                    collectionBean.setImage(taoItem.getPic().get(0));
                    collectionBean.save();
                    mIvCollect.setImageResource(R.mipmap.ic_home_item_collect_set);
                }
            }
        });
        mBtnManage.setVisibility(View.GONE);
        if (DebugSpUtils.isManageEnable()) {
            mBtnManage.setVisibility(View.VISIBLE);
            mBtnManage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final EditText enterDebug = new EditText(mContext);
                    enterDebug.setSingleLine(true);
                    new AlertDialog.Builder(mContext).setTitle("请输入密码")
                            .setView(enterDebug)
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    User user = User.getCurrentUser(User.class);
                                    if (user != null && "111".equals(user.getUsername()) &&
                                            DebugSpUtils.KEY_GO_DEBUG.equals(enterDebug.getText().toString())) {
                                        TaoKuang sc = new TaoKuang();
                                        sc.delete(taoItem.getObjectId(), new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {
                                                ToastUtils.show("已删除");
                                            }
                                        });
                                    }
                                }
                            })
                            .create()
                            .show();


                }
            });
        }
    }

}