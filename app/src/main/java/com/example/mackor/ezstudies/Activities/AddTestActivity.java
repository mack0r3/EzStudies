package com.example.mackor.ezstudies.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mackor.ezstudies.BackEndTools.Networking;
import com.example.mackor.ezstudies.BackEndTools.UserSessionManager;
import com.example.mackor.ezstudies.FrontEndTools.FontManager;
import com.example.mackor.ezstudies.R;

public class AddTestActivity extends AppCompatActivity {

    ProgressBar progressBar;
    String indexNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_test);

        //Font Awesome
        TextView testTypeIcon = (TextView)findViewById(R.id.testTypeIcon);
        TextView testPointsIcon = (TextView)findViewById(R.id.testPointsIcon);

        testTypeIcon.setTypeface(FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME));
        testPointsIcon.setTypeface(FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME));

        final EditText testType = (EditText)findViewById(R.id.testTypeET);
        final EditText testPoints = (EditText)findViewById(R.id.testPointsET);

        progressBar = (ProgressBar)findViewById(R.id.myProgressBar);

        indexNo = new UserSessionManager(getApplicationContext(), AddTestActivity.this).getIndexNo();

        Button addTestButton = (Button)findViewById(R.id.addTestButton);
        addTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String testTypeString = testType.getText().toString();
                final String testPointsString = testPoints.getText().toString();

                if(testTypeString.equals("") || testPointsString.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Fill all fields", Toast.LENGTH_SHORT).show();
                } else
                {
                    String method = "INSERT_TEST";
                    Networking networking = (Networking) new Networking(progressBar, getApplicationContext(), new Networking.AsyncResponse() {
                        @Override
                        public void processFinish(String output) {
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("result", testPointsString);
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
                            overridePendingTransition(R.anim.fade_in, R.anim.slide_down);
                        }
                    }).execute(method, indexNo, testTypeString, testPointsString);
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_down);
    }
}
