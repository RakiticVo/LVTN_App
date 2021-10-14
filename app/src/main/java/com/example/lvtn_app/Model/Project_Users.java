package com.example.lvtn_app.Model;

public class Project_Users {
    String user_ID;
    String project_ID;
    String position;
    String key;

    public Project_Users(String user_ID, String project_ID, String position, String key) {
        this.user_ID = user_ID;
        this.project_ID = project_ID;
        this.position = position;
        this.key = key;
    }

    public Project_Users() {
    }

    public String getUser_ID() {
        return user_ID;
    }

    public void setUser_ID(String user_ID) {
        this.user_ID = user_ID;
    }

    public String getProject_ID() {
        return project_ID;
    }

    public void setProject_ID(String project_ID) {
        this.project_ID = project_ID;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
