package com.example.mackor.ezstudies;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Bogus on 2016-02-11.
 */
public class Networking extends AsyncTask<Object, Void, String>{
    private Context context;

    @Override
    protected void onPreExecute() {
        Toast.makeText(context, "Submitting...", Toast.LENGTH_LONG).show();
    }

    public Networking(Context context) {
        this.context = context;
    }

    @Override
    protected void onPostExecute(String result) {
        String message = "";
        try {
            JSONObject json = new JSONObject(result);
            message = json.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected String doInBackground(Object... params) {
        String findIndexURL = "http://46.101.168.84/EzStudiesCRUD/find_index.php";
        String registerURL = "http://46.101.168.84/EzStudiesCRUD/register.php";
        String method = (String)params[0];
        NewStudent newStudent = (NewStudent)params[1];
        boolean indexFound = findIndex(newStudent.getIndexNo(), findIndexURL);

        if(!indexFound)
        {
            JSONObject json = new JSONObject();
            try {
                json.put("message", "You do not belong to our group :(");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return json.toString();
        }

        if(method.equals("REGISTER") && indexFound)
        {
            try {
                URL url = new URL(registerURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data =   URLEncoder.encode("fname", "UTF-8") + "=" + URLEncoder.encode(newStudent.getFname(), "UTF-8") + "&" +
                                URLEncoder.encode("lname", "UTF-8") + "=" + URLEncoder.encode(newStudent.getLname(), "UTF-8") + "&" +
                                URLEncoder.encode("index", "UTF-8") + "=" + URLEncoder.encode(newStudent.getIndexNo(), "UTF-8") + "&" +
                                URLEncoder.encode("group", "UTF-8") + "=" + URLEncoder.encode(newStudent.getGroup(), "UTF-8") + "&" +
                                URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(newStudent.getPassword(), "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String response = "";
                String line = "";
                while((line = bufferedReader.readLine()) != null)
                {
                    response += line;
                }
                inputStream.close();
                bufferedReader.close();
                httpURLConnection.disconnect();
                return response;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    private boolean findIndex(String indexNo, String findIndexUrl)
    {
        try {
            URL url = new URL(findIndexUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String data = URLEncoder.encode("indexNo", "UTF-8") + "=" + URLEncoder.encode(indexNo, "UTF-8");
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String response = "";
            String line = "";
            while((line = bufferedReader.readLine()) != null)
            {
                response += line;
            }
            inputStream.close();
            bufferedReader.close();
            httpURLConnection.disconnect();

            JSONObject json = new JSONObject(response);
            return json.getString("success").equals("1");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void _(String message)
    {
        Log.v("EzStudies", message);
    }
}
