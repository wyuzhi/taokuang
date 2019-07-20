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

public class MySoldFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private SellingRecyclerviewAdapter mAdapter;
    private EmptyRecyclerViewHelper mEmptyRecyclerViewHelper;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_sold_fragment, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_wo_mc);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new SellingRecyclerviewAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mEmptyRecyclerViewHelper = new EmptyRecyclerViewHelper(mRecyclerView);
        loadData();

    }
    private void loadData() {
        if (BmobUser.isLogin()) {
            BmobQuery<TaoKuang> query = new BmobQuery<>();
            query.addWhereExists("goumai");
            query.addWhereNotEqualTo("type", "1");
            query.addWhereEqualTo("fabu", BmobUser.getCurrentUser(User.class));
            query.addWhereEqualTo("jiaoyi", false);
            query.order("-updatedAt");
            query.include("fabu,goumai");
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
            Snackbar.make(mRecyclerView, "请先登录", Snackbar.LENGTH_LONG).show();
        }
    }

}

