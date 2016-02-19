package com.example.mackor.ezstudies.BackEndTools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.example.mackor.ezstudies.Models.LogStudent;
import com.example.mackor.ezstudies.Models.NewStudent;

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
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Bogus on 2016-02-11.
 */
public class Networking extends AsyncTask<Object, Void, String> {

    private Context context;

    public interface AsyncResponse {
        void processFinish(String output);
    }
    public AsyncResponse delegate = null;

    public Networking(Context context, AsyncResponse delegate){
        this.delegate = delegate;
        this.context = context;
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }

    @Override
    protected String doInBackground(Object... params) {
        String registerURL = "http://46.101.168.84/EzStudiesCRUD/register.php";
        String loginURL = "http://46.101.168.84/EzStudiesCRUD/login.php";
        String getResultsURL = "http://46.101.168.84/EzStudiesCRUD/get_results.php";
        String method = (String) params[0];

        if (!isConnectedToInternet(context))
            return "{\"success\":\"false\",\"message\":\"Connection problem\"}";

        switch (method) {
            case "REGISTER":
                NewStudent newStudent = (NewStudent) params[1];
                String regData = URLdataEncoder(newStudent, "fname", "lname", "indexNo", "group", "password");
                return makeHttpPOSTRequest(registerURL, regData);
            case "LOGIN":
                LogStudent logStudent = (LogStudent) params[1];
                String logData = URLdataEncoder(logStudent, "indexNo", "password");
                String x = makeHttpPOSTRequest(loginURL, logData);
                return x;
            case "GETRESULTS":
                return makeHttpPOSTRequest(getResultsURL, null);
            default:
                return null;
        }
    }


    protected String makeHttpPOSTRequest(String registerURL, String URLDataEncoded) {
        try {
            URL url = new URL(registerURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            if(URLDataEncoded != null)
            {
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                bufferedWriter.write(URLDataEncoded);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
            }
            InputStream inputStream;
            inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String response = "";
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                response += line;
            }
            inputStream.close();
            bufferedReader.close();
            httpURLConnection.disconnect();
            return response;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            _(e.getMessage());
        } catch (ProtocolException e) {
            e.printStackTrace();
            _(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            _(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            _(e.getMessage());
        }
        return null;
    }

    private String URLdataEncoder(Object object, String... params) {
        String URLdataEncoded = "";
        Field fields[] = object.getClass().getDeclaredFields();
        for (int i = 0; i < params.length; i++) {
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.getName().equals(params[i])) {
                    try {
                        String value = (String) field.get(object);
                        URLdataEncoded += URLEncoder.encode(params[i], "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8");
                        if (i < params.length - 1) URLdataEncoded += "&";
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

    public boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    private void _(String message) {
        Log.v("ERRORS", message);
    }
}