package com.hgy.activity;

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

/**
 * Created by asusnb on 2017/9/4.
 */

public class MyInformationActivity extends BaseActivity implements View.OnClickListener{
    private static final int PHOTO_FROM_GALLERY = 1;
    private static final int PHOTO_FROM_CAMERA = 2;
    private ImageView ivHeadPic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_information);
        initView();
    }

    private void initView() {
        ImageView ivBack= (ImageView) findViewById(R.id.iv_back);
        LinearLayout lyHeadPic= (LinearLayout) findViewById(R.id.ly_headPic);
        ivHeadPic = (ImageView) findViewById(R.id.myInfo_headPic);
        lyHeadPic.setOnClickListener(this);
        ivHeadPic.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        TextView tvTitle= (TextView) findViewById(R.id.tv_title);
        tvTitle.setText("我的资料");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back: finish();  //标题返回 id
                break;
            case R.id.myInfo_headPic:
            case R.id.ly_headPic:
                 Intent intent = new Intent(this,SelectedImageActivity.class);
                 startActivity(intent);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case PHOTO_FROM_GALLERY:
                if (resultCode==RESULT_OK){
                    if (data != null) {
                        Uri uri = data.getData();
                        ivHeadPic.setImageURI(uri);
                    }
                }else {

                }
                break;

        }
    }
}