package com.example.lvtn_app.Model;

public class NotificationIssueToday {
    private String message;
    private String today;
    private String status;

    public NotificationIssueToday(String message, String today, String status) {
        this.message = message;
        this.today = today;
        this.status = status;
    }

    public NotificationIssueToday() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToday() {
        return today;
    }

    public void setToday(String today) {
        this.today = today;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
