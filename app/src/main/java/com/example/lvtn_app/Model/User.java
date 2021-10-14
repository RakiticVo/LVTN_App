package com.example.lvtn_app.Model;

public class User {
    private String user_ID;
    private String user_Name;
    private String user_Email;
    private String user_Pass;
    private String user_Status;
    private String user_Gender;
    private String user_Phone;
    private String user_DOB;
    private String user_Address;
    private String user_Avatar;

    public User(String user_ID, String user_Name, String user_Email, String user_Pass, String user_Status, String user_Gender, String user_Phone, String user_DOB, String user_Address, String user_Avatar) {
        this.user_ID = user_ID;
        this.user_Name = user_Name;
        this.user_Email = user_Email;
        this.user_Pass = user_Pass;
        this.user_Status = user_Status;
        this.user_Gender = user_Gender;
        this.user_Phone = user_Phone;
        this.user_DOB = user_DOB;
        this.user_Address = user_Address;
        this.user_Avatar = user_Avatar;
    }

    public User() {
    }

    public String getUser_ID() {
        return user_ID;
    }

    public void setUser_ID(String user_ID) {
        this.user_ID = user_ID;
    }

    public String getUser_Name() {
        return user_Name;
    }

    public void setUser_Name(String user_Name) {
        this.user_Name = user_Name;
    }

    public String getUser_Email() {
        return user_Email;
    }

    public void setUser_Email(String user_Email) {
        this.user_Email = user_Email;
    }

    public String getUser_Pass() {
        return user_Pass;
    }

    public void setUser_Pass(String user_Pass) {
        this.user_Pass = user_Pass;
    }

    public String getUser_Status() {
        return user_Status;
    }

    public void setUser_Status(String user_Status) {
        this.user_Status = user_Status;
    }

    public String getUser_Gender() {
        return user_Gender;
    }

    public void setUser_Gender(String user_Gender) {
        this.user_Gender = user_Gender;
    }

    public String getUser_Phone() {
        return user_Phone;
    }

    public void setUser_Phone(String user_Phone) {
        this.user_Phone = user_Phone;
    }

    public String getUser_DOB() {
        return user_DOB;
    }

    public void setUser_DOB(String user_DOB) {
        this.user_DOB = user_DOB;
    }

    public String getUser_Address() {
        return user_Address;
    }

    public void setUser_Address(String user_Address) {
        this.user_Address = user_Address;
    }

    public String getUser_Avatar() {
        return user_Avatar;
    }

    public void setUser_Avatar(String user_Avatar) {
        this.user_Avatar = user_Avatar;
    }
}
