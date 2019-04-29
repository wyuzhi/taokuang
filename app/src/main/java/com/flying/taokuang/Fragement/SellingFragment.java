package com.flying.taokuang.Fragement;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flying.baselib.utils.app.LogUtils;
import com.flying.taokuang.Adapter.HomeRecyclerViewAdapter;
import com.flying.taokuang.PersonalActivity;
import com.flying.taokuang.R;
import com.flying.taokuang.entity.TaoKuang;
import com.flying.taokuang.entity.User;
import com.flying.taokuang.tool.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class SellingFragment extends BaseFragment {
    private String fabuID;
    private String goumaiID;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fabuID = ((PersonalActivity) activity).getFabuID();
    }


    private RecyclerView sRecyclerView;
    private HomeRecyclerViewAdapter sAdapter;
    private SwipeRefreshLayout sSwipeRefresh;
    //private StaggeredGridLayoutManager slayoutManager;
    private List<TaoKuang> sTaolist;
    //HomeRecyclerViewAdapter.OnItemClickListener listener = (HomeRecyclerViewAdapter.OnItemClickListener) getContext();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selling, container, false);
        initView(view);
        return view;
    }

    private void initView(final View v) {
        sTaolist = new ArrayList<>();
        sRecyclerView = v.findViewById(R.id.recycler_tao_s);
        loadDate();
    }

    private void loadDate() {
        if (BmobUser.isLogin()) {
            User user = new User();
            user.setObjectId(fabuID);
            LogUtils.d("查询", "查询成功" + fabuID);
            BmobQuery<TaoKuang> query = new BmobQuery<>();
            query.addWhereDoesNotExists("goumai");
            query.addWhereEqualTo("fabu",user);
            query.order("-updatedAt");
            //包含作者信息
            query.include("fabu");
            query.findObjects(new FindListener<TaoKuang>() {

                @Override
                public void done(List<TaoKuang> object, BmobException e) {
                    if (e == null) {
                        sTaolist = object;
                        StaggeredGridLayoutManager slayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                        sRecyclerView.setLayoutManager(slayoutManager);
                        sAdapter = new HomeRecyclerViewAdapter(getContext(), sTaolist);
                        sRecyclerView.setAdapter(sAdapter);
                        LogUtils.d("查询", "查询成功" + object);
                        //Snackbar.make(fRecyclerView, "查询成功", Snackbar.LENGTH_LONG).show();
                    } else {
                        LogUtils.e("BMOB", e.toString());
                        Snackbar.make(sRecyclerView, e.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                }

            });
        } else {
            Snackbar.make(sRecyclerView, "请先登录", Snackbar.LENGTH_LONG).show();
        }
    }

}