package com.example.lvtn_app.Model;

public class Joining_Request {
    public String type;
    public String leader_ID;
    public String object_ID;
    public String receiver_ID;
    public String position;
    public String status;
    public String result;

    public Joining_Request(String type, String leader_ID, String object_ID, String receiver_ID, String position, String status, String result) {
        this.type = type;
        this.leader_ID = leader_ID;
        this.object_ID = object_ID;
        this.receiver_ID = receiver_ID;
        this.position = position;
        this.status = status;
        this.result = result;
    }

    public Joining_Request() {
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

    public String getObject_ID() {
        return object_ID;
    }

    public void setObject_ID(String object_ID) {
        this.object_ID = object_ID;
    }

    public String getReceiver_ID() {
        return receiver_ID;
    }

    public void setReceiver_ID(String receiver_ID) {
        this.receiver_ID = receiver_ID;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
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
