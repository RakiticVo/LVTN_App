package com.example.lvtn_app.Model;

public class User_Issue_List {
    String issue_ID;
    String user_name;
    String project_ID;

    public User_Issue_List(String issue_ID, String user_name, String project_ID) {
        this.issue_ID = issue_ID;
        this.user_name = user_name;
        this.project_ID = project_ID;
    }

    public User_Issue_List() {
    }

    public String getIssue_ID() {
        return issue_ID;
    }

    public void setIssue_ID(String issue_ID) {
        this.issue_ID = issue_ID;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getProject_ID() {
        return project_ID;
    }

    public void setProject_ID(String project_ID) {
        this.project_ID = project_ID;
    }
}
