package com.example.taokuang.tool;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.example.taokuang.tool.MyApplication;

public class BaseFragment extends Fragment {
    private Activity activity;

    public Context getContext() {
        if (activity == null) {
            return MyApplication.getInstance();
        }
        return activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = getActivity();
    }

}
