package com.example.mackor.ezstudies.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mackor.ezstudies.BackEndTools.AlarmReceiver;
import com.example.mackor.ezstudies.BackEndTools.Networking;
import com.example.mackor.ezstudies.BackEndTools.UserSessionManager;
import com.example.mackor.ezstudies.Fragments.CalculusFragment;
import com.example.mackor.ezstudies.Fragments.MainFragment;
import com.example.mackor.ezstudies.FrontEndTools.FontManager;
import com.example.mackor.ezstudies.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class UserPanelActivity extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    FragmentTransaction fragmentTransaction;
    NavigationView navigationView;
    View headerView;


    int lastItemClicked;

    //Global indexNo and points in order to pass them to DA1ListFragment
    String indexNo;
    int points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_panel);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);;
        drawerLayout.setWillNotDraw(false);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_container, new MainFragment());
        fragmentTransaction.commit();
        getSupportActionBar().setTitle("EzStudies");
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setItemTextColor(ColorStateList.valueOf(Color.WHITE));
        navigationView.setItemIconTintList(ColorStateList.valueOf(Color.WHITE));

        //Change header view programmatically
        headerView = navigationView.inflateHeaderView(R.layout.navigation_drawer_header);
        headerView.setWillNotDraw(false);

        final TextView headerNameIcon = (TextView) headerView.findViewById(R.id.header_name_icon);
        final TextView headerGroupIcon = (TextView) headerView.findViewById(R.id.header_group_icon);
        final TextView headerCalculusPointsIcon = (TextView) headerView.findViewById(R.id.header_calculus_points_icon);
        final TextView headerName = (TextView) headerView.findViewById(R.id.header_name);
        final TextView headerGroup = (TextView) headerView.findViewById(R.id.header_group);
        final TextView headerCalculusPoints = (TextView) headerView.findViewById(R.id.header_calculus_points);

        //Font Awesome
        headerNameIcon.setTypeface(FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME));
        headerGroupIcon.setTypeface(FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME));
        headerCalculusPointsIcon.setTypeface(FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME));

        ProgressBar progressBar = (ProgressBar) headerView.findViewById(R.id.myProgressBar);
        String method = "GET_USER_INFO";
        indexNo = new UserSessionManager(getApplicationContext(), UserPanelActivity.this).getIndexNo();
        Networking networking = (Networking) new Networking(progressBar, getApplicationContext(), new Networking.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                try {
                    JSONObject json = new JSONObject(output);
                    if(json.has("success"))
                    {
                        Toast toast = Toast.makeText(getApplicationContext(), json.getString("message"), Toast.LENGTH_LONG);
                        toast.show();
                    } else {
                        View view = findViewById(R.id.loggedUserInfo);
                        view.setVisibility(View.VISIBLE);
                        String group = json.getString("group");
                        points = Integer.parseInt(json.getString("points"));
                        headerName.setText(indexNo);
                        headerGroup.setText(group);
                        headerCalculusPoints.setText(String.valueOf(points));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).execute(method, indexNo);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.calculus_id:
                        CalculusFragment calculusFragment = new CalculusFragment();
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, calculusFragment);
                        if (lastItemClicked != item.getItemId())
                            fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Calculus");
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        lastItemClicked = item.getItemId();
                        break;
                    case R.id.logout_id:
                        UserSessionManager userSessionManager = new UserSessionManager(getApplicationContext(), UserPanelActivity.this);
                        userSessionManager.logoutUser();
                }
                return false;
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_search:
                startActivity(new Intent(this, UserPanelActivity.class));
                this.finish();
                this.overridePendingTransition(0, 0);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            if(count == 1){
                lastItemClicked = 0;
                getSupportActionBar().setTitle("EzStudies");
            }
            getSupportFragmentManager().popBackStack();
        }

    }

    public View getHeaderView()
    {
        return headerView;
    }
}
