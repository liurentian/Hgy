package com.hgy.main;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;

import com.hgy.R;
import com.hgy.adapter.NoScrollPagerAdapter;
import com.hgy.fragment.ExploreFragment;
import com.hgy.fragment.MineFragment;
import com.hgy.fragment.NewsFragment;
import com.hgy.fragment.TweetFragment;
import com.hgy.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Fragment> list=new ArrayList<>();
    private NoScrollViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
    }

    private void initData() {
        list.add(new NewsFragment());
        list.add(new TweetFragment());
        list.add(new ExploreFragment());
        list.add(new MineFragment());
    }

    private void initView() {
        viewPager = (NoScrollViewPager) findViewById(R.id.main_container);
        NoScrollPagerAdapter adapter=new NoScrollPagerAdapter(getSupportFragmentManager(),list);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0,false);
        RadioGroup rg= (RadioGroup) findViewById(R.id.main_rg);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i){
                    case R.id.main_tab_news:
                        viewPager.setCurrentItem(0,false);
                        break;
                    case R.id.main_tab_tweet:
                        viewPager.setCurrentItem(1,false);
                        break;
                    case R.id.main_tab_explore:
                        viewPager.setCurrentItem(2,false);
                        break;
                    case R.id.main_tab_me:
                        viewPager.setCurrentItem(3,false);
                        break;
                }
            }
        });
    }
}
