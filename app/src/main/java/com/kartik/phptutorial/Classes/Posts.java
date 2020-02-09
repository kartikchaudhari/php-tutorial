package com.kartik.phptutorial.Classes;

public class Posts {
    private int post_id;
    private  String post_title;
    private byte[] post_title_image;
    private  String post_content;
    private byte[] post_content_image;
    private  int auther_id;



    public Posts() {}

    public Posts(int post_id, String post_title, String post_content, int auther_id) {
        this.post_id = post_id;
        this.post_title = post_title;
        this.post_content = post_content;
        this.auther_id = auther_id;
    }

    public Posts(String post_title) {
        this.post_title = post_title;
    }

    public Posts(String post_title, String post_content, int auther_id) {
        this.post_title = post_title;
        this.post_content = post_content;
        this.auther_id = auther_id;
    }

    public Posts(String post_title, byte[] post_title_image, String post_content, int auther_id) {
        this.post_title = post_title;
        this.post_title_image = post_title_image;
        this.post_content = post_content;
        this.auther_id = auther_id;
    }

    public Posts(String post_title, byte[] post_title_image, String post_content, byte[] post_content_image, int auther_id) {
        this.post_title = post_title;
        this.post_title_image = post_title_image;
        this.post_content = post_content;
        this.post_content_image = post_content_image;
        this.auther_id = auther_id;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public byte[] getPost_title_image() {
        return post_title_image;
    }

    public void setPost_title_image(byte[] post_title_image) {
        this.post_title_image = post_title_image;
    }

    public String getPost_content() {
        return post_content;
    }

    public void setPost_content(String post_content) {
        this.post_content = post_content;
    }

    public byte[] getPost_content_image() {
        return post_content_image;
    }

    public void setPost_content_image(byte[] post_content_image) {
        this.post_content_image = post_content_image;
    }

    public int getAuther_id() {
        return auther_id;
    }

    public void setAuther_id(int auther_id) {
        this.auther_id = auther_id;
    }


}
