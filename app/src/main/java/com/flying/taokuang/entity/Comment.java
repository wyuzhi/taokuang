package com.flying.taokuang.entity;

import cn.bmob.v3.BmobObject;

public class Comment extends BmobObject {

    private String content;
    private User author;
    private User commentator;

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
}
