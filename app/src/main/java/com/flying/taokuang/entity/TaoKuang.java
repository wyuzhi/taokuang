package com.flying.taokuang.entity;

import java.util.List;

import cn.bmob.v3.BmobObject;

public class TaoKuang extends BmobObject {
    private int gengxin;
    private String biaoti;
    private String lianxi;
    private String miaoshu;
    private String weizhi;
    private String jiage;
    private String leibie;
    private User fabu;
    private User goumai;
    private Boolean jiaoyi;
    private Boolean buy;
    private String gezi;
    private List<String> pic;
    private String type;



    public String getBiaoti() {
        return biaoti;
    }

    public void setBiaoti(String biaoti) {
        this.biaoti = biaoti;
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

    public int getGengxin() {
        return gengxin;
    }

    public void setGengxin(int gengxin) {
        this.gengxin = gengxin;
    }

    public String getGezi() {
        return gezi;
    }

    public void setGezi(String gezi) {
        this.gezi = gezi;
    }

    public Boolean getJiaoyi() {
        return jiaoyi;
    }

    public void setJiaoyi(Boolean jiaoyi) {
        this.jiaoyi = jiaoyi;
    }

    public List<String> getPic() {
        return pic;
    }

    public void setPic(List<String> pic) {
        this.pic = pic;
    }

    public Boolean getBuy() {
        return buy;
    }

    public void setBuy(Boolean buy) {
        this.buy = buy;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
