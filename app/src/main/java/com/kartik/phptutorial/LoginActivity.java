package com.kartik.phptutorial;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.kartik.phptutorial.Classes.Users;
import com.kartik.phptutorial.Helpers.dbHelper;


public class LoginActivity extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    private TextView linkRegister,linkForgotPass;
    private TextView tvEmail,tvPassword;
    private Button btnLogin;
    private CheckBox cbRemember;
    private AwesomeValidation awesomeValidation;


    public static final String user_prefs = "user_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //auto login user
        autoLoginUser();

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        //link to open registration screen
        linkRegister=findViewById(R.id.lnkRegister);
        linkRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);
            }
        });

        //link to open forget password screen
        linkForgotPass=findViewById(R.id.lnkForgotPassword);
        linkForgotPass.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i=new Intent(LoginActivity.this,ForgetPasswordActivity.class);
                startActivity(i);
            }
        });

        //accept user input
        tvEmail=findViewById(R.id.tvEmail);
        tvPassword=findViewById(R.id.tvPassword);

        //attach validation to the fields
        awesomeValidation.addValidation(LoginActivity.this,R.id.tvEmail, RegexTemplate.NOT_EMPTY,R.string.emailError);
        awesomeValidation.addValidation(LoginActivity.this,R.id.tvEmail, Patterns.EMAIL_ADDRESS,R.string.emailError);
        awesomeValidation.addValidation(LoginActivity.this,R.id.tvPassword,".{6,}",R.string.passwordError);
        awesomeValidation.addValidation(LoginActivity.this,R.id.tvPassword,RegexTemplate.NOT_EMPTY,R.string.passwordError);
        
        //login button
        btnLogin=findViewById(R.id.loginBtn);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (awesomeValidation.validate()){
               try{
                   loginUser(tvEmail.getText().toString(),tvPassword.getText().toString());
               }
               catch (Exception e){
                   Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
               }
            }
            }
        });

    }

    public void  autoLoginUser(){
        //check for shared preference
        sharedpreferences = getSharedPreferences(user_prefs,Context.MODE_PRIVATE);

        if(sharedpreferences.contains("key_email") && sharedpreferences.contains("key_pass")){
            String email=sharedpreferences.getString("key_email",null);
            String name=sharedpreferences.getString("key_full_name",null);
            this.redirectDashboard(email,name);
        }
    }


    public void loginUser(String email,String pass){
        SQLiteOpenHelper db_helper=new dbHelper(LoginActivity.this);
        SQLiteDatabase db=db_helper.getWritableDatabase();

        Users user=new Users(email,pass);
        Cursor cursor=dbHelper.pullUserInfo(db,user);
        if (cursor.getCount()>0){
            if(cursor.moveToFirst()) {
                if (email.equals(cursor.getString(3)) && pass.equals(cursor.getString(4))) {
                    cbRemember=findViewById(R.id.cbRemember);
                    String name=cursor.getString(1)+" "+cursor.getString(2);
                    if(cbRemember.isChecked()){
                        this.SavePrefs(email,pass,name);
                    }
                    redirectDashboard(email,name);
                }
            }
        } else {
            Toast.makeText(this, "No user found..", Toast.LENGTH_SHORT).show();
        }
    }

    //redirect method jump to dashboard
    public void redirectDashboard(String email,String name){
        Intent i=new Intent(LoginActivity.this, DashboardActivity.class);
        i.putExtra("user_full_name",name);
        i.putExtra("user_email",email);
        startActivity(i);
    }


    //save to shared prefs
    public void SavePrefs(String email,String pass,String name){
        SharedPreferences.Editor editor=sharedpreferences.edit();
        editor.putString("key_email",email);
        editor.putString("key_pass",pass);
        editor.putString("key_full_name",name);
        editor.commit();
    }
    //handle back button
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to Exit ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LoginActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
}
