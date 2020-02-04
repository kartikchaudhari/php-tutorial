package com.kartik.phptutorial;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.kartik.phptutorial.Classes.Users;
import com.kartik.phptutorial.Helpers.dbHelper;

public class RegisterActivity extends AppCompatActivity {

    private TextView tvFistName,tvLastName,tvEmail,tvPassword,tvConfirmPassword;
    private Button btnRegister;
    private AwesomeValidation awesomeValidation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        tvFistName=findViewById(R.id.tvFirstName);
        tvLastName=findViewById(R.id.tvLastName);
        tvEmail=findViewById(R.id.tvEmail);
        tvPassword=findViewById(R.id.tvPassword);
        tvConfirmPassword=findViewById(R.id.tvConfirmPassword);
        btnRegister=findViewById(R.id.btnRegister);

        //adding validations
        awesomeValidation.addValidation(this,R.id.tvFirstName, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$",R.string.firstNameError);
        awesomeValidation.addValidation(this,R.id.tvLastName,"^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$",R.string.lastNameError);
        awesomeValidation.addValidation(this,R.id.tvEmail, Patterns.EMAIL_ADDRESS,R.string.emailError);
        awesomeValidation.addValidation(this,R.id.tvPassword,".{6,}",R.string.passwordError);
        awesomeValidation.addValidation(RegisterActivity.this, R.id.tvConfirmPassword, RegexTemplate.NOT_EMPTY,R.string.confirmPasswordError);

        btnRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(awesomeValidation.validate()){
                    doRegister(RegisterActivity.this,tvFistName.getText().toString(),tvLastName.getText().toString(),tvEmail.getText().toString(),tvPassword.getText().toString(),tvConfirmPassword.getText().toString());
                }
                else{
                    Toast.makeText(RegisterActivity.this, "Error occurred while registration.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void doRegister(Context context, String firstName, String lastName, String email, String password, String confirmPassword){
        SQLiteOpenHelper db_helper=new dbHelper(this);
        SQLiteDatabase db=db_helper.getWritableDatabase();
        Users user=new Users(firstName,lastName,email,password);

        if(dbHelper.registerUser(db,user)){
            Toast.makeText(context, "Success: User Registered Successfully.", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context, "Error occurred while registration.", Toast.LENGTH_SHORT).show();
        }
    }

    //public void
}
