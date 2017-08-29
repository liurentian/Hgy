package com.hgy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hgy.base.BaseActivity;
import com.hgy.db.MyUser;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by asusnb on 2017/8/29.
 */

public class RegisterActivity extends BaseActivity {

    private EditText register_email;
    private EditText register_username;
    private EditText register_password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        register_email = (EditText) findViewById(R.id.register_input_email);
        register_username = (EditText) findViewById(R.id.register_input_username);
        register_password = (EditText) findViewById(R.id.register_input_username);
    }

    public void register(View view){
        String email=register_email.getText().toString().trim();
        String username=register_username.getText().toString().trim();
        String password=register_password.getText().toString().trim();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
            // TODO  吐司提示
            Toast.makeText(this,"要输入的内容不能为空!",Toast.LENGTH_SHORT).show();
            return;
        }
        MyUser user=new MyUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.signUp(new SaveListener<MyUser>() {
            @Override
            public void done(MyUser myUser, BmobException e) {
                if(e==null){
                    finish();
                }else{
                    Log.e("bmob",e.toString());
                }
            }
        });
    }
}
