package com.example.taokuang.entity;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class TaoKuang extends BmobObject {
    private String biaoti;
    private String lianxi;
    private String miaoshu;
    private String weizhi;
    private String jiage;
    private String leibie;
    private String fabuname;
    private BmobFile picyi;
    private BmobFile picer;
    private BmobFile picsan;
    private User fabu;
    private User goumai;


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

    public BmobFile getPicyi() {
        return picyi;
    }

    public void setPicyi(BmobFile picyi) {
        this.picyi = picyi;
    }

    public User getFabu() {
        return fabu;
    }

    public void setFabu(User fabu) {
        this.fabu = fabu;
    }

    public User getGoumai() {
        return goumai;
    }

    public void setGoumai(User goumai) {
        this.goumai = goumai;
    }

    public String getLianxi() {
        return lianxi;
    }

    public void setLianxi(String lianxi) {
        this.lianxi = lianxi;
    }

    public String getLeibie() {
        return leibie;
    }

    public void setLeibie(String leibie) {
        this.leibie = leibie;
    }

    public String getFabuname() {
        return fabuname;
    }

    public void setFabuname(String fabuname) {
        this.fabuname = fabuname;
    }
}
