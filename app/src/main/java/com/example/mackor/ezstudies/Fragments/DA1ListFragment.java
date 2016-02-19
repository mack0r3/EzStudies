package com.example.mackor.ezstudies.Fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mackor.ezstudies.BackEndTools.CustomJSONAdapter;
import com.example.mackor.ezstudies.BackEndTools.Networking;
import com.example.mackor.ezstudies.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Bogus on 2016-02-19.
 */
public class DA1ListFragment extends ListFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.list_fragment_da1, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Networking networking = (Networking) new Networking(getActivity(), new Networking.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                try {
                    JSONArray jsonArr = SortResults(new JSONArray(output), "DA1");
                    CustomJSONAdapter myAdapter = new CustomJSONAdapter(jsonArr, getActivity());
                    setListAdapter(myAdapter);
                } catch (JSONException e) {
                    Log.v("ERROR", e.getMessage());
                    e.printStackTrace();
                }
            }
        }).execute("GETRESULTS", null);

    }
    public JSONArray SortResults(JSONArray jsonArr, String group)
    {
        JSONArray newJSONArr = new JSONArray();
        for(int i = 0 ; i < jsonArr.length(); i++)
        {
            try {
                JSONObject json = jsonArr.getJSONObject(i);
                Log.v("ERRORS", json.toString());
                if(json.getString("group").equals(group))
                {
                    newJSONArr.put(json);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return newJSONArr;
    }
}
