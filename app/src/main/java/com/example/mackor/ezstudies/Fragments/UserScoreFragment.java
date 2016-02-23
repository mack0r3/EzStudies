package com.example.mackor.ezstudies.Fragments;


import android.content.DialogInterface;
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

        final TextView userNameTextView = (TextView) inflatedView.findViewById(R.id.userNameTextView);
        final TextView userActivityPointsTextView = (TextView) inflatedView.findViewById(R.id.userActivityPointsTextView);
        final ProgressBar progressBar = (ProgressBar) inflatedView.findViewById(R.id.myProgressBar);
        Switch publicResultSwitch = (Switch)inflatedView.findViewById(R.id.publicResultSwitch);

        Button saveActivityPointsButton = (Button)inflatedView.findViewById(R.id.saveActivityButton);

        //FontAwesome
        TextView addPointButtonIcon = (TextView)inflatedView.findViewById(R.id.addPointButtonIcon);
        TextView subtractPointButtonIcon = (TextView)inflatedView.findViewById(R.id.subtractPointButtonIcon);
        addPointButtonIcon.setTypeface(FontManager.getTypeface(getContext(), FontManager.FONTAWESOME));
        subtractPointButtonIcon.setTypeface(FontManager.getTypeface(getContext(), FontManager.FONTAWESOME));

        publicResultSwitch.setTextOff("OFF");
        publicResultSwitch.setTextOn("ON");


        //Retrieve logged user info
        String method = "GET_USER_INFO";
        indexNo = new UserSessionManager(getContext(), getActivity()).getIndexNo();

        Networking getUserInfo = (Networking) new Networking(progressBar, getContext(), new Networking.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                try {
                    JSONObject json = new JSONObject(output);

                    inflatedView.findViewById(R.id.userScoreFragmentContainer).setVisibility(View.VISIBLE);

                    String firstName = json.getString("fname").substring(0, 1).toUpperCase() + json.getString("fname").substring(1);
                    String lastName = json.getString("lname").substring(0, 1).toUpperCase() + json.getString("lname").substring(1);
                    currentPoints = Integer.parseInt(json.getString("points"));

                    userNameTextView.setText(firstName + " " + lastName);
                    userActivityPointsTextView.setText(String.valueOf(currentPoints));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).execute(method, indexNo);


        addPointButtonIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pointsCounter++;
                if(pointsCounter > 0)  userActivityPointsTextView.setText(currentPoints + " + " + pointsCounter);
                else if(pointsCounter == 0) userActivityPointsTextView.setText(String.valueOf(currentPoints));
                else userActivityPointsTextView.setText(String.valueOf(currentPoints + " - " + pointsCounter * (-1)));
            }
        });
        subtractPointButtonIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pointsCounter--;
                if(pointsCounter > 0)  userActivityPointsTextView.setText(currentPoints + " + " + pointsCounter);
                else if(pointsCounter == 0) userActivityPointsTextView.setText(String.valueOf(currentPoints));
                else userActivityPointsTextView.setText(String.valueOf(currentPoints + " - " + pointsCounter * (-1)));
            }
        });

        final String _method = "UPDATE_POINTS";

        saveActivityPointsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String method = "";
                String plural = "";
                String message;
                if(pointsCounter > 0)method = "add ";
                if(pointsCounter < 0)method = "subtract ";
                if(pointsCounter > 1 || pointsCounter < -1) plural = "s";
                if(pointsCounter == 0) message = "Do you want to save changes?";
                else message = "Do you want to " + method + Math.abs(pointsCounter) + " point" + plural + "?";

                new AlertDialog.Builder(getContext())
                        .setMessage(message)
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Networking addPoint = (Networking) new Networking(progressBar, getContext(), new Networking.AsyncResponse() {
                                    @Override
                                    public void processFinish(String output) {
                                        //Na latwizne polecialem w chuj ale jebac, nie dam rady juz
                                        int actualPoints = currentPoints + pointsCounter;
                                        View navHeader = ((UserPanelActivity)getActivity()).getHeaderView();
                                        TextView tv = (TextView)(navHeader.findViewById(R.id.header_calculus_points));
                                        tv.setText(String.valueOf(actualPoints));

                                        ViewPager vp = (ViewPager) getActivity().findViewById(R.id.viewPager);
                                        vp.getAdapter().notifyDataSetChanged();

                                        pointsCounter = 0;
                                    }
                                }).execute(_method, indexNo, pointsCounter);
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
        return inflatedView;
    }


}
