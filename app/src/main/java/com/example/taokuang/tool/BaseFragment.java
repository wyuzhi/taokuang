package com.example.taokuang.tool;

import android.app.Activity;
import android.content.Context;

import com.jph.takephoto.app.TakePhotoFragment;

public class BaseFragment extends TakePhotoFragment {
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
