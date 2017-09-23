package com.hgy.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hgy.R;
import com.hgy.activity.LoginActivity;
import com.hgy.activity.MyInformationActivity;
import com.hgy.activity.SettingActivity;
import com.hgy.base.BaseFragment;
import com.hgy.db.MyUser;

import cn.bmob.v3.BmobUser;

/**
 * Created by asusnb on 2017/8/30.
 */

public class MineFragment extends BaseFragment implements View.OnClickListener{

    private LinearLayout user_info_counts;
    private TextView tvScore;
    private ImageView ivSex;
    private TextView tvUsername;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_mine;
    }

    @Override
    protected void initWidget(View mRoot) {
        super.initWidget(mRoot);
        ImageView ivSetting= (ImageView) mRoot.findViewById(R.id.mine_setting);
        ImageView ivUserPic= (ImageView) mRoot.findViewById(R.id.mine_userPic);
        LinearLayout myInfo= (LinearLayout) mRoot.findViewById(R.id.mine_myInfo);
        myInfo.setOnClickListener(this);
        ivSetting.setOnClickListener(this);
        ivUserPic.setOnClickListener(this);
        user_info_counts = (LinearLayout) mRoot.findViewById(R.id.mine_user_info_counts);
        tvScore = (TextView) mRoot.findViewById(R.id.tv_score);
        ivSex = (ImageView) mRoot.findViewById(R.id.iv_gender);
        tvUsername = (TextView) mRoot.findViewById(R.id.tv_username);

    }

    @Override
    protected void initData() {
        super.initData();

    }

    /**
     * 加载用户信息
     */
    private void loadUserInfo() {
        MyUser user= BmobUser.getCurrentUser(MyUser.class);
        if (user!=null){ //已登录
            tvUsername.setText(user.getUsername());
            int sexPic=user.getSex().equals("男")?R.mipmap.ic_male:R.mipmap.ic_female;
            ivSex.setVisibility(View.VISIBLE);
            ivSex.setImageResource(sexPic);
//            tvScore.setVisibility(View.VISIBLE);
            user_info_counts.setVisibility(View.VISIBLE);
        }else{ //未登录
            tvUsername.setText("点击头像登录");
            ivSex.setVisibility(View.GONE);
//            tvScore.setVisibility(View.GONE);
            user_info_counts.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserInfo();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.mine_setting: // 设置
                startActivity(new Intent(mActivity, SettingActivity.class));
                break;
            case R.id.mine_userPic: //用户头像
            case R.id.mine_myInfo:  //我的资料
                //如果未登录，则进入登录界面，登录则进入我的资料界面
                if (BmobUser.getCurrentUser(MyUser.class)==null) {
                    Intent intent = new Intent(mActivity, LoginActivity.class);
                    intent.putExtra("from", "MineFragment");
                    startActivity(intent);
                    break;
                }else{
                    Intent intent=new Intent(mActivity, MyInformationActivity.class);
                    startActivity(intent);
                    break;
                }
        }
    }
}
