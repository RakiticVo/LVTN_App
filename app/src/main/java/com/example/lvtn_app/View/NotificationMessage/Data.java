package com.example.lvtn_app.View.NotificationMessage;

public class Data {
    private String user; //sender
    private String group_id;
    private String body;
    private String title;
    private String sented; //receiver

    public Data(String user, String group_id, String body, String title, String sented) {
        this.user = user;
        this.body = body;
        this.title = title;
        this.sented = sented;
    }

    public Data() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSented() {
        return sented;
    }

    public void setSented(String sented) {
        this.sented = sented;
    }
}
