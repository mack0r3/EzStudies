package com.example.mackor.ezstudies.Fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mackor.ezstudies.Activities.UserPanelActivity;
import com.example.mackor.ezstudies.BackEndTools.CustomJSONAdapter;
import com.example.mackor.ezstudies.BackEndTools.Networking;
import com.example.mackor.ezstudies.FrontEndTools.Utility;
import com.example.mackor.ezstudies.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Bogus on 2016-02-19.
 */
public class DA1ListFragment extends ListFragment {

    ProgressBar progressBar;
    View inflatedView;

    String indexNo;
    int points;


    public DA1ListFragment newIntance(String indexNo, int points) {
        DA1ListFragment fragment = new DA1ListFragment();
        Bundle args = new Bundle();
        args.putString("indexNo", indexNo);
        args.putInt("points", points);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflatedView = inflater.inflate(R.layout.list_fragment_da1, container, false);

        indexNo = getArguments().getString("indexNo");
        points = getArguments().getInt("points");

        TextView myIndexNo = (TextView)inflatedView.findViewById(R.id.indexNoTextView);
        myIndexNo.setText(indexNo);

        progressBar = (ProgressBar)inflatedView.findViewById(R.id.myProgressBar);
        return inflatedView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Networking networking = (Networking) new Networking(progressBar, getContext(), new Networking.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                try {
                    JSONArray jsonArr = SortResults(new JSONArray(output), "DA1");
                    CustomJSONAdapter myAdapter = new CustomJSONAdapter(jsonArr, getActivity());

                    ListView lv = (ListView)inflatedView.findViewById(android.R.id.list);
                    setListAdapter(myAdapter);
                    Utility.setListViewHeightBasedOnChildren(lv);
                } catch (JSONException e) {
                    Log.v("ERROR", e.getMessage());
                    e.printStackTrace();
                }
            }
        }).execute("GETRESULTS", null);

    }

    private JSONArray SortResults(JSONArray jsonArr, String group) {
        JSONArray newJSONArr = new JSONArray();
        for (int i = 0; i < jsonArr.length(); i++) {
            try {
                JSONObject json = jsonArr.getJSONObject(i);
                if (json.getString("group").equals(group)) {
                    newJSONArr.put(json);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return newJSONArr;
    }
}
