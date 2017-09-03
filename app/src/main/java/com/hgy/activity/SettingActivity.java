package com.hgy.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hgy.R;
import com.hgy.base.BaseActivity;
import com.hgy.db.MyUser;

import cn.bmob.v3.BmobUser;

/**
 * Created by asusnb on 2017/9/2.
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener{

    private LinearLayout signUp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    private void initView() {
        ImageView ivBack= (ImageView) findViewById(R.id.iv_back);

        signUp = (LinearLayout) findViewById(R.id.setting_sign_up_ll);
        signUp.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        checkIsLogin();
    }

    private void checkIsLogin() {
        MyUser user=BmobUser.getCurrentUser(MyUser.class);
        if (user==null){
            signUp.setVisibility(View.GONE);
        }else {
            signUp.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.setting_sign_up_ll:
                MyUser.logOut();
                Toast.makeText(this,"注销成功",Toast.LENGTH_SHORT).show();
                checkIsLogin();
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
