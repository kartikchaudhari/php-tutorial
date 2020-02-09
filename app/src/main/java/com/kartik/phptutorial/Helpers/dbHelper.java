package com.kartik.phptutorial.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kartik.phptutorial.Classes.Posts;
import com.kartik.phptutorial.Classes.Users;

import java.util.ArrayList;

public class dbHelper extends SQLiteOpenHelper {

    private  static final int db_version=1;
    private  static  final  String db_name="php_tutorials.db";
    
    public dbHelper(Context context) {
        super(context, db_name, null, db_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String users_table_sql="CREATE TABLE `users` ( `user_id` INTEGER PRIMARY KEY AUTOINCREMENT, `first_name` TEXT, `last_name` TEXT, `email` TEXT, `password` TEXT )";
        String posts_table_sql="CREATE TABLE `posts` ( `post_id` INTEGER PRIMARY KEY AUTOINCREMENT, `post_title` TEXT, `post_title_image` BLOB, `post_content` TEXT, `post_content_image` BLOB, `post_title_as_image` BLOB, `post_content_as_image` BLOB, `auther_id` INTEGER )";
        db.execSQL(users_table_sql);
        db.execSQL(posts_table_sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //database upgrade code
    }


    //user registration
    public static boolean registerUser(SQLiteDatabase db, Users user){
        ContentValues userData=new ContentValues();
        userData.put("first_name",user.getFirst_name());
        userData.put("last_name",user.getLast_name());
        userData.put("email",user.getEmail());
        userData.put("password",user.getPassword());

        if(db.insert("users",null,userData)!=-1){
            return true;
        }
        db.close();
        return false;
    }

    public static Cursor pullUserInfo(SQLiteDatabase db,Users user){
        String sql="SELECT * FROM users WHERE email='"+user.getEmail()+"' AND password='"+user.getPassword()+"'";
        Cursor cursor=db.rawQuery(sql,null);
        return cursor;
    }


    //create post
    public static boolean addPost(SQLiteDatabase db, Posts post){
        ContentValues postData=new ContentValues();
        postData.put("post_title",post.getPost_title());
        postData.put("post_title_image",post.getPost_title_image());
        postData.put("post_content",post.getPost_content());
        postData.put("post_content_image",post.getPost_content_image());
        postData.put("auther_id",post.getAuther_id());
        if(db.insert("posts",null,postData)!=-1){
            return true;
        }
        db.close();
        return false;
    }

    //list post
    public ArrayList<Posts> getAllPosts(){
        SQLiteDatabase database=this.getReadableDatabase();
        ArrayList<Posts> posts=new ArrayList<Posts>();

        Cursor cursor=database.rawQuery("SELECT * FROM posts ORDER BY post_id ASC",new String[]{});
        if(cursor.moveToFirst()){
            do{
                Posts post=new Posts();
                post.setPost_title(cursor.getString(cursor.getColumnIndex("post_title")));
                post.setPost_title_image(cursor.getBlob(cursor.getColumnIndex("post_title_image")));
                post.setPost_content(cursor.getString(cursor.getColumnIndex("post_content")));
                post.setPost_content_image(cursor.getBlob(cursor.getColumnIndex("post_content_image")));
                posts.add(post);
            }while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return posts;
    }

    public void deletePost(Posts post){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("posts", "post_id" + " = ?",
                new String[]{String.valueOf(post.getPost_id())});
        db.close();
    }
}
