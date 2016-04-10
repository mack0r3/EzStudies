package com.example.mackor.ezstudies.Fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mackor.ezstudies.BackEndTools.ViewPagerAdapter;
import com.example.mackor.ezstudies.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CalculusFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    View inflatedView;

//    String indexNo;
//    int points;


    public CalculusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View inflatedView = inflater.inflate(R.layout.fragment_calculus, container, false);


//        indexNo = getArguments().getString("indexNo");
//        points = getArguments().getInt("points");

        // Inflate the layout for this fragment

        tabLayout = (TabLayout)inflatedView.findViewById(R.id.tabLayout);
        viewPager = (ViewPager)inflatedView.findViewById(R.id.viewPager);

        List<Fragment> fragmentsList = getFragmentsList();
        List<String> tabTitles = getTabTitles();

        viewPagerAdapter = new ViewPagerAdapter(getFragmentManager(), getFragmentsList(), getTabTitles());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        return inflatedView;
    }

    public ViewPager getViewAdapter()
    {
        return viewPager;
    }

    private List<Fragment> getFragmentsList()
    {
        List<Fragment> fragmentsList = new ArrayList<>();
        fragmentsList.add(new UserScoreFragment());
        fragmentsList.add(new GeneralScoreFragment());


        return fragmentsList;
    }
    private List<String> getTabTitles()
    {
        List<String> tabTitles = new ArrayList<String>();
        tabTitles.add("Activity");
        tabTitles.add("General");
        return tabTitles;
    }

}
