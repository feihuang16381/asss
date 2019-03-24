package com.cqut.icode.asss_android.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 作者：hwl
 * 时间：2017/7/9:18:51
 * 邮箱：1097412672@qq.com
 * 说明：
 */
public class ContentPagerAdapter extends FragmentPagerAdapter {
    private List<String> tabIndicators;
    private Context context;
    private List<Fragment> tabFragments;

    
    public ContentPagerAdapter(FragmentManager fm,Context context,
                               List<Fragment> tabFragments,List<String> tabIndicators) {
        super(fm);
        this.context = context;
        this.tabFragments = tabFragments;
        this.tabIndicators = tabIndicators;
    }

    @Override
    public Fragment getItem(int position) {
        return tabFragments.get(position);
    }

    @Override
    public int getCount() {
        return tabFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabIndicators.get(position);
    }
}
