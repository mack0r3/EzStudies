package com.example.mackor.ezstudies.BackEndTools;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.mackor.ezstudies.Models.LogStudent;
import com.example.mackor.ezstudies.Models.NewStudent;
import com.example.mackor.ezstudies.R;

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
    private ProgressBar progressBar;

    public interface AsyncResponse {
        void processFinish(String output);
    }

    public AsyncResponse delegate = null;

    public Networking(ProgressBar progressbar, Context context, AsyncResponse delegate) {
        this.progressBar = progressbar;
        this.delegate = delegate;
        this.context = context;
    }

    @Override
    protected void onPostExecute(String result) {
        if(progressBar != null) progressBar.setVisibility(View.GONE);
        delegate.processFinish(result);
    }

    @Override
    protected void onPreExecute() {
        if(progressBar != null) progressBar.setVisibility(View.VISIBLE);
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Object... params) {
        String registerURL = "http://ezstudies.knightparty.pl/register.php";
        String loginURL = "http://ezstudies.knightparty.pl/login.php";
        String getResultsURL = "http://ezstudies.knightparty.pl/get_results.php";
        String getUserInfoURL = "http://ezstudies.knightparty.pl/get_user_info.php";
        String updateUserPointsURL = "http://ezstudies.knightparty.pl/update_user_points.php";
        String getUserTestsURL = "http://ezstudies.knightparty.pl/get_user_tests.php";
        String insertTestURL = "http://ezstudies.knightparty.pl/insert_test.php";
        String getUsersPointsURL = "http://ezstudies.knightparty.pl/get_users_points.php";
        String getAveragePointsURL = "http://ezstudies.knightparty.pl/get_average_points.php";
        String method = (String) params[0];

        if (!isConnectedToInternet(context)) {
            return "{\"success\":\"false\",\"message\":\"Connection problem\"}";
        }


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
            case "GET_USER_INFO":
                String get_user_info_data = "";
                try {
                    get_user_info_data = URLEncoder.encode("indexNo", "UTF-8") + "=" + URLEncoder.encode(params[1].toString(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return makeHttpPOSTRequest(getUserInfoURL, get_user_info_data);
            case "UPDATE_POINTS":
                String update_points_data = "";
                try {
                    update_points_data = URLEncoder.encode("indexNo", "UTF-8") + "=" + URLEncoder.encode(params[1].toString(), "UTF-8") + "&";
                    update_points_data += URLEncoder.encode("points", "UTF-8") + "=" + URLEncoder.encode(params[2].toString(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return makeHttpPOSTRequest(updateUserPointsURL, update_points_data);
            case "GET_USER_TESTS":
                String get_tests_data = "";
                try {
                    get_tests_data = URLEncoder.encode("indexNo", "UTF-8") + "=" + URLEncoder.encode(params[1].toString(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return makeHttpPOSTRequest(getUserTestsURL, get_tests_data);
            case "INSERT_TEST":
                String insert_test_data = "";
                try {
                    insert_test_data = URLEncoder.encode("indexNo", "UTF-8") + "=" + URLEncoder.encode(params[1].toString(), "UTF-8") + "&";
                    insert_test_data += URLEncoder.encode("testType", "UTF-8") + "=" + URLEncoder.encode(params[2].toString(), "UTF-8") + "&";
                    insert_test_data += URLEncoder.encode("result", "UTF-8") + "=" + URLEncoder.encode(params[3].toString(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return makeHttpPOSTRequest(insertTestURL, insert_test_data);
            case "GET_USERS_POINTS":
                String get_users_points = "";
                try {
                    get_users_points = URLEncoder.encode("group", "UTF-8") + "=" + URLEncoder.encode(params[1].toString(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return makeHttpPOSTRequest(getUsersPointsURL, get_users_points);
            case "GET_AVERAGE_POINTS":
                String get_average_points = "";
                try {
                    get_average_points = URLEncoder.encode("group", "UTF-8") + "=" + URLEncoder.encode(params[1].toString(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return makeHttpPOSTRequest(getAveragePointsURL, get_average_points);
            default:
                return null;
        }
    }


    protected String makeHttpPOSTRequest(String registerURL, String URLDataEncoded) {
        try {
            URL url = new URL(registerURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            if (URLDataEncoded != null) {
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
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }


    private void _(String message) {
        Log.v("ERRORS", message);
    }
}