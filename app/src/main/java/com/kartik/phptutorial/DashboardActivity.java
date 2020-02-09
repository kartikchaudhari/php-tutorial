package com.kartik.phptutorial;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.kartik.phptutorial.Classes.Constants;
import com.kartik.phptutorial.Fragments.AddPost;
import com.kartik.phptutorial.Fragments.ListPosts;
import com.kartik.phptutorial.Fragments.ManagePost;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private TextView tvFullName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //navigation drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,
                                                                R.string.navigation_drawer_open,
                                                                R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //navigation view
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //navigation header
        View headerView=navigationView.getHeaderView(0);
        tvFullName=headerView.findViewById(R.id.tvUserFullName);
        bindUserInfo();


        //load default fragment to activity
        Fragment fragment=new ListPosts();
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content_frame,fragment);
        ft.commit();

    }

    //onclick handler for left sidebar
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id=menuItem.getItemId();
        Fragment fragment=null;
        Intent intent=null;

        switch(id){

            case R.id.nav_list_post:
                fragment=new ListPosts();
                break;

            case R.id.nav_manage_post:
                fragment=new ManagePost();
                break;

            case R.id.nav_add_post:
                fragment=new AddPost();
                break;

        }

        if(fragment!=null){
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame,fragment);
            ft.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //onclick handler for topbar menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        boolean isLogOutSet=false;
        Fragment fragment=null;
        Intent intent=null;

        switch(id){
            case R.id.iteamCreatePost:
                fragment=new AddPost();
                break;

            case R.id.iteamSettings:
                intent=new Intent(DashboardActivity.this,SettingsActivity.class);
                break;

            case R.id.iteamContribute:
                intent=new Intent(DashboardActivity.this, ContributeActivity.class);
                break;

            case R.id.iteamDeveloper:
                intent=new Intent(DashboardActivity.this,DeveloperActivity.class);
                break;

            case R.id.iteamAboutApp:
                intent=new Intent(DashboardActivity.this,AboutAppActivity.class);
                break;

            case R.id.iteamLogout:
                    isLogOutSet=true;
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        if(fragment!=null){
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame,fragment);
            ft.commit();
        }else{
            if(intent!=null){
                startActivity(intent);
            }

            if(isLogOutSet==true){
                logout();
            }
        }


        return true;
    }


    //set title for current activity
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    //bind dashbaoard elements with logged in used information
    public void bindUserInfo(){
        Intent i=getIntent();
        tvFullName.setText(i.getStringExtra("user_full_name").toUpperCase());
    }

    //logs the user out
    public void logout(){
        SharedPreferences preferences =getSharedPreferences(Constants.USER_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        finish();

        Intent intent=new Intent(DashboardActivity.this,LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to Exit ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DashboardActivity.super.onBackPressed();
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
