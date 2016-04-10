package com.example.mackor.ezstudies.BackEndTools;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mackor.ezstudies.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Korzonkie on 03.04.2016.
 */
public class AlarmReceiver extends BroadcastReceiver {

    Context context;
    int averagePoints;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;
        setAveragePoints();
        createNotificationWhenConnectionEstablished();
    }
    private void createNotificationWhenConnectionEstablished(){
        String method = "GET_USER_INFO";
        String indexNo = retrieveIndexNo();

        new Networking(null, context, new Networking.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                int points = retrievePointsFromJson(output);
                String msg = "";
                if(points > averagePoints) msg = "Solve a problem for tomorrow! You are under the average.";
                else if(points < averagePoints) msg = "Let others solve some problems! You are above the average.";
                createNotification("EzStudies", "Hey there!", output, msg);
            }
        }).execute(method, indexNo);
    }

    public void createNotification(String title, String ticker, String jsonString, String message){

        String content = makeNotificationContentMessageFromJson(jsonString);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle().bigText(message);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setTicker(ticker)
                .setContentText(message)
                .setSmallIcon(R.drawable.applogov3)
                //.setLargeIcon(R.drawable.applogov3)
                .setStyle(bigTextStyle);

        mBuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }

    private String retrieveIndexNo(){
        UserSessionManager userSessionManager = new UserSessionManager(context);
        return userSessionManager.getIndexNo();
    }

    private int retrievePointsFromJson(String jsonString){
        JSONObject json = null;
        int points = 0;
        try {
            json = new JSONObject(jsonString);
            points = Integer.parseInt(json.getString("points"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return points;
    }

    private String retrieveNameFromJson(String jsonString){
        JSONObject json = null;
        String name = "";
        try {
            json = new JSONObject(jsonString);
            name = json.getString("fname");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }

    private String makeNotificationContentMessageFromJson(String jsonString) {
        String name = retrieveNameFromJson(jsonString);
        name = capitalizeFirstLetter(name);

        return name + "!";
    }

    private String capitalizeFirstLetter(String name){
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    private void setAveragePoints(){
        String method = "GET_AVERAGE_POINTS";

        new Networking(null, context, new Networking.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                averagePoints = retrieveAveragePointsFromJson(output);
            }
        }).execute(method, "DA1");

    }

    private int retrieveAveragePointsFromJson(String jsonString){
        JSONObject json = null;
        int averagePoints = 0;
        try {
            json = new JSONObject(jsonString);
            averagePoints = Math.round(Float.parseFloat(json.getString("averagePoints")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return averagePoints;
    }

}
