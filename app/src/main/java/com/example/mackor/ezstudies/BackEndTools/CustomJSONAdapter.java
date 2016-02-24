package com.example.mackor.ezstudies.BackEndTools;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mackor.ezstudies.FrontEndTools.FontManager;
import com.example.mackor.ezstudies.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Korzonkie on 2016-02-18.
 */
public class CustomJSONAdapter extends BaseAdapter {

    private JSONArray jsonArr;
    private Context context;

    public CustomJSONAdapter(JSONArray jsonArr, Context context) {
        this.jsonArr = jsonArr;
        this.context = context;
    }

    @Override
    public int getCount() {
        if(jsonArr == null) return 0;
        else return jsonArr.length();
    }

    @Override
    public Object getItem(int position) {
        if(jsonArr == null) return null;
        else try {
            return jsonArr.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        try {
            JSONObject json = jsonArr.getJSONObject(position);
            return json.getLong("ID");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.custom_user_test_result, null);
        }

        TextView testTypeTV = (TextView)convertView.findViewById(R.id.test_type);
        TextView testScoreTV = (TextView)convertView.findViewById(R.id.test_score);

        JSONObject json = (JSONObject) getItem(position);

        String testType = "";
        int testScore = 0;
        try {
            testType = json.getString("testType");
            testScore = json.getInt("result");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        testTypeTV.setText(testType);
        testScoreTV.setText(String.valueOf(testScore));

        return convertView;
    }
}
