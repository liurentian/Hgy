package com.hgy.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hgy.R;
import com.hgy.base.BaseActivity;
import com.hgy.db.FeedBack;
import com.hgy.db.MyUser;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by asusnb on 2017/9/23.
 */

public class FeedBackActivity extends BaseActivity implements View.OnClickListener{

    private RadioButton rb_error;
    private RadioButton rb_advise;
    private EditText etContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        initView();
    }

    private void initView() {
        TextView tvTitle= (TextView) findViewById(R.id.tv_title);
        tvTitle.setText("意见反馈");
        findViewById(R.id.iv_back).setOnClickListener(this);
        rb_error = (RadioButton) findViewById(R.id.rb_error);
        rb_advise = (RadioButton) findViewById(R.id.rb_advise);
        etContent = (EditText) findViewById(R.id.et_feed_back);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back: finish();
                break;
            case R.id.btn_commit:
                commitSuggestion(); //提交意见
                break;
        }
    }
    /**提交意见*/
    private void commitSuggestion() {
        if (rb_error.isChecked() || rb_advise.isChecked()){
            if (TextUtils.isEmpty(etContent.getText().toString().trim())){
                Toast.makeText(this,"反馈的内容不能为空!",Toast.LENGTH_SHORT).show();
                return;
            }
            String type=rb_error.isChecked()?"程序错误":"功能建议";
            FeedBack feedBack=new FeedBack();
            feedBack.setContent(etContent.getText().toString().trim());
            feedBack.setPublisher(BmobUser.getCurrentUser(MyUser.class));
            feedBack.setType(type);
            feedBack.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                   if (e==null){
                       //提交成功
                       finish();
                   }else{
                       Toast.makeText(FeedBackActivity.this,"提交失败，请检查您的网络!",Toast.LENGTH_SHORT).show();
                   }
                }
            });
        }else{
            Toast.makeText(this,"请选择反馈意见的类型!",Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
