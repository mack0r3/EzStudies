package com.example.mackor.ezstudies;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mackor.ezstudies.BackEndTools.ViewPagerAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class CalculusFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;


    public CalculusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View inflatedView = inflater.inflate(R.layout.fragment_calculus, container, false);

        // Inflate the layout for this fragment

        tabLayout = (TabLayout)inflatedView.findViewById(R.id.tabLayout);
        viewPager = (ViewPager)inflatedView.findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getFragmentManager());
        viewPagerAdapter.addFragments(new DA1Fragment(), "DA1");
        viewPagerAdapter.addFragments(new DA2Fragment(), "DA2");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


        return inflatedView;
    }

}
