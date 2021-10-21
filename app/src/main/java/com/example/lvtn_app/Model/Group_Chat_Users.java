package com.example.lvtn_app.Model;

public class Group_Chat_Users {
    String user_ID;
    String group_ID;
    String position;

    public Group_Chat_Users(String user_ID, String group_ID, String position, String key) {
        this.user_ID = user_ID;
        this.group_ID = group_ID;
        this.position = position;
    }

    public Group_Chat_Users() {
    }

    public String getUser_ID() {
        return user_ID;
    }

    public void setUser_ID(String user_ID) {
        this.user_ID = user_ID;
    }

    public String getGroup_ID() {
        return group_ID;
    }

    public void setGroup_ID(String group_ID) {
        this.group_ID = group_ID;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
