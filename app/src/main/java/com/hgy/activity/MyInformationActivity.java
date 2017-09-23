package com.hgy.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hgy.R;
import com.hgy.base.BaseActivity;
import com.hgy.db.MyUser;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by asusnb on 2017/9/4.
 */

public class MyInformationActivity extends BaseActivity implements View.OnClickListener{
    public static final int PHOTO_FROM_GALLERY = 0x001;
    public static final int PHOTO_FROM_CAMERA = 0x002;
    private ImageView ivHeadPic;
    private TextView tvUserName;
    private ImageView ivGender;
    private TextView tvJoinDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_information);
        initView();
    }

    private void initView() {
        TextView tvTitle= (TextView) findViewById(R.id.tv_title);
        tvTitle.setText("我的资料");
        ImageView ivBack= (ImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);
        /**头像**/
        LinearLayout lyHeadPic= (LinearLayout) findViewById(R.id.ly_headPic);
        ivHeadPic = (ImageView) findViewById(R.id.myInfo_headPic);
        lyHeadPic.setOnClickListener(this);
        /**用户名**/
        tvUserName = (TextView) findViewById(R.id.myInfo_UserName);
        /**性别**/
        findViewById(R.id.myInfo_ll_UpdateGender).setOnClickListener(this);
        ivGender = (ImageView) findViewById(R.id.myInfo_gender);
        /** 加入时间**/
        tvJoinDate = (TextView) findViewById(R.id.myInfo_joinDate);

    }
    /**
     * 加载用户信息
     */
    private void loadUserInfo() {
        MyUser user= BmobUser.getCurrentUser(MyUser.class);
        if (user!=null) { //已登录
            tvUserName.setText(user.getUsername());
            int sexPic = user.getSex().equals("男") ? R.mipmap.ic_male : R.mipmap.ic_female;
            ivGender.setImageResource(sexPic);
            tvJoinDate.setText(user.getCreatedAt());
        }
    }
    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.iv_back: finish();  //标题，返回
                break;
            case R.id.ly_headPic: //修改头像
                 Intent intent = new Intent(this,SelectedImageActivity.class);
                 startActivityForResult(intent,PHOTO_FROM_GALLERY);
                break;
            case R.id.myInfo_ll_UpdateGender:  //修改性别
                updateGender();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case PHOTO_FROM_GALLERY:
                switch (resultCode){
                    case PHOTO_FROM_GALLERY:
                        String imgUri = data.getStringExtra("ImgUri");
                        ivHeadPic.setImageURI(Uri.parse(imgUri));
                        break;
                }
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserInfo();
    }

    /**
     * 修改性别
     */
    public void updateGender(){
        final ProgressDialog dialog=ProgressDialog.show(this,null,"正在提交数据...",true,false);
        MyUser user= BmobUser.getCurrentUser(MyUser.class);
        MyUser newUser=new MyUser();
        String sex=user.getSex().equals("男")? "女" :"男";
        newUser.setSex(sex);
        newUser.update(user.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    loadUserInfo();
                }else{
//                            toast("更新用户信息失败:" + e.getMessage());
                }
                dialog.dismiss();
            }
        });
    }
}
