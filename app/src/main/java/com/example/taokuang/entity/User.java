package com.example.taokuang.entity;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class User extends BmobUser {
    private String nicheng;
    private String xh;
    private BmobFile icon;
    private BmobFile xsz;
    private Boolean renz;

    public String getNicheng() {
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
}
