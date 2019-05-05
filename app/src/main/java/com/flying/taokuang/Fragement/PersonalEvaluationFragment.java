package com.flying.taokuang.Fragement;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flying.baselib.utils.app.LogUtils;
import com.flying.taokuang.Adapter.CommentAdapter;
import com.flying.taokuang.UserPageActivity;
import com.flying.taokuang.R;
import com.flying.taokuang.entity.Comment;
import com.flying.taokuang.entity.User;
import com.flying.taokuang.ui.EmptyRecyclerViewHelper;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 个人主页的评价 Fragment
 */
public class PersonalEvaluationFragment extends Fragment {
    private String fabuID;
    private String goumaiID;
    private RecyclerView sRecyclerView;
    private CommentAdapter sAdapter;
    private List<Comment> sTaolist;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fabuID = ((UserPageActivity) activity).getUserID();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_evaluate, container, false);
        initView(view);
        return view;
    }

    private void initView(final View v) {
        sTaolist = new ArrayList<>();
        sRecyclerView = v.findViewById(R.id.recycler_tao_e);
        LinearLayoutManager slayoutManager = new LinearLayoutManager(getContext());
        sRecyclerView.setLayoutManager(slayoutManager);
        sAdapter = new CommentAdapter(getContext());
        sRecyclerView.setAdapter(sAdapter);
        EmptyRecyclerViewHelper.with(sRecyclerView);
        loadDate();
    }

    private void loadDate() {
        BmobQuery<Comment> tQuery = new BmobQuery<>();
        User user = new User();
        user.setObjectId(fabuID);
        tQuery.addWhereEqualTo("commentator", user);
        tQuery.order("-updatedAt");
        tQuery.include("author,commentator");
        tQuery.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> list, BmobException e) {
                if (e == null) {
                    sAdapter.addData(list);
                }
            }
        });
    }

}
