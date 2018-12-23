package com.lintend.forum.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.lintend.forum.R;
import com.lintend.forum.adapter.MyAdapter;


public class About_swipable_activity extends Fragment {
    PagerSlidingTabStrip pagerslide;
    ViewPager viewPager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.about_swipelayout, null);
        pagerslide = v.findViewById(R.id.pager_tab);
        viewPager = v.findViewById(R.id.viewpager);
        FragmentManager fm =getChildFragmentManager();
        viewPager.setAdapter(new MyAdapter(getActivity(),fm));
        pagerslide.setViewPager(viewPager);
        return v;
}

}
