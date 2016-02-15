package com.example.mackor.ezstudies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(isLoggedIn())Log.v("ERROR", "Session active");
        else Log.v("ERROR", "Session inactive");

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
    private boolean isLoggedIn(){
        SharedPreferences sharedPreferences = this.getSharedPreferences("MyPrefs", this.MODE_PRIVATE);
        String s = sharedPreferences.getString("LOGIN_SESSION", "OFF");
        return (s.equals("ON"));
    }
}
