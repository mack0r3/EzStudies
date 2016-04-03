package com.example.mackor.ezstudies.BackEndTools;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
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
    private String currentIndexNo;

    public CustomJSONAdapter(String currentIndexNo, JSONArray jsonArr, Context context) {
        this.jsonArr = jsonArr;
        this.context = context;
        this.currentIndexNo = currentIndexNo;
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
        convertView.setBackgroundColor(Color.parseColor("#101010"));
        TextView testTypeTV = (TextView)convertView.findViewById(R.id.test_type);
        TextView testScoreTV = (TextView)convertView.findViewById(R.id.test_score);

        JSONObject json = (JSONObject) getItem(position);

        String indexNo = "";
        int points = 0;
        try {
            indexNo = json.getString("indexNo");
            points = json.getInt("points");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        testTypeTV.setText(indexNo);
        testScoreTV.setText(String.valueOf(points));

        if(currentIndexNo.equals(indexNo))
        {
            convertView.setBackgroundColor(Color.parseColor("#27ae60"));
        }

        return convertView;
    }
}
