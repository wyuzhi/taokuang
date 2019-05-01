package com.flying.taokuang.entity;

import android.text.TextUtils;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class User extends BmobUser {
    private String nicheng;
    private String mima;
    private String xh;
    private String phone;
    private BmobFile icon;
    private BmobFile xsz;
    private Boolean renz;

    public String getNicheng() {
        if (TextUtils.isEmpty(nicheng)) {
            return "用户" + getObjectId();
        }
        return nicheng;
    }

    public void setNicheng(String nicheng) {
        this.nicheng = nicheng;
    }

    public BmobFile getIcon() {
        return icon;
    }

    public void setIcon(BmobFile icon) {
        this.icon = icon;
    }

    public Boolean getRenz() {
        return renz;
    }

    public void setRenz(Boolean renz) {
        this.renz = renz;
    }

    public BmobFile getXsz() {
        return xsz;
    }

    public void setXsz(BmobFile xsz) {
        this.xsz = xsz;
    }

    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getMima() {
        return mima;
    }

    public void setMima(String mima) {
        this.mima = mima;
    }
}
