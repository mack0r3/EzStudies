package com.example.mackor.ezstudies.Fragments;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mackor.ezstudies.BackEndTools.Networking;
import com.example.mackor.ezstudies.BackEndTools.ViewPagerAdapter;
import com.example.mackor.ezstudies.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class GeneralScoreFragment extends Fragment {


    public GeneralScoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_general_score, container, false);
        // Inflate the layout for this fragment

        TextView tv = (TextView)inflatedView.findViewById(R.id.textView);
        ProgressBar progressBar = (ProgressBar)inflatedView.findViewById(R.id.myProgressBar);

        Networking addPoint = (Networking) new Networking(progressBar, getContext(), new Networking.AsyncResponse() {
            @Override
            public void processFinish(String output) {
            }
        }).execute("GET_USER_INFO", "276946");

        return inflatedView;
    }


}
