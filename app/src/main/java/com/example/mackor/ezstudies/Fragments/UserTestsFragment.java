package com.example.mackor.ezstudies.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.mackor.ezstudies.AddTestFragment;
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

    public UserTestsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflatedView = inflater.inflate(R.layout.fragment_user_tests, container, false);

        myProgressBar = (ProgressBar)inflatedView.findViewById(R.id.myProgressBar);

        String method = "GET_USER_TESTS";
        String indexNo = new UserSessionManager(getContext(), getActivity()).getIndexNo();
        Networking networking = (Networking) new Networking(myProgressBar, getContext(), new Networking.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                try {
                    JSONArray jsonArr = new JSONArray(output);
                    CustomJSONAdapter myAdapter = new CustomJSONAdapter(jsonArr, getContext());
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
                AddTestFragment addTestFragment = new AddTestFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_container, addTestFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                //Nie ogarniam kurwa ale ta linijka pomogla w problemie, gdy wracalem z fragmentu to napotkiwałem blank activity
                //Jakby po zmienieniu activity zrobilem refresh starego fregmentu i on tam został. Dizeła więc chuj xd
                ViewPager vp = (ViewPager) getActivity().findViewById(R.id.viewPager);
                vp.getAdapter().notifyDataSetChanged();

            }
        });


        return inflatedView;
    }

}
