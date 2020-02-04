package com.kartik.phptutorial.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kartik.phptutorial.Classes.Posts;
import com.kartik.phptutorial.Listners.ItemClickListener;
import com.kartik.phptutorial.R;

import java.util.List;
public class ManagePostListAdapter extends RecyclerView.Adapter<ManagePostListAdapter.ManagePostsListViewHolder> {

    Context context;
    List<Posts> postsList;
    ItemClickListener clickListener;

    public ManagePostListAdapter(Context context, List<Posts> postsList) {
        this.context = context;
        this.postsList = postsList;
    }


    @Override
    public ManagePostsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v= LayoutInflater.from(context).inflate(R.layout.manage_list_iteam,parent,false);
        ManagePostListAdapter.ManagePostsListViewHolder managePostsListViewHolder=new ManagePostListAdapter.ManagePostsListViewHolder(v);
        return managePostsListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ManagePostsListViewHolder holder, int position) {
        holder.postTitle.setText(postsList.get(position).getPost_title());
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    public void setClickListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }



    public class ManagePostsListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView postTitle;
        public ManagePostsListViewHolder(View itemView) {
            super(itemView);
            postTitle=itemView.findViewById(R.id.list_item_text);
            itemView.setTag(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) clickListener.onClick(v, getAdapterPosition());
        }
    }
}
