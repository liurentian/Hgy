package com.hgy.db;

import cn.bmob.v3.BmobUser;

/**
 * Created by asusnb on 2017/8/29.
 */

public class MyUser extends BmobUser {
    private Boolean sex;



    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }
}
