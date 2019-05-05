package com.flying.taokuang.Fragement;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flying.baselib.utils.app.LogUtils;
import com.flying.taokuang.Adapter.PersonalSellingRecyclerviewAdapter;
import com.flying.taokuang.UserPageActivity;
import com.flying.taokuang.R;
import com.flying.taokuang.entity.TaoKuang;
import com.flying.taokuang.entity.User;
import com.flying.taokuang.ui.EmptyRecyclerViewHelper;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 个人主页的在售 Fragment
 */
public class PersonalSellingFragment extends Fragment {
    private String fabuID;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fabuID = ((UserPageActivity) activity).getUserID();
    }


    private RecyclerView sRecyclerView;
    private PersonalSellingRecyclerviewAdapter sAdapter;
    private SwipeRefreshLayout sSwipeRefresh;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selling, container, false);
        initView(view);
        return view;
    }

    private void initView(final View v) {
        sRecyclerView = v.findViewById(R.id.recycler_tao_s);
        sRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        sAdapter = new PersonalSellingRecyclerviewAdapter(getContext());
        sRecyclerView.setAdapter(sAdapter);
        EmptyRecyclerViewHelper.with(sRecyclerView);
        loadDate();
    }

    private void loadDate() {
        if (BmobUser.isLogin()) {
            User user = new User();
            user.setObjectId(fabuID);
            LogUtils.d("查询", "查询成功" + fabuID);
            BmobQuery<TaoKuang> query = new BmobQuery<>();
            query.addWhereDoesNotExists("goumai");
            query.addWhereEqualTo("fabu", user);
            query.order("-updatedAt");
            //包含作者信息
            query.include("fabu");
            query.findObjects(new FindListener<TaoKuang>() {

                @Override
                public void done(List<TaoKuang> object, BmobException e) {
                    if (e == null) {
                        sAdapter.addData(object);
                    }
                }

            });
        } else {
            Snackbar.make(sRecyclerView, "请先登录", Snackbar.LENGTH_LONG).show();
        }
    }

}