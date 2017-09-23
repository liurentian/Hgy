package com.hgy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
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
    private final int QUEST_LOGIN_FROM_FEEDBACK=0x001;
    private LinearLayout signUp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    private void initView() {
        findViewById(R.id.iv_back).setOnClickListener(this);  //Title Back
        signUp = (LinearLayout) findViewById(R.id.setting_sign_up_ll); //注销
        signUp.setOnClickListener(this);
        findViewById(R.id.setting_ll_feedBack).setOnClickListener(this); //意见反馈

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
            case R.id.setting_sign_up_ll: //注销
                MyUser.logOut();
                Toast.makeText(this,"注销成功",Toast.LENGTH_SHORT).show();
                checkIsLogin();
                break;
            case R.id.iv_back: //返回键
                finish();
                break;
            case R.id.setting_ll_feedBack: //意见反馈
                toFeedBack();
                break;
        }
    }

    /**
     * 判断是否登录 跳转到意见反馈
     */
    private void toFeedBack() {
        MyUser user=BmobUser.getCurrentUser(MyUser.class);
        if (user!=null) {
            startActivity(new Intent(this, FeedBackActivity.class));
        }else {
            Intent intent=new Intent(this,LoginActivity.class);
            intent.putExtra("from","QUEST_LOGIN_FROM_FEEDBACK");
            startActivityForResult(intent,QUEST_LOGIN_FROM_FEEDBACK);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == QUEST_LOGIN_FROM_FEEDBACK || resultCode==LoginActivity.LOGIN_RESULT_CODE){
            toFeedBack();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkIsLogin();
    }
}
