package com.example.mackor.ezstudies.Fragments;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mackor.ezstudies.Activities.AddPointsActivity;
import com.example.mackor.ezstudies.Activities.UserPanelActivity;
import com.example.mackor.ezstudies.BackEndTools.Networking;
import com.example.mackor.ezstudies.BackEndTools.UserSessionManager;
import com.example.mackor.ezstudies.FrontEndTools.FontManager;
import com.example.mackor.ezstudies.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.BatchUpdateException;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserScoreFragment extends Fragment {

    View inflatedView;
    int REFRESH_VIEWPAGER_REQUEST = 1;

    int pointsCounter = 0;
    int currentPoints;

    String indexNo;


    public UserScoreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_user_score, container, false);

        final TextView userActivityPointsTextView = (TextView) inflatedView.findViewById(R.id.userActivityPointsTextView);
        final ProgressBar progressBar = (ProgressBar) inflatedView.findViewById(R.id.myProgressBar);
//        Switch publicResultSwitch = (Switch) inflatedView.findViewById(R.id.publicResultSwitch);


//        publicResultSwitch.setTextOff("OFF");
//        publicResultSwitch.setTextOn("ON");


        //Retrieve logged user info
        String method = "GET_USER_INFO";
        indexNo = new UserSessionManager(getContext()).getIndexNo();

        Networking getUserInfo = (Networking) new Networking(progressBar, getContext(), new Networking.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                try {
                    JSONObject json = new JSONObject(output);
                    if(json.has("success"))
                    {
                        Toast toast = Toast.makeText(getContext(), json.getString("message"), Toast.LENGTH_LONG);
                        toast.show();
                    } else {
                        inflatedView.findViewById(R.id.userScoreFragmentContainer).setVisibility(View.VISIBLE);
                        currentPoints = Integer.parseInt(json.getString("points"));
                        userActivityPointsTextView.setText(String.valueOf(currentPoints));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).execute(method, indexNo);

        Button addPointsButton = (Button) inflatedView.findViewById(R.id.addActivityPointsButton);
        addPointsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), AddPointsActivity.class);
                startActivityForResult(intent, REFRESH_VIEWPAGER_REQUEST);
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.fade_out);
            }
        });
        return inflatedView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REFRESH_VIEWPAGER_REQUEST)
        {
            if(resultCode == Activity.RESULT_OK){
                //Refresh ViewPager
                ViewPager vp = (ViewPager) getActivity().findViewById(R.id.viewPager);
                vp.getAdapter().notifyDataSetChanged();

                //Update headerView with new value of activity points
                 View navHeader = ((UserPanelActivity)getActivity()).getHeaderView();
                 TextView tv = (TextView)(navHeader.findViewById(R.id.header_calculus_points));
                 tv.setText(String.valueOf(currentPoints + data.getIntExtra("points", 0)));
            }
        }
    }



}
