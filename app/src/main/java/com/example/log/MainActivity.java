package com.example.log;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Map;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity implements OnClick {

    private EditText eName;
    private EditText ePassword;
    private Button eLogin;
    private TextView eAttemptsInfo;
    private CheckBox eRememberMe;
    //how many attempts to login users have
    boolean isValid = false;
    private int counter = 5;

    public Credentials credentials;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        eName = findViewById(R.id.etUsername);
        ePassword = findViewById(R.id.etPassword);
        eLogin = findViewById(R.id.Login);
        eAttemptsInfo = findViewById(R.id.SignInAttempts);
        TextView eRegister = findViewById(R.id.tvRegister);
        eRememberMe = findViewById(R.id.cbRememberMe);
        //credentials
        credentials = new Credentials();

        sharedPreferences = getApplicationContext().getSharedPreferences("CredentialsDB", MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();

        if (sharedPreferences != null) {

            Map<String, ?> preferencesMap = sharedPreferences.getAll();

            if (preferencesMap.size() != 0) {
                credentials.loadCredentials(preferencesMap);
            }

            String savedUsername = sharedPreferences.getString("LastSavedUsername", "");
            String savedPassword = sharedPreferences.getString("LastSavedPassword", "");

            if (sharedPreferences.getBoolean("RememberMeCheckbox", false)) {
                eName.setText(savedUsername);
                ePassword.setText(savedPassword);
                eRememberMe.setChecked(true);
            }
        }
        //makes it so when user clicks remember me that it saves the details of the last user
        eRememberMe.setOnClickListener(v -> {

            sharedPreferencesEditor.putBoolean("RememberMeCheckbox", eRememberMe.isChecked());
            sharedPreferencesEditor.apply();
        });

        eRegister.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, RegistrationActivity.class)));

        //controls what the login button does and checks on click
        eLogin.setOnClickListener(v -> {

            if (v == eLogin) {

                Intent intent = new Intent(getApplicationContext(), Homepage.class);
                startActivity(intent);
            }

            String inputName = eName.getText().toString();
            String inputPassword = ePassword.getText().toString();
            //checks and validates whether details entered are correct
            if (inputName.isEmpty() || inputPassword.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter all details", Toast.LENGTH_SHORT).show();
            } else {

                isValid = validate(inputName, inputPassword);

                if (!isValid) {

                    counter--;

                    Toast.makeText(MainActivity.this, "Incorrect credentials entered", Toast.LENGTH_SHORT).show();

                    eAttemptsInfo.setText("No. of attempts remaining: " + counter);
                    //If user runs out of attempts, disable the login button
                    if (counter == 0) {
                        eLogin.setEnabled(false);
                    }


                } else {
                    Toast.makeText(MainActivity.this, "Correct credentials entered", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this, Homepage.class);
                    startActivity(intent);
                }
            }

        });
    }

    private boolean validate(String name, String password) {
        return credentials.checkCredentials(name, password);
    }

    //Code to add users to sheet
    private void addUsersToSheet() {
        final ProgressDialog loading = ProgressDialog.show(this, "Adding User", "Please wait");
        final String name = eName.getText().toString().trim();
        final String password = ePassword.getText().toString().trim();
        //contexts to google web app to add users
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbwCxmDf4tL8sB0YZ-tPd6woCnTEeTmTw1W2HCjhtfhJY2eQ4Qs/exec",
                response -> {

                    loading.dismiss();
                    Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), Homepage.class);
                    startActivity(intent);

                },
                error -> {

                }
        ) {
            @Override //parameters to put add into the google spreadsheet
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("action", "addUser");
                params.put("Username", name);
                params.put("Password", password);

                return params;
            }
        };
        //time allowed before timing out if it doesn't work, for example, 50000 = 50 seconds
        int socketTimeOut = 50000;

        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);


    }
        //What happens when button is clicked
        private void onClick(View v) {

            if (v == eLogin) {
             addUsersToSheet();
         }
     }
}