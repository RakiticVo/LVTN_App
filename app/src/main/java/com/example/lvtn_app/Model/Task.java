package com.example.lvtn_app.Model;

public class Task {
    private String task_ID;
    private String task_Name;
    private String task_Description;
    private String task_StartDate;
    private String task_Creator;
    private String task_Type;

    public Task(String task_ID, String task_Name, String task_Description, String task_StartDate, String task_Creator, String task_Type) {
        this.task_ID = task_ID;
        this.task_Name = task_Name;
        this.task_Description = task_Description;
        this.task_StartDate = task_StartDate;
        this.task_Creator = task_Creator;
        this.task_Type = task_Type;
    }

    public Task() {
    }

    public String getTask_ID() {
        return task_ID;
    }

    public void setTask_ID(String task_ID) {
        this.task_ID = task_ID;
    }

    public String getTask_Name() {
        return task_Name;
    }

    public void setTask_Name(String task_Name) {
        this.task_Name = task_Name;
    }

    public String getTask_Description() {
        return task_Description;
    }

    public void setTask_Description(String task_Description) {
        this.task_Description = task_Description;
    }

    public String getTask_StartDate() {
        return task_StartDate;
    }

    public void setTask_StartDate(String task_StartDate) {
        this.task_StartDate = task_StartDate;
    }

    public String getTask_Creator() {
        return task_Creator;
    }

    public void setTask_Creator(String task_Creator) {
        this.task_Creator = task_Creator;
    }

    public String getTask_Type() {
        return task_Type;
    }

    public void setTask_Type(String task_Type) {
        this.task_Type = task_Type;
    }
}
