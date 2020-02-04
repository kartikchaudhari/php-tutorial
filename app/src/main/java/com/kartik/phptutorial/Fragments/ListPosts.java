package com.kartik.phptutorial.Fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kartik.phptutorial.Adapters.PostListAdapter;
import com.kartik.phptutorial.Classes.Posts;
import com.kartik.phptutorial.Classes.Utility;
import com.kartik.phptutorial.Helpers.dbHelper;
import com.kartik.phptutorial.Listners.ItemClickListener;
import com.kartik.phptutorial.R;
import com.kartik.phptutorial.SinglePostActivity;

import java.util.List;

public class ListPosts extends Fragment implements ItemClickListener {


    View v;
    RecyclerView recyclerView;
    List<Posts> postsList;
    SwipeRefreshLayout swipeRefreshLayout;
    PostListAdapter adapter;

    public ListPosts() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_list_posts, container, false);

        recyclerView=v.findViewById(R.id.rvPostLists);

        adapter=new PostListAdapter(getContext(),postsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);
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

    return  v;
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

    @Override
    public void onClick(View view, int position) {
        final  Posts post=postsList.get(position);
        Intent intent=new Intent(getActivity().getApplicationContext(), SinglePostActivity.class);
        intent.putExtra("post_title",post.getPost_title());
        intent.putExtra("post_content",post.getPost_content());
        intent.putExtra("post_title_image",post.getPost_title_image());
        startActivity(intent);
    }
}
