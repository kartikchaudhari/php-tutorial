package com.kartik.phptutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {
    private TextView tv;
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        tv= findViewById(R.id.tvWelcome);
        iv= findViewById(R.id.ivLogo);

        //code to attach animation
        Animation animation= AnimationUtils.loadAnimation(this,R.anim.transition);
        tv.startAnimation(animation);
        iv.startAnimation(animation);

        //start thread and run new activity
        Thread t=new Thread(){
            public void run(){
                try{
                    sleep(3000);
                    Intent i=new Intent(getBaseContext(),LoginActivity.class);
                    startActivity(i);
                    finish();
                }
                catch (Exception e){
                    Toast.makeText(SplashActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };

        t.start();


    }
}
