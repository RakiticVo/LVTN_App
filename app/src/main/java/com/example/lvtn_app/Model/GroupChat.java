package com.example.lvtn_app.Model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcelable;

import com.example.lvtn_app.R;

import java.util.ArrayList;

public class GroupChat{
    private String group_ID;
    private String group_Name;
    private String group_Image;
    private String group_Creator;
    private String group_LastMess;
    private String group_LastSender;

    public GroupChat(String group_ID, String group_Name, String group_Image, String group_Creator, String group_LastMess, String group_LastSender) {
        this.group_ID = group_ID;
        this.group_Name = group_Name;
        this.group_Image = group_Image;
        this.group_Creator = group_Creator;
        this.group_LastMess = group_LastMess;
        this.group_LastSender = group_LastSender;
    }

    public GroupChat() {
    }

    public String getGroup_ID() {
        return group_ID;
    }

    public void setGroup_ID(String group_ID) {
        this.group_ID = group_ID;
    }

    public String getGroup_Name() {
        return group_Name;
    }

    public void setGroup_Name(String group_Name) {
        this.group_Name = group_Name;
    }

    public String getGroup_Image() {
        return group_Image;
    }

    public void setGroup_Image(String group_Image) {
        this.group_Image = group_Image;
    }

    public String getGroup_Creator() {
        return group_Creator;
    }

    public void setGroup_Creator(String group_Creator) {
        this.group_Creator = group_Creator;
    }

    public String getGroup_LastMess() {
        return group_LastMess;
    }

    public void setGroup_LastMess(String group_LastMess) {
        this.group_LastMess = group_LastMess;
    }

    public String getGroup_LastSender() {
        return group_LastSender;
    }

    public void setGroup_LastSender(String group_LastSender) {
        this.group_LastSender = group_LastSender;
    }
}
