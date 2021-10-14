package com.example.lvtn_app.Model;

import android.net.Uri;

import java.util.Date;

public class Project {
    private String project_ID;
    private String project_Name;
    private String project_Description;
    private String project_FinishDate;
    private String project_Type;
    private String project_DateCreate;
    private String project_Leader;
    private String project_Background;


    public Project(String project_ID, String project_Name, String project_Description, String project_FinishDate, String project_Type, String project_DateCreate, String project_Leader, String project_Background) {
        this.project_ID = project_ID;
        this.project_Name = project_Name;
        this.project_Description = project_Description;
        this.project_FinishDate = project_FinishDate;
        this.project_Type = project_Type;
        this.project_DateCreate = project_DateCreate;
        this.project_Leader = project_Leader;
        this.project_Background = project_Background;
    }

    public Project() {
    }

    public String getProject_ID() {
        return project_ID;
    }

    public void setProject_ID(String project_ID) {
        this.project_ID = project_ID;
    }

    public String getProject_Name() {
        return project_Name;
    }

    public void setProject_Name(String project_Name) {
        this.project_Name = project_Name;
    }

    public String getProject_Description() {
        return project_Description;
    }

    public void setProject_Description(String project_Description) {
        this.project_Description = project_Description;
    }

    public String getProject_FinishDate() {
        return project_FinishDate;
    }

    public void setProject_FinishDate(String project_FinishDate) {
        this.project_FinishDate = project_FinishDate;
    }

    public String getProject_Type() {
        return project_Type;
    }

    public void setProject_Type(String project_Type) {
        this.project_Type = project_Type;
    }

    public String getProject_DateCreate() {
        return project_DateCreate;
    }

    public void setProject_DateCreate(String project_DateCreate) {
        this.project_DateCreate = project_DateCreate;
    }

    public String getProject_Leader() {
        return project_Leader;
    }

    public void setProject_Leader(String project_Leader) {
        this.project_Leader = project_Leader;
    }

    public String getProject_Background() {
        return project_Background;
    }

    public void setProject_Background(String project_Background) {
        this.project_Background = project_Background;
    }
}
