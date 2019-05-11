package com.flying.taokuang;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieCompositionFactory;
import com.airbnb.lottie.LottieListener;
import com.flying.baselib.WeakHandler;
import com.flying.baselib.commonui.BannerIndicatorView;
import com.flying.baselib.utils.ui.ToastUtils;
import com.flying.baselib.utils.ui.UiUtils;
import com.flying.taokuang.Adapter.DetailImageAdapter;
import com.flying.taokuang.base.BaseToolbarActivity;
import com.flying.taokuang.entity.CollectionBean;
import com.flying.taokuang.entity.TaoKuang;
import com.flying.taokuang.entity.User;
import com.flying.taokuang.ui.AsyncImageView;
import com.flying.taokuang.ui.TipsDialog;
import com.flying.taokuang.ui.AlertDialog;
import com.tendcloud.tenddata.TCAgent;

import org.litepal.LitePal;

import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static android.view.View.GONE;

public class DetailActivity extends BaseToolbarActivity {
    public static final String GO_DETAIL_PAGE_TAG = "GO_DETAIL_PAGE_TAG";
    private static final int MSG_LOAD_GOODS = 1;
    private static final int MSG_LOAD_USER = 2;
    private static final int MSG_INIT_VIEW = 3;
    private static final int MSG_LOAD_FAIL = 4;
    private static final int MSG_REMOVE_LOADING_ANIM = 5;

    private ImageView mIvcollect;
    private ImageView mIvBack;
    private Button mBtnBuy;
    private Button mBtnFinishTrade;
    private RecyclerView mRvImages;
    private TextView mTvUserNickName;
    private TextView mToolbarTitle;
    private AsyncImageView mIvUserAvatar;
    private SwipeRefreshLayout mRefreshLayout;
    private BannerIndicatorView mIndicatorView;
    private View mGoUserPage;
    private FrameLayout mContentLayout;
    private LottieAnimationView lottieAnimationView;

    private LinearLayoutManager mImagesLayoutManager;

    private DetailImageAdapter mDetailImageAdapter;
    private TaoKuang mCurrentGoods;

