package com.example.mackor.ezstudies.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mackor.ezstudies.BackEndTools.Networking;
import com.example.mackor.ezstudies.BackEndTools.UserSessionManager;
import com.example.mackor.ezstudies.Fragments.CalculusFragment;
import com.example.mackor.ezstudies.R;

public class AddPointsActivity extends AppCompatActivity {

    String indexNo;
    int points;
    EditText pointsInput;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_points);

        indexNo = new UserSessionManager(getApplicationContext(), this).getIndexNo();

        pointsInput = (EditText)findViewById(R.id.pointsInput);
        progressBar = (ProgressBar)findViewById(R.id.myProgressBar);
        Button addPointsButton = (Button)findViewById(R.id.addActivityPointsButton);
        addPointsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pointsInput.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Fill field with your activity points", Toast.LENGTH_SHORT).show();
                } else {
                    String method = "UPDATE_POINTS";
                    points = Integer.parseInt(pointsInput.getText().toString());
                    Networking networking = (Networking) new Networking(progressBar, getApplicationContext(), new Networking.AsyncResponse() {
                        @Override
                        public void processFinish(String output) {
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("points", points);
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
                            overridePendingTransition(R.anim.fade_in, R.anim.slide_down);
                        }
                    }).execute(method, indexNo, points);
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
