package com.kartik.phptutorial.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kartik.phptutorial.Helpers.dbHelper;

import com.kartik.phptutorial.Classes.Posts;
import com.kartik.phptutorial.Classes.Utility;
import com.kartik.phptutorial.Listners.ItemClickListener;
import com.kartik.phptutorial.R;

import java.util.List;
public class ManagePostListAdapter extends RecyclerView.Adapter<ManagePostListAdapter.ManagePostsListViewHolder> {

    Context context;
    List<Posts> postsList;
    ItemClickListener clickListener;
    dbHelper db;
    //view holder class
    public class ManagePostsListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView postTitle;
        public ImageView ivEdit,ivDelete;
        public ManagePostsListViewHolder(View itemView) {
            super(itemView);
            postTitle=itemView.findViewById(R.id.list_item_text);
            ivEdit=itemView.findViewById(R.id.ivEdit);
            ivDelete=itemView.findViewById(R.id.ivDelete);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) clickListener.onClick(v, getAdapterPosition());
        }
    }



    public ManagePostListAdapter(Context context, List<Posts> postsList) {
        this.context = context;
        this.postsList = postsList;
        this.db=new dbHelper(context);
    }


    @Override
    public ManagePostsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v= LayoutInflater.from(context).inflate(R.layout.manage_list_iteam,parent,false);
        ManagePostListAdapter.ManagePostsListViewHolder managePostsListViewHolder=new ManagePostListAdapter.ManagePostsListViewHolder(v);
        return managePostsListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ManagePostsListViewHolder holder, final int position) {
        holder.postTitle.setText(postsList.get(position).getPost_title());
        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    delete(position);
                }
                catch(Exception e){
                    Utility.makeToast(context,e.getMessage());
                }
            }
        });

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.makeToast(context,Integer.toString(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }


    //delete method
    public void delete(int position){
        //delete data from db
        db.deletePost(postsList.get(position));

        //remove node from the list
        postsList.remove(position);
    }

    //edit method
}
