package com.example.mackor.ezstudies.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;

import com.example.mackor.ezstudies.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ManagePointsFragment extends Fragment {


    public ManagePointsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_manage_points, container, false);



        return inflatedView;
    }



}
