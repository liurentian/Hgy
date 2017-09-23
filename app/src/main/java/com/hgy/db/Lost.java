package com.hgy.db;

import cn.bmob.v3.BmobObject;

/**
 * Created by liurentian on 2017/8/31.
 */

public class Lost extends BmobObject {
    private String title; //标题
    private String phone;  //手机
    private String describe;  //联系手机
    private MyUser publisher;  //发布信息者的用户名
    private String type;  //帖子类型 -1 为丢失物品的帖子 1为 捡到物品的帖子
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public MyUser getPublisher() {
        return publisher;
    }

    public void setPublisher(MyUser publisher) {
        this.publisher = publisher;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
