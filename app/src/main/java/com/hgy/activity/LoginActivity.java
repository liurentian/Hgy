package com.hgy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.hgy.R;
import com.hgy.base.BaseActivity;
import com.hgy.db.MyUser;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText login_username;
    private EditText login_password;
    private Button login;
    private Button register;
    private String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        from = getIntent().getStringExtra("from");
        initView();
    }

    private void initView() {
        login_username = (EditText) findViewById(R.id.login_input_username);
        login_password = (EditText) findViewById(R.id.login_input_password);
        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);
        ImageButton back= (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(this);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    private void Login() {
        String username=login_username.getText().toString().trim();
        String password=login_password.getText().toString().trim();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
            Toast.makeText(this,"手机号或密码不能为空!",Toast.LENGTH_SHORT).show();
            return;
        }
        MyUser.loginByAccount(username, password, new LogInListener<MyUser>() {
            @Override
            public void done(MyUser myUser, BmobException e) {
                if(myUser!=null){
                    Log.i("smile","用户登陆成功");
                    if (from.equals("LostAndFoundActivity")){
                        setResult(001);
                    }
                    finish();
                }else {
                    Toast.makeText(LoginActivity.this,"帐号或密码错误",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.login:
                Login();
                break;
            case R.id.register:
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivityForResult(intent,000);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==000 && resultCode==999){
            login_username.setText(data.getStringExtra("username"));
        }
    }
}
