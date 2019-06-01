package com.flying.baselib.commonui.edit;

import android.view.ViewGroup;

public interface EditorCallback {
    void onCancel();

    void onSubmit(String content);

    void onAttached(ViewGroup rootView);

    class Extend implements EditorCallback {

        @Override
        public void onCancel() {

        }

        @Override
        public void onSubmit(String content) {

        }

        @Override
        public void onAttached(ViewGroup rootView) {

        }
    }
}
