package com.flying.taokuang;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flying.baselib.utils.app.LogUtils;
import com.flying.taokuang.Adapter.DetailAdapter;
import com.flying.taokuang.entity.CollectionBean;
import com.flying.taokuang.entity.TaoKuang;
import com.flying.taokuang.entity.User;
import com.flying.taokuang.ui.AsyncImageView;
import com.flying.taokuang.wo.WomcActivity;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class DetailActivity extends AppCompatActivity {
    private static final int MIN_CLICK_DELAY_TIME = 6000;
    private static long lastClickTime;
    private ImageView dcollect;
    private String jgd;
    private String btd;
    private String[] lists;
    String fabuID;
    String goumaiID;
    String gmcg;
    String dh;
    String nc;
    String gzid;
    String ncd;
    String lxd;
    String msd;
    String wzd;
    String icon;
    String id;
    List<String> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        id = intent.getStringExtra("ID");
        btd = intent.getStringExtra("标题");
        if (TextUtils.isEmpty(btd)){
            BmobQuery<TaoKuang> bmobQuery = new BmobQuery<TaoKuang>();
            bmobQuery.getObject(id, new QueryListener<TaoKuang>() {
                @Override
                public void done(TaoKuang taoKuang, BmobException e) {

//                    String a = taoKuang.getFabu().getObjectId();
//                    String b = BmobUser.getCurrentUser(User.class).getObjectId();
//                    if (a.equals(b) && taoKuang.getGoumai() != null) {
//                        gzid = taoKuang.getGezi();
//                        dh = taoKuang.getFabu().getPhone();
//                        nc = taoKuang.getFabu().getNicheng();
//                    }
                    if (taoKuang.getFabu().getIcon() != null) {
                         icon = taoKuang.getFabu().getIcon().getFileUrl();
                    }
//                    if (taoKuang.getGoumai() != null) {
//                        goumaiID = taoKuang.getGoumai().getObjectId();
//                        icon =  taoKuang.getFabu().getIcon().getFileUrl();
//                    }
                    fabuID = taoKuang.getFabu().getObjectId();
//                    gmcg = taoKuang.getGoumai()  == null ? null:"成功";
                    gzid = taoKuang.getGezi();
                    ncd = taoKuang.getFabu().getNicheng();
                    jgd = taoKuang.getJiage();
                    btd = taoKuang.getBiaoti();
                    lxd = taoKuang.getLianxi();
                    msd = taoKuang.getMiaoshu();
                    wzd = taoKuang.getWeizhi();
                    list = taoKuang.getPic();
                    lists = taoKuang.getPic().toArray(new String[taoKuang.getPic().size()]);
                    initView();

                }
            });
        }else {
            fabuID = intent.getStringExtra("发布");
            LogUtils.d("Detail", fabuID);
            goumaiID = intent.getStringExtra("购买");
            gmcg = intent.getStringExtra("购买成功");
            dh = intent.getStringExtra("购买phone");
            nc = intent.getStringExtra("购买name");
            gzid = intent.getStringExtra("鸽子id");
            ncd = intent.getStringExtra("昵称");
            jgd = intent.getStringExtra("价格");
            btd = intent.getStringExtra("标题");
            lxd = intent.getStringExtra("联系");
            msd = intent.getStringExtra("描述");
            wzd = intent.getStringExtra("位置");
            icon = intent.getStringExtra("发布icon");
            lists = intent.getStringArrayExtra("图片");
            list = Arrays.asList(lists);
            initView();
        }






    }

    private void initView() {
        Button dgm = findViewById(R.id.detail_gm);
        dgm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SweetAlertDialog(DetailActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("确认购买")
                        .setContentText("确认后请尽快联系卖家交易，否则视为鸽子")
                        .setCancelText("考虑一下")
                        .setConfirmText("得到它")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {

                            @Override
                            public void onClick(final SweetAlertDialog sweetAlertDialog) {
                                long curClickTime = System.currentTimeMillis();
                                if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
                                    // 超过点击间隔后再将lastClickTime重置为当前点击时间
                                    lastClickTime = curClickTime;

                                    if (BmobUser.isLogin() && BmobUser.getCurrentUser(User.class).getRenz()) {
                                        final TaoKuang gm = new TaoKuang();

                                        BmobQuery<TaoKuang> gml = new BmobQuery<>();
                                        gml.getObject(id, new QueryListener<TaoKuang>() {
                                            @Override
                                            public void done(TaoKuang taoKuang, BmobException e) {
                                                if (e == null) {
                                                    String o = taoKuang.getFabu().getObjectId();
                                                    String p = BmobUser.getCurrentUser(User.class).getObjectId();
                                                    if (taoKuang.getGoumai() == null && !o.equals(p)) {
                                                        gm.setGoumai(BmobUser.getCurrentUser(User.class));
                                                        gm.update(id, new UpdateListener() {
                                                            @Override
                                                            public void done(BmobException e) {
                                                                if (e == null) {
                                                                    Toast.makeText(DetailActivity.this, "GOT’EM，请尽快联系卖家，当面交易",
                                                                            Toast.LENGTH_LONG).show();
                                                                    sweetAlertDialog
                                                                            .showCancelButton(false)
                                                                            .setTitleText("GOT'EM")
                                                                            .setContentText("请尽快联系卖家拿到它")
                                                                            .setConfirmText("OK")
                                                                            .setConfirmClickListener(null)
                                                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                                                                    Intent gmcg = new Intent(DetailActivity.this, MainActivity.class);
                                                                    gmcg.putExtra("购买成功", "购买成功");
                                                                    gmcg.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                    startActivity(gmcg);
                                                                } else {
                                                                    LogUtils.d("购买", "购买失败:" + e);
                                                                    Toast.makeText(DetailActivity.this, "购买失败",
                                                                            Toast.LENGTH_SHORT).show();
                                                                }

                                                            }
                                                        });
                                                    } else {
                                                        sweetAlertDialog.cancel();
                                                        Toast.makeText(DetailActivity.this, "已经售出或该商品是你发布的",
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                        });

                                    } else {
                                        sweetAlertDialog.cancel();
                                        Toast.makeText(DetailActivity.this, "请先登陆或认证",
                                                Toast.LENGTH_LONG).show();

                                    }
                                }

                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                            }
                        })
                        .show();


            }
        });
        ImageView img = findViewById(R.id.img_return);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView dgmnc = findViewById(R.id.detail_gmnc);
        TextView dgmdh = findViewById(R.id.detail_gmdh);

        TextView djg = findViewById(R.id.detail_jg);
        TextView dlx = findViewById(R.id.detail_lx);
        TextView dbt = findViewById(R.id.detail_bt);
        TextView dms = findViewById(R.id.detail_ms);
        TextView dwz = findViewById(R.id.detail_wz);
        TextView dnc = findViewById(R.id.detail_nc);
        AsyncImageView dicon = findViewById(R.id.detail_tx);
        dicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Personal = new Intent(DetailActivity.this, PersonalActivity.class);
                Personal.putExtra("发布", fabuID);
                startActivity(Personal);

            }
        });
        RecyclerView imgrec = findViewById(R.id.detail_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        imgrec.setNestedScrollingEnabled(false);
        imgrec.setLayoutManager(layoutManager);
        /*final ImageView di1 = findViewById(R.id.detail_im1);
        final ImageView di2 = findViewById(R.id.detail_im2);
        final ImageView di3 = findViewById(R.id.detail_im3);*/
        LinearLayout linearLayout = findViewById(R.id.detal_gmxx);
        Button gmqr = findViewById(R.id.gm_qr);

        if (gmcg != null) {
            gmqr.setVisibility(View.VISIBLE);
        }
        gmqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(DetailActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("确认交易")
                        .setContentText("交易成功？")
                        .setCancelText("还没")
                        .setConfirmText("成功了")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(final SweetAlertDialog sweetAlertDialog) {
                                TaoKuang cgl = new TaoKuang();
                                cgl.setBuy(true);
                                cgl.update(id, new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null) {
                                            sweetAlertDialog
                                                    .showCancelButton(false)
                                                    .setTitleText("交易成功")
                                                    .setContentText("感谢支持，希望在这里遇见更好的自己")
                                                    .setConfirmText("OK")
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                            BmobQuery<TaoKuang> bmobQuery = new BmobQuery<>();
                                                            bmobQuery.getObject(id, new QueryListener<TaoKuang>() {
                                                                @Override
                                                                public void done(TaoKuang taokuang, BmobException e) {
                                                                    if (e == null) {
                                                                        Intent commentIntent = new Intent(DetailActivity.this, CommentActivity.class);
                                                                        commentIntent.putExtra("被评人", taokuang.getFabu().getObjectId());
                                                                        commentIntent.putExtra("评价人", taokuang.getGoumai().getObjectId());
                                                                        startActivity(commentIntent);
                                                                    } else {
                                                                    }
                                                                }
                                                            });

                                                            ////////

//                                                            Intent intent = new Intent(DetailActivity.this, WomcActivity.class);
//                                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                                            startActivity(intent);
                                                        }
                                                    })
                                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                        } else {
                                            Toast.makeText(DetailActivity.this, "确认失败",
                                                    Toast.LENGTH_LONG).show();
                                            sweetAlertDialog.cancel();
                                        }
                                    }
                                });


                            }
                        }).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                        .show();


            }
        });




        dgmdh.setText(dh);
        dgmnc.setText(nc);
        String b = BmobUser.getCurrentUser(User.class).getObjectId();
        if (gzid != null) {
            linearLayout.setVisibility(View.VISIBLE);
        }
        dnc.setText(ncd);
        djg.setText("￥" + jgd);
        djg.setTextColor(Color.RED);
        dbt.setText(btd);
        dlx.setText(lxd);
        dms.setText(msd);
        dwz.setText(wzd);

