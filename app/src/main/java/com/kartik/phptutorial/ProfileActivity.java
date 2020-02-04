package com.kartik.phptutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    private TextView lblEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        lblEmail=findViewById(R.id.lblEmail);


        //catch intent here
        Intent i=getIntent();
        lblEmail.setText(i.getStringExtra("email"));
    }
}
