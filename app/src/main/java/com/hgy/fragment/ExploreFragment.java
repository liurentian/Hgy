package com.hgy.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hgy.R;
import com.hgy.activity.LostAndFoundActivity;
import com.hgy.base.BaseFragment;

/**
 * Created by asusnb on 2017/8/30.
 */

public class ExploreFragment extends BaseFragment implements View.OnClickListener{
    
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_explore;
    }

    @Override
    protected void initWidget(View mRoot) {
        super.initWidget(mRoot);
        TextView title= (TextView) mRoot.findViewById(R.id.title);
        title.setText(getString(R.string.main_tab_name_explore));
        LinearLayout found_lost= (LinearLayout) mRoot.findViewById(R.id.lost_found_ll);
        found_lost.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.lost_found_ll:
                startActivity(new Intent(mActivity, LostAndFoundActivity.class));
                break;
        }
    }
}
