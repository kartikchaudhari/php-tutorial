package com.kartik.phptutorial;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.kartik.phptutorial.Classes.Utility;

public class SinglePostActivity extends AppCompatActivity {

    TextView tvPostTitle,tvPostContent;
    ImageView ivPostTitle,ivPostContent;
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
                this.loadImage(intent.getByteArrayExtra("post_title_image"),1);
            }

            if(intent.getByteArrayExtra("post_content_image")!=null){
                this.loadImage(intent.getByteArrayExtra("post_content_image"),2);
            }
        }
        catch(Exception e){
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

    public void loadImage(byte[] b,int imageCase) {
       switch(imageCase){
           case 1:{
               ivPostTitle=findViewById(R.id.ivPostTitle);
               ivPostTitle.setVisibility(View.VISIBLE);
               final byte[] postTitleImage=b;
               new Thread(new Runnable() {
                   @Override
                   public void run() {
                       try {
                           // Show Image from DB in ImageView
                           ivPostTitle.post(new Runnable() {
                               @Override
                               public void run() {
                                   ivPostTitle.setImageBitmap(Utility.getImage(postTitleImage));
                               }
                           });
                       } catch (Exception e) {
                           Utility.makeToast(SinglePostActivity.this,e.getLocalizedMessage());
                       }
                   }
               }).start();
               ivPostTitle.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Intent i=new Intent(SinglePostActivity.this, ZoomedImage.class);
                       i.putExtra("post_title_image_zoomed",postTitleImage);
                       startActivity(i);
                   }
               });

           }
           break;

           case 2:{
               ivPostContent=findViewById(R.id.ivPostContent);
               ivPostContent.setVisibility(View.VISIBLE);
               final byte[] postContentImage=b;
               new Thread(new Runnable() {
                   @Override
                   public void run() {
                       try {
                           // Show Image from DB in ImageView
                           ivPostContent.post(new Runnable() {
                               @Override
                               public void run() {
                                   ivPostContent.setImageBitmap(Utility.getImage(postContentImage));
                               }
                           });

                       } catch (Exception e) {
                           Utility.makeToast(SinglePostActivity.this,e.getLocalizedMessage());
                       }
                   }
               }).start();

               ivPostContent.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Intent i=new Intent(SinglePostActivity.this, ZoomedImage.class);
                       i.putExtra("post_title_image_zoomed",postContentImage);
                       startActivity(i);
                   }
               });
           }
       }
    }
}
