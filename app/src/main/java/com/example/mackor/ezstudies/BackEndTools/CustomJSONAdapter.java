package com.example.mackor.ezstudies.BackEndTools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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
            convertView = layoutInflater.inflate(R.layout.calculus_result_row, null);
        }

        TextView indexNoTextView = (TextView)convertView.findViewById(R.id.indexNoTextView);
        TextView pointsTextView = (TextView)convertView.findViewById(R.id.pointsTextView);

        JSONObject json = (JSONObject) getItem(position);

        String indexNo = null;
        int points = 0;

        try {
            indexNo = json.getString("indexNo");
            points = json.getInt("points");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        indexNo += ":";

        indexNoTextView.setText(indexNo);
        pointsTextView.setText(String.valueOf(points));

        return convertView;
    }
}
