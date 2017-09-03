package com.hgy.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by asusnb on 2017/8/30.
 */

public abstract class BaseFragment extends Fragment {
    public Activity mActivity;
    protected View mRoot;
    protected LayoutInflater mInflater;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mActivity=getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        inflater.inflate()
        if (mRoot != null) {
            ViewGroup parent = (ViewGroup) mRoot.getParent();
            if (parent != null)
                parent.removeView(mRoot);
        } else {
            mRoot = inflater.inflate(getLayoutId(), container, false);
            mInflater = inflater;
            initWidget(mRoot);
            initData();
        }
        return mRoot;
    }

    protected void initData() {

    }

    protected  void initWidget(View mRoot){

    }

    protected abstract int getLayoutId();

}
