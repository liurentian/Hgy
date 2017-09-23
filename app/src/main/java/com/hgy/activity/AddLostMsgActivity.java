package com.hgy.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hgy.R;
import com.hgy.base.BaseActivity;
import com.hgy.db.Lost;
import com.hgy.db.MyUser;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by asusnb on 2017/8/31.
 */

public class AddLostMsgActivity extends BaseActivity {

    private EditText title;
    private EditText phone;
    private EditText describe;
    private int lostType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lost_msg);
        lostType = getIntent().getIntExtra("lostType",-1);  // -1 lost  found ==1
        initView();
    }

    private void initView() {
        title = (EditText) findViewById(R.id.lost_title);
        phone = (EditText) findViewById(R.id.lost_phone);
        describe = (EditText) findViewById(R.id.lost_describe);
        if (lostType==-1){
            title.setHint("我丢失了.....");
        }else{
            title.setHint("我捡到了.....");
        }
    }

    public void back(View view){
        finish();
    }
    public void saveServer(View view){
        String titleText=title.getText().toString().trim();
        String phoneNum=phone.getText().toString().trim();
        String description=describe.getText().toString().trim();
        if (TextUtils.isEmpty(titleText) || TextUtils.isEmpty(phoneNum)
                || TextUtils.isEmpty(description)){
            Toast.makeText(this,"不能为空!",Toast.LENGTH_SHORT).show();
            return;
        }
        Lost lost=new Lost();
        lost.setTitle(titleText);
        lost.setPhone(phoneNum);
        lost.setDescribe(description);
        lost.setPublisher(BmobUser.getCurrentUser(MyUser.class));
        lost.setType(lostType+"");
        lost.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    finish();
                }else{
                    Toast.makeText(AddLostMsgActivity.this,"添加失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
