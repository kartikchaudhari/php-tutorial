package com.kartik.phptutorial.Fragments;


import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.kartik.phptutorial.Classes.Posts;
import com.kartik.phptutorial.Classes.Utility;
import com.kartik.phptutorial.Helpers.dbHelper;
import com.kartik.phptutorial.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

public class AddPost extends Fragment {

    private TextView tvAuthor;
    private EditText etPostTitle,etPost;
    private ToggleButton btnPost;
    private Button btnPostTitleImage,btnPostContentImage;
    private AwesomeValidation awesomeValidation;

    private static final int SELECT_PICTURE = 100;
    private Uri selectedImageUri=null;
    public AddPost() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_add_post, container, false);

        //initialize the validation library
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        //initialize the controls
        etPost=view.findViewById(R.id.tvPost);
        etPostTitle=view.findViewById(R.id.tvPostTitle);
        btnPost=view.findViewById(R.id.btnCreatePost);
        btnPostTitleImage=view.findViewById(R.id.btnPostTitleImage);
        btnPostContentImage=view.findViewById(R.id.btnPostContentImage);


        //attach the validation
        awesomeValidation.addValidation(etPost,RegexTemplate.NOT_EMPTY,"Post Contents are Required.");
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
                                    etPost.getText().toString().trim(),1,selectedImageUri);
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
                openImageChooser();
            }
        });

        btnPostContentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });


        return view;
    }

    //choose image
    public  void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    selectedImageUri=data.getData();
                    btnPostTitleImage.setText("Image Selected");
                    btnPostTitleImage.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        }
    }

    //share post from user to database
    public void doSharePost(Context context,String post_title,String post_content,int auther_id,@Nullable Uri post_title_image_uri) throws FileNotFoundException {
        SQLiteOpenHelper db_helper=new dbHelper(context);
        SQLiteDatabase db=db_helper.getWritableDatabase();

        byte[] post_title_image=null;
        if(post_title_image_uri!=null){
            post_title_image=this.UriToByte(post_title_image_uri);
        }

        Posts post=new Posts(post_title,post_title_image,post_content,auther_id);
        if(dbHelper.addPost(db,post)){
            etPostTitle.setText(null);
            etPost.setText(null);
            btnPostTitleImage.setText(R.string.btn_select_image_post_title);
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
