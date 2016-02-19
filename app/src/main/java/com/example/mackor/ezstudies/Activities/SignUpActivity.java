package com.example.mackor.ezstudies.Activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mackor.ezstudies.BackEndTools.Networking;
import com.example.mackor.ezstudies.FrontEndTools.FontManager;
import com.example.mackor.ezstudies.Models.NewStudent;
import com.example.mackor.ezstudies.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        TextView fnameIc = (TextView)findViewById(R.id.first_name_input_icon);
        TextView lnameIc = (TextView)findViewById(R.id.last_name_input_icon);
        TextView indexNoIc = (TextView)findViewById(R.id.index_number_input_icon);
        TextView groupIc = (TextView)findViewById(R.id.group_input_icon);
        TextView passwordIc = (TextView)findViewById(R.id.password_input_icon);

        final EditText fnameInput = (EditText)findViewById(R.id.first_name_input);
        final EditText lnameInput = (EditText)findViewById(R.id.last_name_input);
        final EditText indexNoInput = (EditText)findViewById(R.id.index_number_input);
        final EditText passwordInput = (EditText)findViewById(R.id.password_input);

        fnameIc.setTypeface(FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME));
        lnameIc.setTypeface(FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME));
        indexNoIc.setTypeface(FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME));
        groupIc.setTypeface(FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME));
        passwordIc.setTypeface(FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME));


        /* Setting up spinner */
        final Spinner groupSpinner = (Spinner)findViewById(R.id.group_spinner);

        String[] groupListRes = getResources().getStringArray(R.array.group_array);
        List<String> groupList = Arrays.asList(groupListRes);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, groupList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupSpinner.setAdapter(adapter);
        /*         --        */

        Button signUpButton = (Button)findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideKeyboard();

                String  fname = fnameInput.getText().toString(),
                        lname = lnameInput.getText().toString(),
                        indexNo = indexNoInput.getText().toString(),
                        group = groupSpinner.getSelectedItem().toString(),
                        password = passwordInput.getText().toString();

                NewStudent newStudent = new NewStudent(fname, lname, indexNo, group, password);

                String invalidNameError = "Your name is invalid";
                String invalidIndexError = "Index has to consist of 6 digits";
                String invalidPasswordError = "Password length has to be from 5 up to 20";
                String emptyFieldError = "All fields are required";

                if(!newStudent.emptyFieldFound()) {
                    if(newStudent.validateName()) {
                        if(newStudent.validateIndexNo()) {
                            if(newStudent.validatePassword()) {
                                //Register user
                                String method = "REGISTER";
                                //Najzajebistsza metoda na wysylanie odpowiedzi AsyncTask do innego activity <3
                                Networking networking = (Networking)new Networking(getApplicationContext(), new Networking.AsyncResponse() {
                                    @Override
                                    public void processFinish(String output) {
                                        try {
                                            JSONObject json = new JSONObject(output);
                                            Toast.makeText(getApplicationContext(), json.getString("message"), Toast.LENGTH_SHORT).show();
                                            if(json.getString("success").equals("true"))finish();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).execute(method, newStudent);

                            } else {
                                Toast toast = Toast.makeText(getApplicationContext(), invalidPasswordError, Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(), invalidIndexError, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), invalidNameError, Toast.LENGTH_SHORT);
                        toast.show();
                    }
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

    public void hideKeyboard()
    {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }


}
