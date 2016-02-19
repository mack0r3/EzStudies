package com.example.mackor.ezstudies.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mackor.ezstudies.BackEndTools.UserSessionManager;
import com.example.mackor.ezstudies.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserSessionManager userSessionManager = new UserSessionManager(getApplicationContext(), MainActivity.this);
        if(userSessionManager.isUserLoggedIn())
        {
            Intent intent = new Intent(getApplicationContext(), UserPanelActivity.class);
            startActivity(intent);
            finish();
        }

        Button registerButton = (Button) findViewById(R.id.buttonRegister);
        Button loginButton = (Button)findViewById(R.id.buttonLogin);

        registerButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(intent);
            }
        });
    }
}
