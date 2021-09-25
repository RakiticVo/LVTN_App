package com.example.lvtn_app.Model;

import java.util.ArrayList;

public class Process {
    private int id;
    private String project_name;
    private String name;
    private ArrayList<Task> list;

    public Process(int id, String project_name, String name, ArrayList<Task> list) {
        this.id = id;
        this.project_name = project_name;
        this.name = name;
        this.list = list;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Task> getList() {
        return list;
    }

    public void setList(ArrayList<Task> list) {
        this.list = list;
    }
}
