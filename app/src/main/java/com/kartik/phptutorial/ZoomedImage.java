package com.kartik.phptutorial;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.kartik.phptutorial.Classes.Utility;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;

public class ZoomedImage extends AppCompatActivity {

    ImageView zoomedImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoomed_image);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //for back arrow in topbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent=getIntent();
        try{
            if(intent.getByteArrayExtra("post_title_image_zoomed")!=null){
                this.loadImage(intent.getByteArrayExtra("post_title_image_zoomed"));
            }
            
        }catch(Exception e){
            Utility.makeToast(getApplicationContext(),e.getLocalizedMessage());
        }

    }

    public void loadImage(byte[] b) {
        zoomedImg=findViewById(R.id.zoomImgView);
        final byte[] imgByte=b;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Show Image from DB in ImageView
                    zoomedImg.post(new Runnable() {
                        @Override
                        public void run() {
                            zoomedImg.setImageBitmap(Utility.getImage(imgByte));
                        }
                    });
                } catch (Exception e) {
                    Utility.makeToast(ZoomedImage.this,e.getLocalizedMessage());
                }
            }
        }).start();
    }

    //handle back button
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
