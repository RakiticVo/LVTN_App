package com.example.lvtn_app.Model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcelable;

import com.example.lvtn_app.R;

import java.util.ArrayList;

public class GroupChat{
    private int id_Group;
    private String groupName;
    private String groupImage;
    private String groupCreator;
    private String groupLastMess;
    private String groupLastSender;

    public GroupChat(int id_Group, String groupName, String groupImage, String groupCreator, String groupLastMess, String groupLastSender) {
        this.id_Group = id_Group;
        this.groupName = groupName;
        this.groupImage = groupImage;
        this.groupCreator = groupCreator;
        this.groupLastMess = groupLastMess;
        this.groupLastSender = groupLastSender;
    }

    public int getId_Group() {
        return id_Group;
    }

    public void setId_Group(int id_Group) {
        this.id_Group = id_Group;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupImage() {
        return groupImage;
    }

    public void setGroupImage(String groupImage) {
        this.groupImage = groupImage;
    }

    public String getGroupCreator() {
        return groupCreator;
    }

    public void setGroupCreator(String groupCreator) {
        this.groupCreator = groupCreator;
    }

    public String getGroupLastMess() {
        return groupLastMess;
    }

    public void setGroupLastMess(String groupLastMess) {
        this.groupLastMess = groupLastMess;
    }

    public String getGroupLastSender() {
        return groupLastSender;
    }

    public void setGroupLastSender(String groupLastSender) {
        this.groupLastSender = groupLastSender;
    }
}
