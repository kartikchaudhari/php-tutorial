package com.kartik.phptutorial.Fragments;


import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.kartik.phptutorial.Classes.Posts;
import com.kartik.phptutorial.Classes.Utility;
import com.kartik.phptutorial.DashboardActivity;
import com.kartik.phptutorial.Helpers.dbHelper;
import com.kartik.phptutorial.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

public class AddPost extends Fragment {

    private EditText etPostTitle,etPost;
    private ToggleButton btnPost;
    private Button btnPostTitleImage,btnPostContentImage;
    private ImageView ivPostTitle,ivPostContent;
    private AwesomeValidation awesomeValidation;

    private static final int SELECT_PICTURE = 100;
    private Uri postTitleImageUri=null;
    private Uri postContentImageUri=null;

    private View globalView;

    public AddPost() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_add_post, container, false);

        globalView=view;

        //set parent activity title to the title of fragment
        ((DashboardActivity) getActivity()).setActionBarTitle("Add Post");

        //initialize the validation library
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        //initialize the controls
        etPost=view.findViewById(R.id.tvPost);
        etPostTitle=view.findViewById(R.id.tvPostTitle);
        btnPost=view.findViewById(R.id.btnCreatePost);
        ivPostTitle=view.findViewById(R.id.ivPostTitle);
        ivPostContent=view.findViewById(R.id.ivPostContent);
        btnPostTitleImage=view.findViewById(R.id.btnPostTitleImage);
        btnPostContentImage=view.findViewById(R.id.btnPostContentImage);


        //attach the validation
        awesomeValidation.addValidation(etPost,RegexTemplate.NOT_EMPTY,"Post C  ontents are Required.");
        awesomeValidation.addValidation(etPostTitle,RegexTemplate.NOT_EMPTY,"Post title Required.");

        //set the onclick listener
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check for the validation
                if(awesomeValidation.validate()){
                    //if true, insert the post
                    try {
                        doSharePost(getActivity().getApplicationContext(),
                                    etPostTitle.getText().toString().trim(),
                                    etPost.getText().toString().trim(),1,postTitleImageUri,postContentImageUri);
                    }
                    catch (Exception e) {
                        Utility.makeToast(getContext(),e.getMessage());
                    }
                }
            }
        });


        //onClick listner for selection of image
        btnPostTitleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser(1);
            }
        });
        btnPostContentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser(2);
            }
        });

        return view;
    }

    //choose image
    public  void openImageChooser(int request_code) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select Picture"), request_code);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch(requestCode){
                case 1:{
                    postTitleImageUri = data.getData();
                    if (null != postTitleImageUri) {
                        postTitleImageUri=data.getData();
                        btnPostTitleImage.setText("Image Selected");
                        btnPostTitleImage.setTextColor(getResources().getColor(R.color.colorPrimary));
                        try {
                            ivPostTitle.setImageBitmap(Utility.getImage(this.UriToByte(postTitleImageUri)));
                            ivPostTitle.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            Utility.makeToast(getContext(),e.getMessage());
                        }
                    }
                }
                break;

                case 2:{
                    postContentImageUri = data.getData();
                    if (null != postContentImageUri) {
                        postContentImageUri=data.getData();
                        btnPostContentImage.setText("Image Selected");
                        btnPostContentImage.setTextColor(getResources().getColor(R.color.colorPrimary));
                        try {
                            ivPostContent.setImageBitmap(Utility.getImage(this.UriToByte(postContentImageUri)));
                            ivPostContent.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            Utility.makeToast(getContext(),e.getMessage());
                        }
                    }
                }
            }
        }
    }

    //share post from user to database
    public void doSharePost(Context context,String post_title,String post_content,int auther_id,@Nullable Uri post_title_image_uri,@Nullable Uri post_content_image_uri) throws FileNotFoundException {
        SQLiteOpenHelper db_helper=new dbHelper(context);
        SQLiteDatabase db=db_helper.getWritableDatabase();

        byte[] post_title_image=null;
        byte[] post_content_image=null;
        if(post_title_image_uri!=null){
            post_title_image=this.UriToByte(post_title_image_uri);
        }

        if(post_content_image_uri!=null){
            post_content_image=this.UriToByte(post_content_image_uri);
        }
        if(post_title_image_uri!=null && post_content_image_uri!=null){
            post_title_image=this.UriToByte(post_title_image_uri);
            post_content_image=this.UriToByte(post_content_image_uri);
        }

        Posts post=new Posts(post_title,post_title_image,post_content,post_content_image,auther_id);
        if(dbHelper.addPost(db,post)){
            etPostTitle.setText(null);
            etPost.setText(null);
            ivPostTitle.setVisibility(View.GONE);
            btnPostTitleImage.setText(R.string.btn_select_image_post_title);
            ivPostContent.setVisibility(View.GONE);
            btnPostContentImage.setText(R.string.btn_select_image_post_content);


            //snackbar goes here
            Utility.makeSnakeBar(getView(),"Post Created Successfully.");
        }
        else{
            Utility.makeToast(context, "Error occurred while creating Post.");
        }
    }

    public byte[] UriToByte(Uri selectedImageUri) throws FileNotFoundException {
        InputStream iStream = getActivity().getApplicationContext().getContentResolver().openInputStream(selectedImageUri);
        byte[] inputData=null;
        try {
            inputData = Utility.getBytes(iStream);
        }
        catch (IOException e) {
           Utility.makeToast(getActivity().getApplicationContext(), e.getMessage());
        }
        return inputData;
    }
}
