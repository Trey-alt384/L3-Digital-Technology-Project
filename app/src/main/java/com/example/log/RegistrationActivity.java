package com.example.log;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private EditText eRegName;
    private EditText eRegPassword;
    private Button eRegister;

    public Credentials credentials;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        //registers to each button and id, e.g. eRegName in code = button with id etRegName
        eRegName = findViewById(R.id.etRegName);
        eRegPassword = findViewById(R.id.etRegPassword);
        eRegister = findViewById(R.id.btnRegister);

        credentials = new Credentials();
        //shared preference is used for the credentials database, which is kept private so you can't see for security reasons
        sharedPreferences = getApplicationContext().getSharedPreferences("CredentialsDB", MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();

        if(sharedPreferences != null){

            Map<String, ?> preferencesMap = sharedPreferences.getAll();

            if (preferencesMap.size() != 0) {
                credentials.loadCredentials(preferencesMap);
            }
        }
        //When register button is clicked, registers the users account while checking if a username is taken, then sends user back to MainActivity.class
        eRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String regUsername = eRegName.getText().toString();
                String regPassword = eRegPassword.getText().toString();

                if(validate(regUsername, regPassword)){
                    //checks username to see if it is already taken
                    if(credentials.checkUsername(regUsername)){
                        Toast.makeText(RegistrationActivity.this, "Username is taken", Toast.LENGTH_SHORT).show();
                    }else{
                        //adds credentials to offline database so that it can be reused
                        credentials.addCredentials(regUsername, regPassword);
                        //saves the last used username and last used password
                        sharedPreferencesEditor.putString(regUsername, regPassword);
                        sharedPreferencesEditor.putString("LastSavedUsername", "");
                        sharedPreferencesEditor.putString("LastSavedPassword","");
                        sharedPreferencesEditor.apply();
                        //returns user to sign in page
                        startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                    }




                }


            }
        });

    }

    private boolean validate(String username, String password){

        if(username.isEmpty() || password.length() <8){
            Toast.makeText(this,"Password should be 8 characters at minimum", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}