package com.hgy.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.hgy.R;
import com.hgy.adapter.LostLVAdapter;
import com.hgy.base.BaseActivity;
import com.hgy.db.Lost;
import com.hgy.db.MyUser;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by asusnb on 2017/8/31.
 */

public class LostAndFoundActivity extends BaseActivity {
    private final int QUEST_LOGIN_FROM_ADD_LOST=0x001;
    private int type=-1;  //默认为 -1 丢失界面  1为 找回界面
    private List<Lost> lostList=new ArrayList<>();
    private LostLVAdapter adapter;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_and_found);
        initView();
//        initData();
    }

    /**
     * 请求服务器 获得数据
     */
    private void queryData() {
        progressDialog = ProgressDialog.show(this,null,"加载中....",true,false);
        BmobQuery<Lost> query=new BmobQuery<>();
        query.addWhereEqualTo("type",type+"");
        query.order("-createdAt");
        query.findObjects(new FindListener<Lost>() {
            @Override
            public void done(List<Lost> list, BmobException e) {
                if(e==null){
                    lostList.clear();
                    lostList.addAll(list);
                    adapter.notifyDataSetChanged();
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
                progressDialog.dismiss();
            }
        });
    }

    private void initView() {
        final TextView lost_tab= (TextView) findViewById(R.id.lost_tab);
        final TextView found_tab= (TextView) findViewById(R.id.found_tab);
        lost_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lost_tab.setEnabled(false);
                found_tab.setEnabled(true);
                type=-1;
                queryData();
            }
        });
        found_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lost_tab.setEnabled(true);
                found_tab.setEnabled(false);
                type=1;
                queryData();
            }
        });
        ListView listView= (ListView) findViewById(R.id.listView);
        adapter = new LostLVAdapter(this,lostList);
        listView.setAdapter(adapter);
    }

    //添加信息
    public void addMessage(View view){
        //判断是否登录 然后跳转
        addMessage();
    }

    private void addMessage() {
        MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
        if(userInfo != null){
            // 允许用户使用应用
            Intent intent=new Intent(this,AddLostMsgActivity.class);
            intent.putExtra("lostType",type);
            startActivity(intent);
        }else{
            //缓存用户对象为空时， 可打开用户注册界面…
            Intent intent=new Intent(this,LoginActivity.class);
            intent.putExtra("from","LostAndFoundActivity");
            startActivityForResult(intent,QUEST_LOGIN_FROM_ADD_LOST);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryData();
    }

    public void back(View view){
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==QUEST_LOGIN_FROM_ADD_LOST && resultCode==LoginActivity.LOGIN_RESULT_CODE){
            addMessage();
        }
    }
}
