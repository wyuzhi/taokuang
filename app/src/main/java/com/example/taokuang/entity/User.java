package com.example.taokuang.entity;

import cn.bmob.v3.BmobUser;

public class User extends BmobUser {
    private String nicheng;

    public String getNicheng() {
        return nicheng;
    }

    public void setNicheng(String nicheng) {
        this.nicheng = nicheng;
    }
}
