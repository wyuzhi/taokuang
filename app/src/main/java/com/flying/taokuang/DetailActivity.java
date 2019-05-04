package com.flying.taokuang;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flying.baselib.utils.ui.ToastUtils;
import com.flying.baselib.utils.ui.UiUtils;
import com.flying.taokuang.Adapter.DetailImageAdapter;
import com.flying.taokuang.base.BaseToolbarActivity;
import com.flying.taokuang.entity.CollectionBean;
import com.flying.taokuang.entity.TaoKuang;
import com.flying.taokuang.entity.User;
import com.flying.taokuang.ui.AsyncImageView;
import com.tendcloud.tenddata.TCAgent;

import org.litepal.LitePal;

import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class DetailActivity extends BaseToolbarActivity {
    public static final String GO_DETAIL_PAGE_TAG = "GO_DETAIL_PAGE_TAG";

    private ImageView mIvcollect;
    private ImageView mIvBack;
    private Button mBtnBuy;
    private Button mBtnFinishTrade;
    private RecyclerView mRvImages;
    private TextView mTvUserNickName;
    private TextView mToolbarTitle;
    private AsyncImageView mIvUserAvatar;

    private DetailImageAdapter mDetailImageAdapter;
    private TaoKuang mCurrentGoods;

    public static void go(Context context, String id) {
        if (context == null || TextUtils.isEmpty(id)) {
            return;
        }
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(GO_DETAIL_PAGE_TAG, id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.commonColorGrey3));
        if (getIntent() == null || TextUtils.isEmpty(getIntent().getStringExtra(GO_DETAIL_PAGE_TAG))) {
            finish();
            return;
        }
        BmobQuery<TaoKuang> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(getIntent().getStringExtra(GO_DETAIL_PAGE_TAG), new QueryListener<TaoKuang>() {
            @Override
            public void done(TaoKuang taoKuang, BmobException e) {
                if (e != null) {
                    finish();
                    return;
                }
                mCurrentGoods = taoKuang;
                initView();
            }
        });
    }

    @Override
    public int getContentViewResId() {
        return R.layout.activity_detail;
    }

    @Override
    protected void onPause() {
        super.onPause();
        TCAgent.onPageEnd(this, "商品详情页");
    }

    @Override
    protected void onResume() {
        super.onResume();
        TCAgent.onPageStart(this, "商品详情页");
    }

    private void initView() {
        if (mCurrentGoods == null) {
            return;
        }
        mBtnBuy = findViewById(R.id.detail_gm);
        mIvBack = findViewById(R.id.img_return);
        mTvUserNickName = findViewById(R.id.detail_nc);
        mIvUserAvatar = findViewById(R.id.detail_tx);
        mRvImages = findViewById(R.id.detail_recycler);
        mBtnFinishTrade = findViewById(R.id.gm_qr);
        mIvcollect = findViewById(R.id.detail_collection);
        mToolbarTitle = findViewById(R.id.tv_title);

        TextView djg = findViewById(R.id.detail_jg);
        TextView dlx = findViewById(R.id.detail_lx);
        TextView dbt = findViewById(R.id.detail_bt);
        TextView dms = findViewById(R.id.detail_ms);
        TextView dwz = findViewById(R.id.detail_wz);
        dbt.setText(mCurrentGoods.getBiaoti());
        dlx.setText(mCurrentGoods.getLianxi());
        dms.setText(mCurrentGoods.getMiaoshu());
        dwz.setText(mCurrentGoods.getWeizhi());
        djg.setText("￥" + mCurrentGoods.getJiage());

        UiUtils.setOnTouchBackground(mIvBack);
        UiUtils.setOnTouchBackground(mTvUserNickName);
        UiUtils.setOnTouchBackground(mIvUserAvatar);
        UiUtils.expandClickRegion(mTvUserNickName, UiUtils.dp2px(5));
        UiUtils.expandClickRegion(mIvUserAvatar, UiUtils.dp2px(5));

        mToolbarTitle.setText(mCurrentGoods.getBiaoti());
        mTvUserNickName.setText(mCurrentGoods.getFabu().getNicheng());
        judgeCollection(mCurrentGoods.getObjectId());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRvImages.setFocusable(false);
        mRvImages.setLayoutManager(layoutManager);
        mDetailImageAdapter = new DetailImageAdapter(this, mCurrentGoods.getPic());
        mRvImages.setAdapter(mDetailImageAdapter);

        if (mCurrentGoods.getFabu() != null && mCurrentGoods.getFabu().getIcon() != null && !TextUtils.isEmpty(mCurrentGoods.getFabu().getIcon().getFileUrl())) {
            mIvUserAvatar.setUrl(mCurrentGoods.getFabu().getIcon().getFileUrl(), UiUtils.dp2px(50), UiUtils.dp2px(50));
        } else {
            mIvUserAvatar.setPlaceholderImage(R.drawable.ic_default_avatar);
        }
        mIvUserAvatar.setRoundAsCircle();


        if (mCurrentGoods.getGoumai() != null && mCurrentGoods.getGoumai().getObjectId().equals(User.getCurrentUser(User.class).getObjectId())) {
            mBtnFinishTrade.setVisibility(View.VISIBLE);
        } else {
            mBtnFinishTrade.setVisibility(View.GONE);
        }

        mBtnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(DetailActivity.this)
                        .setTitle("确认购买")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                if (!BmobUser.isLogin() || !BmobUser.getCurrentUser(User.class).getRenz()) {
                                    ToastUtils.show("请先登陆或认证");
                                    dialog.dismiss();
                                    return;
                                }
                                String o = mCurrentGoods.getFabu().getObjectId();
                                String p = BmobUser.getCurrentUser(User.class).getObjectId();
                                if (mCurrentGoods.getGoumai() != null || o.equals(p)) {
                                    ToastUtils.show("该商品已售或是你发布的");
                                }
                                mCurrentGoods.setGoumai(BmobUser.getCurrentUser(User.class));
                                mCurrentGoods.update(mCurrentGoods.getObjectId(), new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e != null) {
                                            ToastUtils.show("购买失败");
                                            return;
                                        }
                                        dialog.dismiss();
                                        finish();
                                    }
                                });
                            }
                        }).setNegativeButton("取消", null).show();
            }
        });

        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mIvUserAvatar.setOnClickListener(mGoUserPageListener);
        mTvUserNickName.setOnClickListener(mGoUserPageListener);
        mBtnFinishTrade.setOnClickListener(mFinishTradeListener);
        mIvcollect.setOnClickListener(mCollectListener);
        UiUtils.expandClickRegion(mIvcollect, UiUtils.dp2px(10));

    }

    private View.OnClickListener mGoUserPageListener = new View.OnClickListener() {
        @Override

        public void onClick(View v) {
            if (mCurrentGoods == null) {
                return;
            }
            UserPageActivity.go(DetailActivity.this, mCurrentGoods.getObjectId());
        }
    };

    private View.OnClickListener mFinishTradeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(DetailActivity.this)
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mCurrentGoods == null) {
                                return;
                            }
                            mCurrentGoods.setBuy(true);
                            mCurrentGoods.update(mCurrentGoods.getObjectId(), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        Intent commentIntent = new Intent(DetailActivity.this, CommentActivity.class);
                                        commentIntent.putExtra("被评人", mCurrentGoods.getFabu().getObjectId());
                                        commentIntent.putExtra("评价人", mCurrentGoods.getGoumai().getObjectId());
                                        startActivity(commentIntent);
                                    } else {
                                        ToastUtils.show("确认失败");
                                    }
                                }
                            });
                        }
                    })
                    .setNegativeButton("取消", null).show();
        }
    };

    private View.OnClickListener mCollectListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mCurrentGoods == null) {
                return;
            }
            List<CollectionBean> collections = LitePal.where("good = ?", mCurrentGoods.getObjectId()).find(CollectionBean.class);
            if (collections.size() > 0) {
                showToast("取消成功");
                LitePal.deleteAll(CollectionBean.class, "good = ?", mCurrentGoods.getObjectId());
                mIvcollect.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_detail_collect_unset));
            } else {
                CollectionBean collectionBean = new CollectionBean();
                collectionBean.setCreateTime(new Date());
                collectionBean.setGood(mCurrentGoods.getObjectId());
                collectionBean.setPrice(mCurrentGoods.getJiage());
                collectionBean.setTitle(mCurrentGoods.getBiaoti());
                collectionBean.setImage(mCurrentGoods.getPic().get(0));
                collectionBean.save();
                showToast("收藏成功");
                mIvcollect.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.ic_home_item_collect_set));
//
            }

        }
    };


    private void judgeCollection(String id) {
        List<CollectionBean> collections = LitePal.where("good = ?", id).find(CollectionBean.class);
        if (collections.size() > 0) {
            mIvcollect.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.ic_home_item_collect_set));
        } else {
            mIvcollect.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_detail_collect_unset));
        }
    }

    private void showToast(String txt) {
        Toast tast = Toast.makeText(this, txt, Toast.LENGTH_SHORT);
        tast.setGravity(Gravity.CENTER, 0, 0);
        View view = LayoutInflater.from(this).inflate(R.layout.custom_toast, null);
        TextView tvMsg = view.findViewById(R.id.tvMsg);
        ImageView tvImg = view.findViewById(R.id.tvImg);
        tvMsg.setText(txt);
        if (txt.equals("收藏成功")) {
            tvImg.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.collection));
        } else
            tvImg.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_collection_no));
        tast.setView(view);
        tast.show();
    }


}
