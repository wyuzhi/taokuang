package com.flying.taokuang.entity;

import cn.bmob.v3.BmobObject;

public class Collection extends BmobObject {
    private User people;
    private TaoKuang goods;



    public User getPeople() {
        return people;
    }

    public void setPeople(User people) {
        this.people = people;
    }

    public TaoKuang getGoods() {
        return goods;
    }

    public void setGoods(TaoKuang goods) {
        this.goods = goods;
    }
}
