package com.flying.taokuang.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flying.baselib.utils.ui.UiUtils;
import com.flying.taokuang.Adapter.CollectionAdapter;
import com.flying.taokuang.R;
import com.flying.taokuang.entity.CollectionBean;
import com.flying.taokuang.ui.EmptyRecyclerViewHelper;

import org.litepal.LitePal;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.List;

public class MyCollectionFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private CollectionAdapter mAdapter;
    private LinearLayoutManager mlayoutManager;
    private EmptyRecyclerViewHelper mEmptyRecyclerViewHelper;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_collection_fragment, container, false);
        initView(view);
        return view;
    }
    private void initView(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_wo_sc);
        mlayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mlayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new CollectionAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
        mEmptyRecyclerViewHelper = new EmptyRecyclerViewHelper(mRecyclerView);
        loadData();
    }

    private void loadData() {
        LitePal.findAllAsync(CollectionBean.class)
                .listen(new FindMultiCallback<CollectionBean>() {
                    @Override
                    public void onFinish(List<CollectionBean> list) {
                        if (mEmptyRecyclerViewHelper != null) {
                            mEmptyRecyclerViewHelper.checkIfEmpty();
                        }
                        mAdapter.addData(list);
                    }
                });
    }

}

