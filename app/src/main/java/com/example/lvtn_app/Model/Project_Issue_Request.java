package com.example.lvtn_app.Model;

public class Project_Issue_Request {
    public String issue_ID;
    public String type;
    public String leader_ID;
    public String project_ID;
    public String receiver_ID;
    public String status;
    public String result;

    public Project_Issue_Request(String issue_ID, String type, String leader_ID, String project_ID, String receiver_ID, String status, String result) {
        this.issue_ID = issue_ID;
        this.type = type;
        this.leader_ID = leader_ID;
        this.project_ID = project_ID;
        this.receiver_ID = receiver_ID;
        this.status = status;
        this.result = result;
    }

    public Project_Issue_Request() {
    }

    public String getIssue_ID() {
        return issue_ID;
    }

    public void setIssue_ID(String issue_ID) {
        this.issue_ID = issue_ID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLeader_ID() {
        return leader_ID;
    }

    public void setLeader_ID(String leader_ID) {
        this.leader_ID = leader_ID;
    }

    public String getProject_ID() {
        return project_ID;
    }

    public void setProject_ID(String project_ID) {
        this.project_ID = project_ID;
    }

    public String getReceiver_ID() {
        return receiver_ID;
    }

    public void setReceiver_ID(String receiver_ID) {
        this.receiver_ID = receiver_ID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
