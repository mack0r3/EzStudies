package com.example.mackor.ezstudies.BackEndTools;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.mackor.ezstudies.FrontEndTools.MyToast;import com.example.mackor.ezstudies.NewStudent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Bogus on 2016-02-11.
 */
public class Networking extends AsyncTask<Object, Void, String>{
    private Context context;
    private Activity activity;
    public Networking(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    @Override
    protected void onPostExecute(String result) {
        String message = "";
        boolean success = false;
        try {
            JSONObject json = new JSONObject(result);
            message = json.getString("message");
            success = json.getString("success").equals("true");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
        if(success)activity.finish();
    }

    @Override
    protected String doInBackground(Object... params) {
        String findIndexURL = "http://46.101.168.84/EzStudiesCRUD/find_index.php";
        String registerURL = "http://46.101.168.84/EzStudiesCRUD/register.php";
        String method = (String) params[0];
        NewStudent newStudent = (NewStudent) params[1];

        if(!isConnectedToInternet(context)) return "{\"success\":\"false\",\"message\":\"Connection problem\"}";

        //Firstly, we check, if user-to-be is in our Group DA1, or DA2;
        //If he is : continues
        //Otherwise return error.
        String findIndexResponse = findIndex(newStudent, findIndexURL);
        try {
            JSONObject indexJSON = new JSONObject(findIndexResponse);
            String indexSuccess = indexJSON.getString("success");
            String indexMessage = indexJSON.getString("message");
            if(indexSuccess.equals("false")) return indexJSON.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (method.equals("REGISTER")) {
            String data = URLdataEncoder(newStudent, "fname", "lname", "indexNo", "group", "password");
            try {
                URL url = new URL(registerURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                sendURLData(httpURLConnection, data);
                httpURLConnection.disconnect();
                return (getJSON(httpURLConnection));
            } catch (MalformedURLException e) {
                e.printStackTrace();
                _(e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                _(e.getMessage());
            }

        }

        return null;
    }

    private String URLdataEncoder(NewStudent newStudent, String... params)
    {
        String URLdataEncoded = "";
        Field fields[] = newStudent.getClass().getDeclaredFields();
        for(int i = 0; i < params.length; i++)
        {
            for(Field field : fields)
            {
                field.setAccessible(true);
                if(field.getName().equals(params[i]))
                {
                    try {
                        String value = (String)field.get(newStudent);
                        URLdataEncoded += URLEncoder.encode(params[i], "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8");
                        if(i < params.length - 1)URLdataEncoded += "&";
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        _(e.getMessage());
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        _(e.getMessage());
                    }
                }
            }
        }
        return URLdataEncoded;
    }

    private void sendURLData(HttpURLConnection httpURLConnection, String URLencodedData)
    {
        try {
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            bufferedWriter.write(URLencodedData);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
            _("sendURLData connection error: " + e.getMessage());
        }
    }

    private String getJSON(HttpURLConnection httpURLConnection)
    {
        InputStream inputStream = null;
        try {
            inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String response = "";
            String line = "";
            while((line = bufferedReader.readLine()) != null)
            {
                response += line;
            }
            inputStream.close();
            bufferedReader.close();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            _("getJSON connection error: " + e.getMessage());
        }
        return null;
    }

    private String findIndex(NewStudent newStudent, String findIndexUrl)
    {
        try {
            URL url = new URL(findIndexUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            String data = URLdataEncoder(newStudent, "indexNo");
            sendURLData(httpURLConnection, data);
            String response = getJSON(httpURLConnection);
            httpURLConnection.disconnect();
            return response;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            _("MalformedURLException: " + e.getMessage());
            return "{\"success\":\"false\",\"message\":\"Contact Maciek ASAP!\"}";
        } catch (IOException e) {
            e.printStackTrace();
            _("IOException: " + e.getMessage());
            return "{\"success\":\"false\",\"message\":\"Connection problem\"}";
        }
    }

    public boolean isConnectedToInternet(Context context){
        ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }

    private void _(String message)
    {
        Log.v("ERRORS", message);
    }
}