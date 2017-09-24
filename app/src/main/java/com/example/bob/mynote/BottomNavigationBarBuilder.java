package com.example.bob.mynote;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bob on 2017/6/1.
 */

public class BottomNavigationBarBuilder {

    private List<Fragment> fragments = new ArrayList<>();
    private ViewPager viewPager;
    private FragmentManager fragmentManager;
    private View mLastClickedView;
    private List<View> mItems = new ArrayList<>();

    public BottomNavigationBarBuilder(ViewPager viewPager, FragmentManager fragmentManager) {
        this.viewPager = viewPager;
        this.fragmentManager = fragmentManager;
    }

    public BottomNavigationBarBuilder addItem(View item, Fragment fragment){
        final int position = fragments.size();
        mItems.add(item);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(position);
            }
        });
        fragments.add(fragment);
        return this;
    }

    public void build(){
        viewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(mLastClickedView != null){
                    mLastClickedView.setSelected(false);
                }
                View v = mItems.get(position);
                v.setSelected(true);
                mLastClickedView = v;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
