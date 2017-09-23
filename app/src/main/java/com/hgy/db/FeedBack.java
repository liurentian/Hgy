package com.hgy.db;

import cn.bmob.v3.BmobObject;

/**
 * 意见反馈 实体类
 * Created by liurentian on 2017/9/23.
 */

public class FeedBack extends BmobObject {
    private String content; //反馈内容
    private MyUser publisher;  //反馈的用户
    private String type;  //反馈类型 程序错误 功能建议
//    private String ImgDescribUrl;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
