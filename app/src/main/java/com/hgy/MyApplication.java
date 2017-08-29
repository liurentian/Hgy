package com.hgy;

import android.app.Application;

import cn.bmob.v3.Bmob;

/**
 * Created by asusnb on 2017/8/29.
 */

public class MyApplication extends Application{
    public static String USER_NAME="";   //当前登录的用户名

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化 Bmob SDK，第一个参数为上下文，第二个参数为Application ID
        Bmob.initialize(this, Global.APP_ID);
    }
}