///
//收藏功能
        dcollect = findViewById(R.id.detail_collection);
        judgeCollection(id);


        dcollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                judgeCollection0(id);

            }
        });


        DetailAdapter adapter = new DetailAdapter(this, list);
        imgrec.setAdapter(adapter);
        dicon.setUrl(icon);
        dicon.setPlaceholderImage(R.drawable.ic_default_avatar);
        dicon.setRoundAsCircle();


        Button gzx = findViewById(R.id.detail_gz);
        gzx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(DetailActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("确认举报")
                        .setContentText("主动联系过卖家了吗？")
                        .setCancelText("再联系联系")
                        .setConfirmText("确认举报")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(final SweetAlertDialog sweet) {
                                TaoKuang gza = new TaoKuang();
                                gza.setGezi(gzid);
                                gza.update(id, new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {

                                    }
                                });
                                TaoKuang gz = new TaoKuang();
                                gz.setObjectId(id);
                                gz.remove("goumai");
                                gz.update(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null) {
                                            sweet
                                                    .showCancelButton(false)
                                                    .setTitleText("举报成功")
                                                    .setContentText("商品已重新上架，鸽子核实之后将对买家做出惩罚")
                                                    .setConfirmText("OK")
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                            Intent intent = new Intent(DetailActivity.this, WomcActivity.class);
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            startActivity(intent);
                                                        }
                                                    })
                                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                        } else {
                                            Toast.makeText(DetailActivity.this, "举报失败",
                                                    Toast.LENGTH_LONG).show();
                                            sweet.cancel();
                                        }
                                    }
                                });
                            }
                        }).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                        .show();

            }
        });
        Button cgx = findViewById(R.id.detail_cg);
        cgx.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new SweetAlertDialog(DetailActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("确认交易")
                        .setContentText("交易成功？")
                        .setCancelText("还没")
                        .setConfirmText("成功了")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(final SweetAlertDialog sweetAlertDialog) {
                                TaoKuang cgl = new TaoKuang();
                                cgl.setJiaoyi(true);
                                cgl.update(id, new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null) {
                                            sweetAlertDialog
                                                    .showCancelButton(false)
                                                    .setTitleText("交易成功")
                                                    .setContentText("感谢支持，希望在这里遇见更好的自己")
                                                    .setConfirmText("OK")
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                            BmobQuery<TaoKuang> bmobQuery = new BmobQuery<>();
                                                            bmobQuery.getObject(id, new QueryListener<TaoKuang>() {
                                                                @Override
                                                                public void done(TaoKuang taokuang, BmobException e) {
                                                                    if (e == null) {
                                                                        Intent commentIntent = new Intent(DetailActivity.this, CommentActivity.class);
                                                                        commentIntent.putExtra("评价人", taokuang.getFabu().getObjectId());
                                                                        commentIntent.putExtra("被评人", taokuang.getGoumai().getObjectId());
                                                                        startActivity(commentIntent);
                                                                    } else {
                                                                        finish();
                                                                    }
                                                                }
                                                            });

                                                            ////////

//                                                            Intent intent = new Intent(DetailActivity.this, WomcActivity.class);
//                                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                                            startActivity(intent);
                                                        }
                                                    })
                                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                        } else {
                                            Toast.makeText(DetailActivity.this, "确认失败",
                                                    Toast.LENGTH_LONG).show();
                                            sweetAlertDialog.cancel();
                                        }
                                    }
                                });


                            }
                        }).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                        .show();


            }
        });
    }

    private void judgeCollection0(final String id) {
        List<CollectionBean> collections = LitePal.where("good = ?", id).find(CollectionBean.class);
        if (collections.size() > 0){
            showToast("取消成功");
            LitePal.deleteAll(CollectionBean.class, "good = ?" , id);
            dcollect.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_collection_no));
        }else {
            CollectionBean collectionBean = new CollectionBean();
            collectionBean.setCreateTime(new Date());
            collectionBean.setGood(id);
            collectionBean.setPrice(jgd);
            collectionBean.setTitle(btd);
            collectionBean.setImage(lists[0]);
            collectionBean.save();
            showToast("收藏成功");
            dcollect.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_collection));
//
        }
    }


    private void judgeCollection(String id) {
        List<CollectionBean> collections = LitePal.where("good = ?", id).find(CollectionBean.class);
        if (collections.size() > 0){
            dcollect.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_collection));
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
            tvImg.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_collection));
        } else
            tvImg.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_collection_no));
        tast.setView(view);
        tast.show();
    }


}
