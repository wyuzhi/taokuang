package com.flying.baselib.commonui.edit;

import android.support.annotation.StringRes;

import java.io.Serializable;

public class InputCheckRule implements Serializable {
    int minLength;  //  最少输入字符数
    int maxLength;  // 最多输入字符数
    String regxRule;    // 正则表达式校验
    int regxWarn;   // 正则表达式失败提示

    public InputCheckRule(int maxLength, int minLength) {
        this(maxLength, minLength, null, 0);
    }

    public InputCheckRule(int maxLength, int minLength, String regxRule, @StringRes int regxWarn) {
        if (maxLength < minLength || minLength < 0) {
            return;
        }
        this.maxLength = maxLength;
        this.minLength = minLength;
        this.regxRule = regxRule;
        this.regxWarn = regxWarn;
    }
}
