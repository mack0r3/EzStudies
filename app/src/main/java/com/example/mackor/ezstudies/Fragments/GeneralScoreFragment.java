package com.example.mackor.ezstudies.Fragments;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mackor.ezstudies.Activities.MainActivity;
import com.example.mackor.ezstudies.Activities.UserPanelActivity;
import com.example.mackor.ezstudies.BackEndTools.CustomJSONAdapter;
import com.example.mackor.ezstudies.BackEndTools.Networking;
import com.example.mackor.ezstudies.BackEndTools.UserSessionManager;
import com.example.mackor.ezstudies.BackEndTools.ViewPagerAdapter;
import com.example.mackor.ezstudies.R;

import org.json.JSONArray;
import org.json.JSONException;


/**
 * A simple {@link Fragment} subclass.
 */
public class GeneralScoreFragment extends Fragment {


    public GeneralScoreFragment() {
        // Required empty public constructor
    }

    String indexNo;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View inflatedView = inflater.inflate(R.layout.fragment_general_score, container, false);
        // Inflate the layout for this fragment

        ProgressBar progressBar = (ProgressBar)inflatedView.findViewById(R.id.myProgressBar);

        UserSessionManager userSessionManager = new UserSessionManager(getContext(), getActivity());
        indexNo = userSessionManager.getIndexNo();


        Networking addPoint = (Networking) new Networking(progressBar, getContext(), new Networking.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                try {
                    JSONArray jsonArr = new JSONArray(output);
                    CustomJSONAdapter myAdapter = new CustomJSONAdapter(indexNo, jsonArr, getContext());
                    ListView listView = (ListView)inflatedView.findViewById(R.id.usersPoints);
                    listView.setAdapter(myAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).execute("GET_USERS_POINTS", "DA1");

        return inflatedView;
    }


}
