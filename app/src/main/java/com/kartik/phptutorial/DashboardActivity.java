package com.kartik.phptutorial;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.navigation.NavigationView;
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

                default:
                    fragment=new ListPosts();
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        Fragment fragment=null;
        Intent intent=null;

        switch(id){

            case R.id.iteamCreatePost:
                fragment=new AddPost();
                break;

            case R.id.nav_manage_post:
                fragment=new ManagePost();
                break;

            case R.id.nav_add_post:
                fragment=new AddPost();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        if(fragment!=null){
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame,fragment);
            ft.commit();
        }

        return true;


    }
    //set title for current activity
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public void bindUserInfo(){
        Intent i=getIntent();
        tvFullName.setText(i.getStringExtra("user_full_name").toUpperCase());
    }


}
