package com.hgy.db;

import cn.bmob.v3.BmobUser;

/**
 * Created by asusnb on 2017/8/29.
 */

public class MyUser extends BmobUser {
    private String sex;



    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
