package com.example.lvtn_app.Model;

import android.graphics.Bitmap;

public class Message {
    private int id;
    private int id_groupchat;
    private String sender;
    private String img_sender;
    private String message;
    private String send_time;

    public Message(int id, int id_groupchat, String sender, String img_sender, String message, String send_time) {
        this.id = id;
        this.id_groupchat = id_groupchat;
        this.sender = sender;
        this.img_sender = img_sender;
        this.message = message;
        this.send_time = send_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_groupchat() {
        return id_groupchat;
    }

    public void setId_groupchat(int id_groupchat) {
        this.id_groupchat = id_groupchat;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getImg_sender() {
        return img_sender;
    }

    public void setImg_sender(String img_sender) {
        this.img_sender = img_sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSend_time() {
        return send_time;
    }

    public void setSend_time(String send_time) {
        this.send_time = send_time;
    }
}
