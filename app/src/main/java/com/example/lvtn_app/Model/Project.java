package com.example.lvtn_app.Model;

import android.net.Uri;

import java.util.Date;

public class Project {
    private int id_project ;
    private String projectName;
    private String projectDescription;
    private String projectFinishDate;
    private String projectType;
    private String projectDateCreate;
    private String projectLeader;
    private int projectBackground;
    private int id_project_user_list;
    private int id_project_group_chat;

    public Project(int id_project, String projectName, String projectDescription, String projectFinishDate, String projectType, String projectDateCreate, String projectLeader, int projectBackground, int id_project_user_list, int id_project_group_chat) {
        this.id_project = id_project;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.projectFinishDate = projectFinishDate;
        this.projectType = projectType;
        this.projectDateCreate = projectDateCreate;
        this.projectLeader = projectLeader;
        this.projectBackground = projectBackground;
        this.id_project_user_list = id_project_user_list;
        this.id_project_group_chat = id_project_group_chat;
    }

    public Project(int id_project, String projectName, String projectDescription, String projectFinishDate, String projectType, String projectDateCreate, String projectLeader, int projectBackground) {
        this.id_project = id_project;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.projectFinishDate = projectFinishDate;
        this.projectType = projectType;
        this.projectDateCreate = projectDateCreate;
        this.projectLeader = projectLeader;
        this.projectBackground = projectBackground;
    }

    public int getId_project() {
        return id_project;
    }

    public void setId_project(int id_project) {
        this.id_project = id_project;
    }

    public String getProjectName() {
        return projectName.trim();
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription() {
        return projectDescription.trim();
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public String getProjectFinishDate() {
        return projectFinishDate.trim();
    }

    public void setProjectFinishDate(String projectFinishDate) {
        this.projectFinishDate = projectFinishDate;
    }

    public String getProjectType() {
        return projectType.trim();
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getProjectDateCreate() {
        return projectDateCreate.trim();
    }

    public void setProjectDateCreate(String projectDateCreate) {
        this.projectDateCreate = projectDateCreate;
    }

    public String getProjectLeader() {
        return projectLeader.trim();
    }

    public void setProjectLeader(String projectLeader) {
        this.projectLeader = projectLeader;
    }

    public int getProjectBackground() {
        return projectBackground;
    }

    public void setProjectBackground(int projectBackground) {
        this.projectBackground = projectBackground;
    }

    public int getId_project_user_list() {
        return id_project_user_list;
    }

    public void setId_project_user_list(int id_project_user_list) {
        this.id_project_user_list = id_project_user_list;
    }

    public int getId_project_group_chat() {
        return id_project_group_chat;
    }

    public void setId_project_group_chat(int id_project_group_chat) {
        this.id_project_group_chat = id_project_group_chat;
    }
}
