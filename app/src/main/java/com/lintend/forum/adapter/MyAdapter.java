package com.lintend.forum.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.lintend.forum.Activity.AboutTabActivity;
import com.lintend.forum.Activity.MyPostActivity;

public class MyAdapter extends FragmentStatePagerAdapter {
    public MyAdapter(FragmentActivity activity, FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            return new AboutTabActivity();
        }else if(position==1) {
            return new MyPostActivity();
            }else {
            return null;
        }

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "About";
        } else if (position == 1) {
            return "My Post";
            } else {
            return null;
        }
    }

    //    defines number of tabs
    @Override
    public int getCount() {
        return 2;
    }
}

