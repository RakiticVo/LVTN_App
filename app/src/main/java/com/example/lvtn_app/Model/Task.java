package com.example.lvtn_app.Model;

public class Task {
    private int id;
    private String name;
    private String process_type;
    private String decription;
    private String issue_type;
    private String start_date;
    private String priority;
    private String assignee;
    private String estimate_time;
    private String creator;
    private String project;
    private String finishdate;

    public Task(int id, String name, String process_type, String decription, String issue_type, String start_date, String priority, String assignee, String estimate_time, String creator, String project, String finishdate) {
        this.id = id;
        this.name = name;
        this.process_type = process_type;
        this.decription = decription;
        this.issue_type = issue_type;
        this.start_date = start_date;
        this.priority = priority;
        this.assignee = assignee;
        this.estimate_time = estimate_time;
        this.creator = creator;
        this.project = project;
        this.finishdate = finishdate;
    }

    public Task(int id, String name, String start_date) {
        this.id = id;
        this.name = name;
        this.start_date = start_date;
    }

    public Task(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Task(int id, String name, String decription, String issue_type, String start_date, String assignee, String creator) {
        this.id = id;
        this.name = name;
        this.decription = decription;
        this.issue_type = issue_type;
        this.start_date = start_date;
        this.assignee = assignee;
        this.creator = creator;
    }

    public Task(int id, String name, String process_type, String decription, String issue_type, String start_date, String priority, String assignee, String creator) {
        this.id = id;
        this.name = name;
        this.process_type = process_type;
        this.decription = decription;
        this.issue_type = issue_type;
        this.start_date = start_date;
        this.priority = priority;
        this.assignee = assignee;
        this.creator = creator;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProcess_type() {
        return process_type;
    }

    public void setProcess_type(String process_type) {
        this.process_type = process_type;
    }

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public String getIssue_type() {
        return issue_type;
    }

    public void setIssue_type(String issue_type) {
        this.issue_type = issue_type;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getEstimate_time() {
        return estimate_time;
    }

    public void setEstimate_time(String estimate_time) {
        this.estimate_time = estimate_time;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getFinishdate() {
        return finishdate;
    }

    public void setFinishdate(String finishdate) {
        this.finishdate = finishdate;
    }
}
