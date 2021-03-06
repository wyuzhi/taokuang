package com.flying.taokuang;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import com.flying.baselib.commonui.edit.EditorCallback;
import com.flying.baselib.commonui.edit.FloatEditorActivity;
import com.flying.baselib.commonui.edit.InputCheckRule;
import com.flying.baselib.utils.device.ClipboardUtils;
import com.flying.baselib.utils.ui.ToastUtils;
import com.flying.baselib.utils.ui.UiUtils;
import com.flying.taokuang.Adapter.CommentAdapter;
import com.flying.taokuang.Adapter.DetailImageAdapter;
import com.flying.taokuang.base.BaseToolbarActivity;
import com.flying.taokuang.entity.CollectionBean;
import com.flying.taokuang.entity.Comment;
import com.flying.taokuang.entity.TaoKuang;
import com.flying.taokuang.entity.User;
import com.flying.taokuang.ui.AlertDialog;
import com.flying.taokuang.ui.AsyncImageView;
import com.flying.taokuang.ui.EmptyRecyclerViewHelper;
import com.flying.taokuang.ui.TipsDialog;
import com.tendcloud.tenddata.TCAgent;

import org.litepal.LitePal;

import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class DetailActivity extends BaseToolbarActivity {
    public static final String GO_DETAIL_PAGE_TAG = "GO_DETAIL_PAGE_TAG";
    private static final int MSG_LOAD_GOODS = 1;
    private static final int MSG_LOAD_USER = 2;
    private static final int MSG_INIT_VIEW = 3;
    private static final int MSG_LOAD_FAIL = 4;
    private static final int MSG_REMOVE_LOADING_ANIM = 5;
    private static final int MSG_LOAD_COMMENT = 6;

    private ImageView mIvCollect;
    private ImageView mIvBack;
    private ImageView mIvContent;
    private ImageView mIvComment;
    private ImageView mIvShare;
    private Button mBtnBuy;
    private Button mBtnFinishTrade;
    private RecyclerView mRvImages;
    private RecyclerView mRvComment;
    private CommentAdapter mCommentAdapter;
    private TextView mTvUserNickName;
    private TextView mToolbarTitle;
    private AsyncImageView mIvUserAvatar;
    private SwipeRefreshLayout mRefreshLayout;
    private BannerIndicatorView mIndicatorView;
    private View mGoUserPage;
    private FrameLayout mContentLayout;
    private LottieAnimationView lottieAnimationView;
    private EmptyRecyclerViewHelper mEmptyRecyclerViewHelper;
    private LinearLayoutManager mImagesLayoutManager;

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
        mRvComment = findViewById(R.id.detail_comment_recycler);
        mRvImages = findViewById(R.id.detail_recycler);
        mBtnFinishTrade = findViewById(R.id.gm_qr);
        mIvCollect = findViewById(R.id.detail_collection);
        mIvContent = findViewById(R.id.detail_content);
        mIvComment = findViewById(R.id.detail_comment);
        mIvShare = findViewById(R.id.detail_share);
        mToolbarTitle = findViewById(R.id.tv_title);
        mGoUserPage = findViewById(R.id.rl_go_user_page);
        mIndicatorView = findViewById(R.id.biv_indicator);

        mRefreshLayout.setRefreshing(true);
        mRefreshLayout.setOnRefreshListener(mRefreshListener);
        mRvImages.setFocusable(false);
        mImagesLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRvImages.setLayoutManager(mImagesLayoutManager);
        LinearLayoutManager slayoutManager = new LinearLayoutManager(this);
        mRvComment.setLayoutManager(slayoutManager);
        mRvComment.setHasFixedSize(true);
        new PagerSnapHelper().attachToRecyclerView(mRvImages);
        mDetailImageAdapter = new DetailImageAdapter(this);
        mRvImages.setAdapter(mDetailImageAdapter);
        mCommentAdapter = new CommentAdapter(this, R.layout.detail_comment_item);
        mRvComment.setAdapter(mCommentAdapter);
        mEmptyRecyclerViewHelper = new EmptyRecyclerViewHelper(mRvComment);
        mIvUserAvatar.setRoundAsCircle();
        UiUtils.setOnTouchBackground(mIvBack);
        UiUtils.expandClickRegion(mIvCollect, UiUtils.dp2px(10));
        UiUtils.expandClickRegion(mIvContent, UiUtils.dp2px(10));
        UiUtils.expandClickRegion(mIvContent, UiUtils.dp2px(10));
        UiUtils.expandClickRegion(mIvShare, UiUtils.dp2px(10));
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
        mIvCollect.setOnClickListener(mCollectListener);
        mIvContent.setOnClickListener(mContentListener);
        mIvComment.setOnClickListener(mCommentListener);
        mIvShare.setOnClickListener(mShareListener);

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
                mHandler.sendEmptyMessage(MSG_LOAD_COMMENT);
                if (mCurrentGoods.getType().equals("1")) {
                    mBtnBuy.setVisibility(View.GONE);
                }
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

    private void loadComment() {
        if (mCurrentGoods == null) {
            return;
        }
        BmobQuery<Comment> tQuery = new BmobQuery<>();
        tQuery.addWhereEqualTo("good", mCurrentGoods);
        tQuery.order("-updatedAt");
        tQuery.include("author");
        tQuery.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> list, BmobException e) {
                if (mEmptyRecyclerViewHelper != null) {
                    mEmptyRecyclerViewHelper.checkIfEmpty();
                }
                if (e == null && mCommentAdapter != null) {
                    mCommentAdapter.setData(list);
                }
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
        ExpandableTextView dms = findViewById(R.id.expandable_text);
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

        mDetailImageAdapter.setData(mCurrentGoods.getPic());
        mIndicatorView.setCellCount(mCurrentGoods.getPic().size());
        mRvImages.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mImagesLayoutManager != null && mIndicatorView != null) {
                    int pos = mImagesLayoutManager.findFirstCompletelyVisibleItemPosition();
                    mIndicatorView.setCurrentPosition(pos);
                }
            }
        });

        if (mCurrentGoods.getGoumai() != null && mCurrentGoods.getGoumai().getObjectId().equals(User.getCurrentUser(User.class).getObjectId())) {
            mBtnFinishTrade.setVisibility(View.VISIBLE);
        } else {
            mBtnFinishTrade.setVisibility(View.GONE);
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
    private View.OnClickListener mShareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String text = "我在淘矿上发现了" + "【" + mCurrentGoods.getBiaoti() + "】" + "$" + mCurrentGoods.getObjectId() + "&" + "复制本消息，打开【淘矿APP】即可查看，下载链接【https://www.pgyer.com/i7Tp】";
            ClipboardUtils.copy(getApplicationContext(), "Label", text);
            Intent textIntent = new Intent(Intent.ACTION_SEND);
            textIntent.setType("text/plain");
            textIntent.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(textIntent, "分享"));
        }
    };

    private View.OnClickListener mCommentListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FloatEditorActivity.openDefaultEditor(DetailActivity.this, new EditorCallback.Extend() {
                @Override
                public void onSubmit(String content) {
                    final Comment comment = new Comment();
                    comment.setAuthor(BmobUser.getCurrentUser(User.class));
                    comment.setGood(mCurrentGoods);
                    comment.setContent(content);
                    comment.save(new SaveListener<String>() {
                        @Override
                        public void done(String objectId, BmobException e) {
                            if (e != null) {
                                ToastUtils.show(getString(R.string.detail_comment_failure));
                                return;
                            }
                            ToastUtils.show(getString(R.string.detail_comment_success));
                            if (mHandler != null) {
                                mHandler.sendEmptyMessage(MSG_LOAD_COMMENT);
                            }
                        }
                    });
                }
            }, new InputCheckRule(60, 1));
        }
    };
    private View.OnClickListener mContentListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mCurrentGoods == null || TextUtils.isEmpty(mCurrentGoods.getLianxi())) {
                return;
            }
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.detail_open_qq, mCurrentGoods.getLianxi()))));
            } catch (Exception e) {
                ToastUtils.show(getResources().getString(R.string.detail_no_qq_tips));
            }
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
                mIvCollect.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_detail_collect_unset));
            } else {
                CollectionBean collectionBean = new CollectionBean();
                collectionBean.setCreateTime(new Date());
                collectionBean.setGood(mCurrentGoods.getObjectId());
                collectionBean.setPrice(mCurrentGoods.getJiage());
                collectionBean.setTitle(mCurrentGoods.getBiaoti());
                collectionBean.setContent(mCurrentGoods.getMiaoshu());
                collectionBean.setImage(mCurrentGoods.getPic().get(0));
                collectionBean.save();
                TipsDialog.show(DetailActivity.this, R.string.dialog_collect_success_tips, R.mipmap.collection);
                mIvCollect.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.ic_home_item_collect_set));
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
                case MSG_LOAD_COMMENT:
                    loadComment();
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
            mIvCollect.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.ic_home_item_collect_set));
        } else {
            mIvCollect.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_detail_collect_unset));
        }
    }

}
