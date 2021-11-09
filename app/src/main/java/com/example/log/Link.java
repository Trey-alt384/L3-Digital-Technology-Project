package com.example.log;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Link extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link);
        //links buttons to their id so that they work
        Button eScan = findViewById(R.id.Scan);
        Button eManual = findViewById(R.id.Manual);
        //When clicked, sends to Scan.class
        eScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Link.this, Scan.class);
                startActivity(intent);
            }
        });
        //When clicked, sends to MainActivity.class
        eManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Link.this, MainActivity.class);
                startActivity(intent);
            }

        });
    }

}