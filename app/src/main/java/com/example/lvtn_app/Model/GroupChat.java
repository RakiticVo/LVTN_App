package com.example.lvtn_app.Model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcelable;

import com.example.lvtn_app.R;

import java.util.ArrayList;

public class GroupChat{
    private int id;
    private String name;
    private String image1;
    private Uri uri_image;
    private String creator;
    private String last_message;
    private String last_sender;

    public GroupChat(int id, String name, String image1, String creator, String last_message, String last_sender) {
        this.id = id;
        this.name = name;
        this.image1 = image1;
        this.creator = creator;
        this.last_message = last_message;
        this.last_sender = last_sender;
    }

    public GroupChat(int id, String name, Uri uri_image, String creator) {
        this.id = id;
        this.name = name;
        this.uri_image = uri_image;
        this.creator = creator;
        this.last_message ="This group has been created";
    }

    public GroupChat(int id, String name, String creator) {
        this.id = id;
        this.name = name;
        this.creator = creator;
        this.image1 = String.valueOf(R.drawable.blueprint);
        this.last_message ="This group has been created";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public Uri getUriImage() {
        return uri_image;
    }

    public void setUriImage(Uri uri_image) {
        this.uri_image = uri_image;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getLast_message() {
        return last_message;
    }

    public void setLast_message(String last_message) {
        this.last_message = last_message;
    }

    public String getLast_sender() {
        return last_sender;
    }

    public void setLast_sender(String last_sender) {
        this.last_sender = last_sender;
    }
}
