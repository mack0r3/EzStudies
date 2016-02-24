package com.example.mackor.ezstudies.Activities;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mackor.ezstudies.BackEndTools.Networking;
import com.example.mackor.ezstudies.BackEndTools.UserSessionManager;
import com.example.mackor.ezstudies.Fragments.CalculusFragment;
import com.example.mackor.ezstudies.Fragments.MainFragment;
import com.example.mackor.ezstudies.FrontEndTools.FontManager;
import com.example.mackor.ezstudies.R;

import org.json.JSONException;
import org.json.JSONObject;

public class UserPanelActivity extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    FragmentTransaction fragmentTransaction;
    NavigationView navigationView;
    View headerView;

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
        getSupportActionBar().setTitle("MainContainer");
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
                    View view = findViewById(R.id.loggedUserInfo);
                    view.setVisibility(View.VISIBLE);

                    JSONObject json = new JSONObject(output);
                    String firstName = json.getString("fname").substring(0, 1).toUpperCase() + json.getString("fname").substring(1);
                    String lastName = json.getString("lname").substring(0, 1).toUpperCase() + json.getString("lname").substring(1);
                    String group = json.getString("group");
                    points = Integer.parseInt(json.getString("points"));
                    headerName.setText(firstName + " " + lastName);
                    headerGroup.setText(group);
                    headerCalculusPoints.setText(String.valueOf(points));
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
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Calculus");
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.logout_id:
                        UserSessionManager userSessionManager = new UserSessionManager(getApplicationContext(), UserPanelActivity.this);
                        userSessionManager.logoutUser();
                }
                return false;
            }
        });
        //navigationView.getMenu();
        //navigationView.inflateMenu(R.menu.drawer_menu);
    }

    public View getHeaderView()
    {
        return headerView;
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();
        Log.v("ERRORS", ""  + count);

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }

    }
}
