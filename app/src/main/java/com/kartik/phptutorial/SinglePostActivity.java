package com.kartik.phptutorial;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.kartik.phptutorial.Classes.Utility;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SinglePostActivity extends AppCompatActivity {

    TextView tvPostTitle,tvPostContent;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //for back arrow in topbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tvPostTitle=findViewById(R.id.tvPostTitle);
        tvPostContent=findViewById(R.id.tvPostContent);


        Intent intent=getIntent();
        tvPostTitle.setText(intent.getStringExtra("post_title").trim());
        tvPostContent.setText(intent.getStringExtra("post_content").trim());
        try{
            if(intent.getByteArrayExtra("post_title_image")!=null){
                this.loadImage(intent.getByteArrayExtra("post_title_image"));
            }
            else{
                img=findViewById(R.id.img);
                img.setVisibility(View.GONE);
            }
        }catch(Exception e){
            Utility.makeToast(getApplicationContext(),e.getMessage());
        }

    }


    //handle that back arrow
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.single_post_topbar_menu, menu);
        return true;
    }

    public void loadImage(byte[] b) {
        img=findViewById(R.id.img);
        final byte[] imgByte=b;
                new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Show Image from DB in ImageView
                    img.post(new Runnable() {
                        @Override
                        public void run() {
                            img.setImageBitmap(Utility.getImage(imgByte));
                        }
                    });
                } catch (Exception e) {
                    Utility.makeToast(SinglePostActivity.this,e.getLocalizedMessage());
                }
            }
        }).start();

                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i=new Intent(SinglePostActivity.this, ZoomedImage.class);
                        i.putExtra("post_title_image_zoomed",imgByte);
                        startActivity(i);
                    }
                });
    }

}
