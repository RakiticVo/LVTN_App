package com.example.lvtn_app.Model;

public class Message {
    private int id_Chat;
    private int id_Group;
    private String sender;
    private String img_sender;
    private String message;
    private String send_time;
    private String send_date;
    private String status;

    public Message() {
    }

    public Message(int id_Chat, int id_Group, String sender, String img_sender, String message, String send_time, String send_date, String status) {
        this.id_Chat = id_Chat;
        this.id_Group = id_Group;
        this.sender = sender;
        this.img_sender = img_sender;
        this.message = message;
        this.send_time = send_time;
        this.send_date = send_date;
        this.status = status;
    }

    public int getId_Chat() {
        return id_Chat;
    }

    public void setId_Chat(int id_Chat) {
        this.id_Chat = id_Chat;
    }

    public int getId_Group() {
        return id_Group;
    }

    public void setId_Group(int id_Group) {
        this.id_Group = id_Group;
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

    public String getSend_date() {
        return send_date;
    }

    public void setSend_date(String send_date) {
        this.send_date = send_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
