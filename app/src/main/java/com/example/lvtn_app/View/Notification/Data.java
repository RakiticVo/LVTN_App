package com.example.lvtn_app.View.Notification;

public class Data {
    private int id_group;
    private int icon;
    private String body;
    private String title;
    private String sented;

    public Data(int id_group, int icon, String body, String title, String sented) {
        this.id_group = id_group;
        this.icon = icon;
        this.body = body;
        this.title = title;
        this.sented = sented;
    }

    public Data() {
    }

    public int getUserName() {
        return id_group;
    }

    public void setUserName(int id_group) {
        this.id_group = id_group;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
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
