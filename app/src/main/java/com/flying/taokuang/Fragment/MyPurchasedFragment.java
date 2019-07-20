package com.flying.taokuang.Fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flying.taokuang.Adapter.SellingRecyclerviewAdapter;
import com.flying.taokuang.R;
import com.flying.taokuang.entity.TaoKuang;
import com.flying.taokuang.entity.User;
import com.flying.taokuang.ui.EmptyRecyclerViewHelper;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MyPurchasedFragment extends Fragment {
    private RecyclerView gRecyclerView;
    private SellingRecyclerviewAdapter mAdapter;
    private EmptyRecyclerViewHelper mEmptyRecyclerViewHelper;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_purchased_fragment, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        gRecyclerView = view.findViewById(R.id.recycler_wo_gm);
        gRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new SellingRecyclerviewAdapter(getContext());
        gRecyclerView.setAdapter(mAdapter);
        gRecyclerView.setHasFixedSize(true);
        mEmptyRecyclerViewHelper = new EmptyRecyclerViewHelper(gRecyclerView);
        loadData();
    }
    private void loadData() {
        if (BmobUser.isLogin()) {
            BmobQuery<TaoKuang> query = new BmobQuery<>();
            query.addWhereDoesNotExists("type");
            query.addWhereEqualTo("goumai", BmobUser.getCurrentUser(User.class));
            query.addWhereEqualTo("buy", false);
            query.order("-updatedAt");
            //包含作者信息
            query.include("fabu");
            query.findObjects(new FindListener<TaoKuang>() {

                @Override
                public void done(List<TaoKuang> object, BmobException e) {
                    if (mEmptyRecyclerViewHelper != null) {
                        mEmptyRecyclerViewHelper.checkIfEmpty();
                    }
                    if (e == null) {
                        mAdapter.addData(object);
                    }
                }

            });
        } else {
            Snackbar.make(gRecyclerView, "请先登录", Snackbar.LENGTH_LONG).show();
        }
    }
}

