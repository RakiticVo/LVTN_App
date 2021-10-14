package com.example.lvtn_app.Model;

public class Issue {
    private String issue_ID;
    private String issue_Name;
    private String issue_ProcessType;
    private String issue_Description;
    private String issue_Type;
    private String issue_StartDate;
    private String issue_Priority;
    private String issue_Assignee;
    private String issue_EstimateTime;
    private String issue_Creator;
    private String issue_project_ID;
    private String issue_FinishDate;

    public Issue(String issue_ID, String issue_Name, String issue_ProcessType, String issue_Description, String issue_Type, String issue_StartDate, String issue_Priority, String issue_Assignee, String issue_EstimateTime, String issue_Creator, String issue_project_ID, String issue_FinishDate) {
        this.issue_ID = issue_ID;
        this.issue_Name = issue_Name;
        this.issue_ProcessType = issue_ProcessType;
        this.issue_Description = issue_Description;
        this.issue_Type = issue_Type;
        this.issue_StartDate = issue_StartDate;
        this.issue_Priority = issue_Priority;
        this.issue_Assignee = issue_Assignee;
        this.issue_EstimateTime = issue_EstimateTime;
        this.issue_Creator = issue_Creator;
        this.issue_project_ID = issue_project_ID;
        this.issue_FinishDate = issue_FinishDate;
    }

    public Issue() {
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

    public String getIssue_Description() {
        return issue_Description;
    }

    public void setIssue_Description(String issue_Description) {
        this.issue_Description = issue_Description;
    }

    public String getIssue_Type() {
        return issue_Type;
    }

    public void setIssue_Type(String issue_Type) {
        this.issue_Type = issue_Type;
    }

    public String getIssue_StartDate() {
        return issue_StartDate;
    }

    public void setIssue_StartDate(String issue_StartDate) {
        this.issue_StartDate = issue_StartDate;
    }

    public String getIssue_Priority() {
        return issue_Priority;
    }

    public void setIssue_Priority(String issue_Priority) {
        this.issue_Priority = issue_Priority;
    }

    public String getIssue_Assignee() {
        return issue_Assignee;
    }

    public void setIssue_Assignee(String issue_Assignee) {
        this.issue_Assignee = issue_Assignee;
    }

    public String getIssue_EstimateTime() {
        return issue_EstimateTime;
    }

    public void setIssue_EstimateTime(String issue_EstimateTime) {
        this.issue_EstimateTime = issue_EstimateTime;
    }

    public String getIssue_Creator() {
        return issue_Creator;
    }

    public void setIssue_Creator(String issue_Creator) {
        this.issue_Creator = issue_Creator;
    }

    public String getIssue_project_ID() {
        return issue_project_ID;
    }

    public void setIssue_project_ID(String issue_project_ID) {
        this.issue_project_ID = issue_project_ID;
    }

    public String getIssue_FinishDate() {
        return issue_FinishDate;
    }

    public void setIssue_FinishDate(String issue_FinishDate) {
        this.issue_FinishDate = issue_FinishDate;
    }
}
