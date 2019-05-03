package com.flying.taokuang.My;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.flying.baselib.utils.app.LogUtils;
import com.flying.baselib.utils.ui.UiUtils;
import com.flying.taokuang.Adapter.CollectionAdapter;
import com.flying.taokuang.R;
import com.flying.taokuang.base.BaseToolbarActivity;
import com.flying.taokuang.entity.CollectionBean;

import org.litepal.LitePal;

import java.util.List;

public class MyCollectionActivity extends BaseToolbarActivity {
    private RecyclerView mRecyclerView;
    private CollectionAdapter mAdapter;
    private LinearLayoutManager mlayoutManager;
    private ImageView mIvBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    public int getContentViewResId() {
        return R.layout.activity_my_collection;
    }

    private void initView() {
        mIvBack = findViewById(R.id.img_return);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        UiUtils.setOnTouchBackground(mIvBack);
        mRecyclerView = findViewById(R.id.recycler_wo_sc);
        mlayoutManager = new LinearLayoutManager(this);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        loadData();
    }

    private void loadData() {
        List<CollectionBean> collections = LitePal.findAll(CollectionBean.class);
//        List<TaoKuang> datas = new ArrayList<>();
//        for (final CollectionBean bean: collections) {
//            TaoKuang taoKuang = new TaoKuang();
//            taoKuang.setBiaoti(bean.getTitle());
//            taoKuang.setJiage(bean.getPrice());
//            taoKuang.setObjectId(bean.getGood());
//            taoKuang.setPic(new ArrayList<String>(){{
//                add(bean.getImage());
//            }});
//            datas.add(taoKuang);
//        }
        LogUtils.d("Collection Taokuang", "loadData: " + collections.size());
        mAdapter = new CollectionAdapter(collections, MyCollectionActivity.this);
        mRecyclerView.setAdapter(mAdapter);
//        if (BmobUser.isLogin()) {
//
//            BmobQuery<Collection> query = new BmobQuery<>();
////用此方式可以构造一个BmobPointer对象。只需要设置objectId就行
//
//            //post.setObjectId("ESIt3334");
//
//            query.addWhereEqualTo("people", BmobUser.getCurrentUser(User.class));
////希望同时查询该评论的发布者的信息，以及该帖子的作者的信息，这里用到上面`include`的并列对象查询和内嵌对象的查询
//            query.include("goods");
//            query.findObjects(new FindListener<Collection>() {
//
//                @Override
//                public void done(List<Collection> objects, BmobException e) {
//                    if (e == null){
//                        List list = new ArrayList();
//                        for (int i =0;i<objects.size();i++){
//                            list.add(objects.get(i).getGoods());
//                        }
//                        mAdapter = new TaoAdapter(MyCollectionActivity.this, list);
//                        mRecyclerView.setAdapter(mAdapter);
//
//                        Log.d("haha", objects.toString());
//
//
//                    }
//
//
//                }
//            });
//
//        }
    }
}

