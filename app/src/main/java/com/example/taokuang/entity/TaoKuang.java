package com.example.taokuang.entity;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class TaoKuang extends BmobObject {
    private String fabuzhu;//发布者
    private String biaoti;
    private String miaoshu;
    private String weizhi;
    private String jiage;
    private BmobFile picyi;
    private BmobFile picer;
    private BmobFile picsan;


    public String getBiaoti() {
        return biaoti;
    }

    public void setBiaoti(String biaoti) {
        this.biaoti = biaoti;
    }

    public BmobFile getPicsan() {
        return picsan;
    }

    public void setPicsan(BmobFile picsan) {
        this.picsan = picsan;
    }

    public String getMiaoshu() {
        return miaoshu;
    }

    public void setMiaoshu(String miaoshu) {
        this.miaoshu = miaoshu;
    }

    public String getWeizhi() {
        return weizhi;
    }

    public void setWeizhi(String weizhi) {
        this.weizhi = weizhi;
    }

    public String getJiage() {
        return jiage;
    }

    public void setJiage(String jiage) {
        this.jiage = jiage;
    }



    public BmobFile getPicer() {
        return picer;
    }

    public void setPicer(BmobFile picer) {
        this.picer = picer;
    }

    public String getFabuzhu() {
        return fabuzhu;
    }

    public void setFabuzhu(String fabuzhu) {
        this.fabuzhu = fabuzhu;
    }

    public BmobFile getPicyi() {
        return picyi;
    }

    public void setPicyi(BmobFile picyi) {
        this.picyi = picyi;
    }
}
