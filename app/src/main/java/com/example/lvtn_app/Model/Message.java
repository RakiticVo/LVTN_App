package com.example.lvtn_app.Model;

public class Message {
    private String message_ID;
    private String message_group_ID;
    private String message_sender;
    private String message_img_sender;
    private String message_text;
    private String message_send_time;
    private String message_send_date;
    private String message_send_status;

    public Message(String message_ID, String message_group_ID, String message_sender, String message_img_sender, String message_text, String message_send_time, String message_send_date, String message_send_status) {
        this.message_ID = message_ID;
        this.message_group_ID = message_group_ID;
        this.message_sender = message_sender;
        this.message_img_sender = message_img_sender;
        this.message_text = message_text;
        this.message_send_time = message_send_time;
        this.message_send_date = message_send_date;
        this.message_send_status = message_send_status;
    }

    public Message() {
    }

    public String getMessage_ID() {
        return message_ID;
    }

    public void setMessage_ID(String message_ID) {
        this.message_ID = message_ID;
    }

    public String getMessage_group_ID() {
        return message_group_ID;
    }

    public void setMessage_group_ID(String message_group_ID) {
        this.message_group_ID = message_group_ID;
    }

    public String getMessage_sender() {
        return message_sender;
    }

    public void setMessage_sender(String message_sender) {
        this.message_sender = message_sender;
    }

    public String getMessage_img_sender() {
        return message_img_sender;
    }

    public void setMessage_img_sender(String message_img_sender) {
        this.message_img_sender = message_img_sender;
    }

    public String getMessage_text() {
        return message_text;
    }

    public void setMessage_text(String message_text) {
        this.message_text = message_text;
    }

    public String getMessage_send_time() {
        return message_send_time;
    }

    public void setMessage_send_time(String message_send_time) {
        this.message_send_time = message_send_time;
    }

    public String getMessage_send_date() {
        return message_send_date;
    }

    public void setMessage_send_date(String message_send_date) {
        this.message_send_date = message_send_date;
    }

    public String getMessage_send_status() {
        return message_send_status;
    }

    public void setMessage_send_status(String message_send_status) {
        this.message_send_status = message_send_status;
    }
}
