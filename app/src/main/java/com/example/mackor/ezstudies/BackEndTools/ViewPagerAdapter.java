package com.example.mackor.ezstudies.BackEndTools;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Korzonkie on 2016-02-17.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    List<ListFragment> fragmentsList = new ArrayList<>();
    List<String> tabTitles = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm, List<ListFragment> fragmentsList, List<String> tabTitles) {
        super(fm);
        this.fragmentsList = fragmentsList;
        this.tabTitles = tabTitles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentsList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentsList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles.get(position);
    }
}
