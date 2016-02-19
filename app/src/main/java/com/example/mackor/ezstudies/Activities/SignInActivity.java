package com.example.mackor.ezstudies.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mackor.ezstudies.BackEndTools.Networking;
import com.example.mackor.ezstudies.BackEndTools.UserSessionManager;
import com.example.mackor.ezstudies.FrontEndTools.FontManager;
import com.example.mackor.ezstudies.Models.LogStudent;
import com.example.mackor.ezstudies.R;

import org.json.JSONException;
import org.json.JSONObject;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        TextView indexNoIc = (TextView)findViewById(R.id.index_number_input_icon);
        TextView passwordIc = (TextView)findViewById(R.id.password_input_icon);

        final EditText indexNoInput = (EditText)findViewById(R.id.index_number_input);
        final EditText passwordInput = (EditText)findViewById(R.id.password_input);

        indexNoIc.setTypeface(FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME));
        passwordIc.setTypeface(FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME));

        Button signInButton = (Button)findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emptyFieldError = "All fields are required";

                final String  indexNo = indexNoInput.getText().toString(),
                        password = passwordInput.getText().toString();
                LogStudent logStudent = new LogStudent(indexNo, password);

                if(!logStudent.emptyFieldFound()) {
                    String method = "LOGIN";
                    Networking networking = (Networking)new Networking(getApplicationContext(), new Networking.AsyncResponse() {
                        @Override
                        public void processFinish(String output) {
                            try {
                                JSONObject json = new JSONObject(output);
                                Toast.makeText(getApplicationContext(), json.getString("message"), Toast.LENGTH_SHORT).show();
                                if(json.getString("success").equals("true"))
                                {
                                    UserSessionManager userSessionManager = new UserSessionManager(getApplicationContext(), SignInActivity.this);
                                    userSessionManager.createUserLogginSession(indexNo);
                                    Intent intent = new Intent(getApplicationContext(), UserPanelActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }).execute(method, logStudent);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), emptyFieldError, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }
    public void _(String message)
    {
        Log.v("ERRORS", message);
    }
}