    private TextView mTvExpand;
    private TextView mTvExpandText;
    private boolean expandText = true;

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
        mContentLayout = findViewById(android.R.id.content);
        lottieAnimationView = new LottieAnimationView(this);
        LottieCompositionFactory.fromAsset(this, "detail_loading.json").addListener(new LottieListener<LottieComposition>() {
            @Override
            public void onResult(LottieComposition result) {
                lottieAnimationView.setComposition(result);
                lottieAnimationView.playAnimation();
            }
        });
        lottieAnimationView.setPadding(UiUtils.dp2px(150), 0, UiUtils.dp2px(150), 0);
        lottieAnimationView.useHardwareAcceleration(true);
        lottieAnimationView.setRepeatCount(-1);
        lottieAnimationView.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        mContentLayout.addView(lottieAnimationView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mToolbar.setBackgroundColor(getResources().getColor(R.color.commonColorGrey3));

        mRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        mBtnBuy = findViewById(R.id.detail_gm);
        mIvBack = findViewById(R.id.img_return);
        mTvUserNickName = findViewById(R.id.detail_nc);
        mIvUserAvatar = findViewById(R.id.detail_tx);
        mRvImages = findViewById(R.id.detail_recycler);
        mBtnFinishTrade = findViewById(R.id.gm_qr);
        mIvcollect = findViewById(R.id.detail_collection);
        mToolbarTitle = findViewById(R.id.tv_title);
        mGoUserPage = findViewById(R.id.rl_go_user_page);
        mIndicatorView = findViewById(R.id.biv_indicator);

        mTvExpand = findViewById(R.id.detail_show_all);
        mTvExpandText = findViewById(R.id.detail_ms);

        mRefreshLayout.setRefreshing(true);
        mRefreshLayout.setOnRefreshListener(mRefreshListener);
        mRvImages.setFocusable(false);
        mImagesLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRvImages.setLayoutManager(mImagesLayoutManager);
        new PagerSnapHelper().attachToRecyclerView(mRvImages);
        mDetailImageAdapter = new DetailImageAdapter(this);
        mRvImages.setAdapter(mDetailImageAdapter);
        mIvUserAvatar.setRoundAsCircle();
        UiUtils.setOnTouchBackground(mIvBack);
        UiUtils.expandClickRegion(mIvcollect, UiUtils.dp2px(10));
        UiUtils.setOnTouchBackground(mGoUserPage);

        mBtnBuy.setOnClickListener(mBuyListener);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mGoUserPage.setOnClickListener(mGoUserPageListener);
        mBtnFinishTrade.setOnClickListener(mFinishTradeListener);
        mIvcollect.setOnClickListener(mCollectListener);

        mTvExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandText) {
                    mTvExpand.setText(R.string.detail_show_all_expand_tips);
                    mTvExpandText.setMaxLines(10);
                    expandText = false;
                    mTvExpand.setText(R.string.detail_show_little_tips);
                } else {
                    mTvExpand.setText(R.string.detail_show_little_tips);
                    mTvExpandText.setMaxLines(3);
                    expandText = true;
                    mTvExpand.setText(R.string.detail_show_all_expand_tips);
                }
            }
        });
        mHandler.sendEmptyMessage(MSG_LOAD_GOODS);
    }


    private void loadGoodsData() {
        if (getIntent() == null || TextUtils.isEmpty(getIntent().getStringExtra(GO_DETAIL_PAGE_TAG))) {
            finish();
            return;
        }
        BmobQuery<TaoKuang> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(getIntent().getStringExtra(GO_DETAIL_PAGE_TAG), new QueryListener<TaoKuang>() {
            @Override
            public void done(TaoKuang taoKuang, BmobException e) {
                if (e != null) {
                    mHandler.sendEmptyMessage(MSG_LOAD_FAIL);
                    return;
                }
                mCurrentGoods = taoKuang;
                mHandler.sendEmptyMessage(MSG_LOAD_USER);
            }
        });
    }

    private void loadUserData() {
        if (mCurrentGoods == null) {
            mHandler.sendEmptyMessage(MSG_LOAD_FAIL);
            return;
        }
        BmobQuery<User> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(mCurrentGoods.getFabu().getObjectId(), new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e != null) {
                    mHandler.sendEmptyMessage(MSG_LOAD_FAIL);
                    return;
                }
                mCurrentGoods.setFabu(user);
                mHandler.sendEmptyMessage(MSG_INIT_VIEW);
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

        TextView djg = findViewById(R.id.detail_jg);
        TextView dlx = findViewById(R.id.detail_lx);
        TextView dms = findViewById(R.id.detail_ms);
        TextView dwz = findViewById(R.id.detail_wz);
        dlx.setText("联系方式:" + mCurrentGoods.getLianxi());
        dms.setText(mCurrentGoods.getMiaoshu());
        dwz.setText("交易地点:" + mCurrentGoods.getWeizhi());
        djg.setText("￥" + mCurrentGoods.getJiage());

        mToolbarTitle.setText(mCurrentGoods.getBiaoti());
        mTvUserNickName.setText(mCurrentGoods.getFabu().getNicheng());
        judgeCollection(mCurrentGoods.getObjectId());

        if (mCurrentGoods.getFabu() != null && mCurrentGoods.getFabu().getIcon() != null && !TextUtils.isEmpty(mCurrentGoods.getFabu().getIcon().getFileUrl())) {
            mIvUserAvatar.setUrl(mCurrentGoods.getFabu().getIcon().getFileUrl(), UiUtils.dp2px(40), UiUtils.dp2px(40));
        } else {
            mIvUserAvatar.setPlaceholderImage(R.drawable.ic_default_avatar);
        }

        mDetailImageAdapter.addData(mCurrentGoods.getPic());
        mIndicatorView.setCellCount(mCurrentGoods.getPic().size());
        mRvImages.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == SCROLL_STATE_IDLE && mImagesLayoutManager != null && mIndicatorView != null) {
                    int pos = mImagesLayoutManager.findFirstCompletelyVisibleItemPosition();
                    mIndicatorView.setCurrentPosition(pos);
                }
            }
        });

        if (mCurrentGoods.getGoumai() != null && mCurrentGoods.getGoumai().getObjectId().equals(User.getCurrentUser(User.class).getObjectId())) {
            mBtnFinishTrade.setVisibility(View.VISIBLE);
        } else {
            mBtnFinishTrade.setVisibility(GONE);
        }
        //text();
        if (dms.getLineCount() <= 3) {
            mTvExpand.setVisibility(GONE);
        } else {
            dms.setMaxLines(3);
        }

    }

    private View.OnClickListener mBuyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final AlertDialog purchaseDialog = new AlertDialog(DetailActivity.this);
            purchaseDialog.setTitle(R.string.dialog_purchase_tips);
            purchaseDialog.setConfirmButton(R.string.dialog_purchase_confirm_tips, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!BmobUser.isLogin() || !BmobUser.getCurrentUser(User.class).getRenz()) {
                        ToastUtils.show(getBaseContext().getResources().getString(R.string.detail_login_or_verify_tips));
                        purchaseDialog.dismiss();
                        return;
                    }
                    String o = mCurrentGoods.getFabu().getObjectId();
                    String p = BmobUser.getCurrentUser(User.class).getObjectId();
                    if (mCurrentGoods.getGoumai() != null) {
                        ToastUtils.show(getBaseContext().getResources().getString(R.string.detail_selled_tips));
                        return;
                    }
                    if (o.equals(p)) {
                        ToastUtils.show(getBaseContext().getResources().getString(R.string.detail_owner_tips));
                        return;
                    }

                    mCurrentGoods.setGoumai(BmobUser.getCurrentUser(User.class));
                    mCurrentGoods.update(mCurrentGoods.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (purchaseDialog != null) {
                                purchaseDialog.dismiss();
                            }
                            if (e != null) {
                                ToastUtils.show("购买失败");
                                return;
                            }
                            TipsDialog.show(DetailActivity.this, R.string.dialog_buy_success_tips, R.mipmap.collection);
                        }
                    });
                }
            });
            purchaseDialog.setCancelButton(R.string.dialog_purchase_cancel_tips, null);
            purchaseDialog.setCanceledOnTouchOutside(true);
            purchaseDialog.show();
        }
    };


    private View.OnClickListener mGoUserPageListener = new View.OnClickListener() {
        @Override

        public void onClick(View v) {
            if (mCurrentGoods == null) {
                return;
            }
            UserPageActivity.go(DetailActivity.this, mCurrentGoods.getFabu().getObjectId());
        }
    };

    private View.OnClickListener mFinishTradeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new android.support.v7.app.AlertDialog.Builder(DetailActivity.this)
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
                TipsDialog.show(DetailActivity.this, R.string.dialog_collect_success_tips, R.mipmap.collection);
                mIvcollect.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.ic_home_item_collect_set));
            }

        }
    };

    private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            if (mHandler != null) {
                mHandler.sendEmptyMessage(MSG_LOAD_GOODS);
            }
        }
    };

    @SuppressLint("HandlerLeak")
    private WeakHandler mHandler = new WeakHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_LOAD_GOODS:
                    loadGoodsData();
                    break;
                case MSG_LOAD_USER:
                    loadUserData();
                    break;
                case MSG_INIT_VIEW:
                    initView();
                    if (mRefreshLayout != null) {
                        mRefreshLayout.setRefreshing(false);
                    }
                    mHandler.sendEmptyMessageDelayed(MSG_REMOVE_LOADING_ANIM, 500);
                    break;
                case MSG_LOAD_FAIL:
                    if (mRefreshLayout != null) {
                        mRefreshLayout.setRefreshing(false);
                    }
                    break;
                case MSG_REMOVE_LOADING_ANIM:
                    if (lottieAnimationView == null) {
                        return;
                    }
                    lottieAnimationView.animate().alpha(0.0f).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            if (mContentLayout == null || lottieAnimationView == null) {
                                return;
                            }
                            mContentLayout.removeView(lottieAnimationView);
                        }
                    }).setDuration(500).start();
                    break;
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


}


