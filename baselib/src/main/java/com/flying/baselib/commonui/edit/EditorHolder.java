package com.flying.baselib.commonui.edit;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;

import java.io.Serializable;

public class EditorHolder implements Serializable{
    int layoutResId;
    int cancelViewId;
    int submitViewId;
    int editTextId;
    public EditorHolder(@LayoutRes int layoutResId, @IdRes int cancelViewId,
                        @IdRes int submitViewId, @IdRes int editTextId){
        this.layoutResId = layoutResId;
        this.cancelViewId = cancelViewId;
        this.submitViewId = submitViewId;
        this.editTextId = editTextId;
    }
}
