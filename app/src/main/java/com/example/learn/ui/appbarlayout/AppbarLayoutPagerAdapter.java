package com.example.learn.ui.appbarlayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class AppbarLayoutPagerAdapter extends FragmentPagerAdapter {

    List<Fragment> list = new ArrayList<>();

    public AppbarLayoutPagerAdapter(FragmentManager fm) {
        super(fm);
        list.add(new AppbarLayoutFragment());
        list.add(new AppbarLayoutFragment());
        list.add(new AppbarLayoutFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
