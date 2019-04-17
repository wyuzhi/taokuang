package com.flying.taokuang.entity;

import org.litepal.crud.LitePalSupport;

import java.util.Date;


public class CollectionBean extends LitePalSupport {
    public String getGood() {
        return good;
    }

    public void setGood(String good) {
        this.good = good;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    private String good;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    private String title;
    private String price;
    private Date createTime;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private String image;
}
