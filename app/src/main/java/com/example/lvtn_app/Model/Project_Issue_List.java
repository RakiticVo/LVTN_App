package com.example.lvtn_app.Model;

public class Project_Issue_List {
    String issue_ID;
    String project_ID;

    public Project_Issue_List(String issue_ID, String project_ID) {
        this.issue_ID = issue_ID;
        this.project_ID = project_ID;
    }

    public Project_Issue_List() {
    }

    public String getIssue_ID() {
        return issue_ID;
    }

    public void setIssue_ID(String issue_ID) {
        this.issue_ID = issue_ID;
    }

    public String getProject_ID() {
        return project_ID;
    }

    public void setProject_ID(String project_ID) {
        this.project_ID = project_ID;
    }
}
