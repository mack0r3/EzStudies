package com.example.mackor.ezstudies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mackor.ezstudies.BackEndTools.Networking;
import com.example.mackor.ezstudies.FrontEndTools.FontManager;

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

                String  indexNo = indexNoInput.getText().toString(),
                        password = passwordInput.getText().toString();
                LogStudent logStudent = new LogStudent(indexNo, password);

                if(!logStudent.emptyFieldFound()) {
                    String method = "LOGIN";
                    Networking networking = new Networking(getApplicationContext(), SignInActivity.this);
                    networking.execute(method, logStudent);
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
