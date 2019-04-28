package com.flying.baselib.utils.device;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

public final class ClipboardUtils {

    private ClipboardUtils() {
        throw new AssertionError("No instances.");
    }

    public static boolean copy(@NonNull Context context, @NonNull CharSequence label, @NonNull CharSequence text) {
        if (context == null) {
            return false;
        }
        if (TextUtils.isEmpty(text)) {
            return false;
        }
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, text);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
            return true;
        }
        return false;
    }

}
