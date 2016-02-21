package com.example.mackor.ezstudies.BackEndTools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.mackor.ezstudies.Activities.SignInActivity;

/**
 * Created by Korzonkie on 2016-02-17.
 */
public class UserSessionManager {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;
    Activity activity;

    //Shared preference mode
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LoginPreferences";
    private static final String IS_USR_LOGGED = "IsUserLoggedIn";
    public static final String KEY_INDEX = "index";

    public UserSessionManager(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public String getIndexNo()
    {
        return sharedPreferences.getString(KEY_INDEX, null);
    }

    public void createUserLogginSession(String indexNo) {
        editor.putBoolean(IS_USR_LOGGED, true);
        editor.putString(KEY_INDEX, indexNo);
        editor.commit();
    }

    //If user isn't logged in, he will be redirted to login page
    public boolean checkLogin() {
        if (!this.isUserLoggedIn()) {
            Intent intent = new Intent(context, SignInActivity.class);
            // Closing all the Activities from stack
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent);

            return true;
        }
        return false;
    }

    public boolean isUserLoggedIn() {
        return sharedPreferences.getBoolean(IS_USR_LOGGED, false);
    }

    public void logoutUser() {

        // Clearing all user data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Login Activity
        Intent intent = new Intent(context, SignInActivity.class);

        // Closing all the Activities
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        activity.finish();


        // Staring Login Activity
        context.startActivity(intent);
    }
}
