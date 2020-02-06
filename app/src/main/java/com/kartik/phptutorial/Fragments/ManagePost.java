package com.kartik.phptutorial.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.kartik.phptutorial.Adapters.ManagePostListAdapter;
import com.kartik.phptutorial.Classes.Posts;
import com.kartik.phptutorial.Classes.Utility;
import com.kartik.phptutorial.DashboardActivity;
import com.kartik.phptutorial.Helpers.dbHelper;
import com.kartik.phptutorial.R;

import java.util.List;

public class ManagePost extends Fragment{

    View v;
    RecyclerView recyclerView;
    List<Posts> postsList;
    SwipeRefreshLayout swipeRefreshLayout;
    ManagePostListAdapter adapter;

    public ManagePost() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_manage_post, container, false);

        //set parent activity title to the title of fragment
        ((DashboardActivity) getActivity()).setActionBarTitle("Manage Posts");

        recyclerView=v.findViewById(R.id.rvManageLists);

        adapter=new ManagePostListAdapter(getContext(),postsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        //swipe to refresh recyclerView
        swipeRefreshLayout = v.findViewById(R.id.swipe_view);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try{
                    fillList();
                    recyclerView.setAdapter(adapter);
                }catch(Exception e){
                    Utility.makeToast(getActivity().getApplicationContext(),e.getMessage());
                }
                Utility.makeToast(getActivity().getApplicationContext(),"List Refreshed");
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            this.fillList();
        }catch(Exception e){
            Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    public void fillList(){
        dbHelper dbHelper=new dbHelper(getActivity().getApplicationContext());
        postsList=dbHelper.getAllPosts();
    }

}
