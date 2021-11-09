package com.example.log;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class Homepage extends AppCompatActivity implements View.OnClickListener {

    //programing for the button to be recognised and work
    Button buttonAddUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        buttonAddUser = findViewById(R.id.btn_add_user);
        buttonAddUser.setOnClickListener(this);

    }

    @Override //Makes it so when you click buttonAddUser, it sends you back to MainActivity.class so you can add more credentials
    public void onClick(View v) {

        if(v==buttonAddUser){

            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }

    }
}
