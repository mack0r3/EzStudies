package com.example.mackor.ezstudies;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mackor.ezstudies.BackEndTools.CustomJSONAdapter;
import com.example.mackor.ezstudies.BackEndTools.Networking;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DA1Fragment extends Fragment {


    public DA1Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View inflatedView = inflater.inflate(R.layout.fragment_da1, container, false);

        Networking networking = (Networking) new Networking(getContext(), new Networking.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                try {
                    //sprawdzić czy to działa (pijany byłem)
                    JSONArray jsonArr = new JSONArray(output);
                    ListView results_da1 = (ListView)inflatedView.findViewById(R.id.results_da1);
                    ListAdapter myAdapter = new CustomJSONAdapter(jsonArr, getContext());
                    results_da1.setAdapter(myAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).execute("GETRESULTS", null);


        return inflatedView;
    }

}
