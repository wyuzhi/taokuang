package com.flying.taokuang.entity;

import cn.bmob.v3.BmobObject;

public class Comment extends BmobObject {

    private String content;
    private User author;
    private User commentator;
    private TaoKuang good;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public User getCommentator() {
        return commentator;
    }

    public void setCommentator(User commentator) {
        this.commentator = commentator;
    }

    public TaoKuang getGood() {
        return good;
    }

    public void setGood(TaoKuang good) {
        this.good = good;
    }
}
