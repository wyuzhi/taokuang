package com.flying.taokuang;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;

public class CollectionDialog extends Dialog {
    public CollectionDialog(Activity context) {
        super(context);
        setContentView(R.layout.collection_dialog_layout);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
