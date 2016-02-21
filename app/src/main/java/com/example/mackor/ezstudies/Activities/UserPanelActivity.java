package com.example.mackor.ezstudies.Activities;

import android.content.SharedPreferences;
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
import com.example.mackor.ezstudies.Fragments.DA1ListFragment;
import com.example.mackor.ezstudies.Fragments.MainFragment;
import com.example.mackor.ezstudies.FrontEndTools.FontManager;
import com.example.mackor.ezstudies.R;

public class UserPanelActivity extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    FragmentTransaction fragmentTransaction;
    NavigationView navigationView;

    int lastClikedItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_panel);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_container, new MainFragment());
        fragmentTransaction.commit();
        getSupportActionBar().setTitle("MainContainer");
        navigationView = (NavigationView)findViewById(R.id.navigation_view);
        navigationView.setItemTextColor(ColorStateList.valueOf(Color.WHITE));
        navigationView.setItemIconTintList(ColorStateList.valueOf(Color.WHITE));
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch(item.getItemId())
                {
                    case R.id.calculus_id:
                        if(lastClikedItem != R.id.calculus_id)
                        {
                            fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.main_container, new CalculusFragment());
                            fragmentTransaction.commit();
                            getSupportActionBar().setTitle("Calculus");
                        }
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        lastClikedItem = R.id.calculus_id;
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


        //Change header view programmatically
        View headerView = navigationView.inflateHeaderView(R.layout.navigation_drawer_header);
        TextView headerNameIcon = (TextView)headerView.findViewById(R.id.header_name_icon);
        TextView headerGroupIcon = (TextView)headerView.findViewById(R.id.header_group_icon);
        TextView headerCalculusPointsIcon = (TextView)headerView.findViewById(R.id.header_calculus_points_icon);
        TextView headerName = (TextView)headerView.findViewById(R.id.header_name);
        TextView headerGroup = (TextView)headerView.findViewById(R.id.header_group);
        TextView headerCalculusPoints = (TextView)headerView.findViewById(R.id.header_calculus_points);

        //Font Awesome
        headerNameIcon.setTypeface(FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME));
        headerGroupIcon.setTypeface(FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME));
        headerCalculusPointsIcon.setTypeface(FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME));

        ProgressBar progressBar = (ProgressBar)headerView.findViewById(R.id.myProgressBar);
        String method = "GETINFO";
        String indexNo = new UserSessionManager(getApplicationContext(), UserPanelActivity.this).getIndexNo();
        Networking networking = (Networking)new Networking(progressBar, getApplicationContext(), new Networking.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                Log.v("ERRORS", output);
            }
        }).execute(method, indexNo);


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }
}
