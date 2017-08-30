package com.hgy.fragment;

import android.view.View;
import android.widget.TextView;

import com.hgy.R;
import com.hgy.base.BaseFragment;

/**
 * Created by asusnb on 2017/8/30.
 */

public class TweetFragment extends BaseFragment {
    
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_tweet;
    }

    @Override
    protected void initWidget(View mRoot) {
        super.initWidget(mRoot);
        TextView title= (TextView) mRoot.findViewById(R.id.title);
        title.setText(getString(R.string.main_tab_name_tweet));
    }
}
