package com.example.mackor.ezstudies.Fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mackor.ezstudies.Activities.AddTestActivity;
import com.example.mackor.ezstudies.Activities.UserPanelActivity;
import com.example.mackor.ezstudies.BackEndTools.CustomJSONAdapter;
import com.example.mackor.ezstudies.BackEndTools.Networking;
import com.example.mackor.ezstudies.BackEndTools.UserSessionManager;
import com.example.mackor.ezstudies.R;

import org.json.JSONArray;
import org.json.JSONException;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserTestsFragment extends Fragment {

    View inflatedView;
    ProgressBar myProgressBar;

    int REFRESH_VIEWPAGER_REQUEST = 1;

    public UserTestsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflatedView = inflater.inflate(R.layout.fragment_user_tests, container, false);

        myProgressBar = (ProgressBar)inflatedView.findViewById(R.id.myProgressBar);

        String method = "GET_USER_TESTS";
        String indexNo = new UserSessionManager(getContext()).getIndexNo();
        Networking networking = (Networking) new Networking(myProgressBar, getContext(), new Networking.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                try {
                    JSONArray jsonArr = new JSONArray(output);
                    CustomJSONAdapter myAdapter = new CustomJSONAdapter("276946", jsonArr, getContext());
                    ListView listView = (ListView)inflatedView.findViewById(R.id.testsListView);
                    listView.setAdapter(myAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).execute(method, indexNo);

        Button addTestButton = (Button)inflatedView.findViewById(R.id.addTestButton);
        addTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                Intent intent = new Intent(getContext(), AddTestActivity.class);
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

                //TODO Update headerView with new value of test points
                View navHeader = ((UserPanelActivity)getActivity()).getHeaderView();
                TextView tv = (TextView)(navHeader.findViewById(R.id.header_calculus_points));
                String formerPoints = tv.getText().toString();
                String newPoints = String.valueOf(Integer.parseInt(formerPoints) + Integer.parseInt(data.getStringExtra("result")));
                tv.setText(newPoints);
            }
        }
    }

}
