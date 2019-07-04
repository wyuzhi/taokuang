package com.flying.taokuang.entity;

import org.litepal.crud.LitePalSupport;

import java.util.Date;


public class CollectionBean extends LitePalSupport {
    private String title;
    private String price;
    private Date createTime;
    private String good;
    private String image;
    private String content;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
