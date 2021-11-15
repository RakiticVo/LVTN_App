package com.example.lvtn_app.Model;

public class User_Issue_List {
    String issue_ID;
    String issue_Name;
    String issue_ProcessType;
    String issue_Type;
    String project_ID;
    String user_name;

    public User_Issue_List(String issue_ID,String issue_Name, String issue_ProcessType, String issue_Type, String project_ID, String user_name) {
        this.issue_ID = issue_ID;
        this.issue_Name = issue_Name;
        this.issue_ProcessType = issue_ProcessType;
        this.issue_Type = issue_Type;
        this.project_ID = project_ID;
        this.user_name = user_name;
    }

    public User_Issue_List() {
    }

    public String getIssue_ID() {
        return issue_ID;
    }

    public void setIssue_ID(String issue_ID) {
        this.issue_ID = issue_ID;
    }

    public String getIssue_Name() {
        return issue_Name;
    }

    public void setIssue_Name(String issue_Name) {
        this.issue_Name = issue_Name;
    }

    public String getIssue_ProcessType() {
        return issue_ProcessType;
    }

    public void setIssue_ProcessType(String issue_ProcessType) {
        this.issue_ProcessType = issue_ProcessType;
    }

    public String getIssue_Type() {
        return issue_Type;
    }

    public void setIssue_Type(String issue_Type) {
        this.issue_Type = issue_Type;
    }

    public String getProject_ID() {
        return project_ID;
    }

    public void setProject_ID(String project_ID) {
        this.project_ID = project_ID;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
