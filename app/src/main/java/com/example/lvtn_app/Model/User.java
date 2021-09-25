package com.example.lvtn_app.Model;

public class User {
    private int id_user;
    private String userName;
    private String userEmail;
    private String userPass;
    private boolean status;
    private String gender_PI;
    private String phone_PI;
    private String dob_PI;
    private String address_PI;
    private String avatar_PI;

    public User(int id_user, String userName, String userEmail, String userPass, boolean status, String gender_PI, String phone_PI, String dob_PI, String address_PI, String avatar_PI) {
        this.id_user = id_user;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPass = userPass;
        this.status = status;
        this.gender_PI = gender_PI;
        this.phone_PI = phone_PI;
        this.dob_PI = dob_PI;
        this.address_PI = address_PI;
        this.avatar_PI = avatar_PI;
    }

    public User(String userName, String gender_PI, String phone_PI, String dob_PI, String address_PI, String avatar_PI) {
        this.userName = userName;
        this.gender_PI = gender_PI;
        this.phone_PI = phone_PI;
        this.dob_PI = dob_PI;
        this.address_PI = address_PI;
        this.avatar_PI = avatar_PI;
    }

    public User(int id_user, String userName, String userEmail, String userPass, boolean status) {
        this.id_user = id_user;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPass = userPass;
        this.status = status;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getGender_PI() {
        return gender_PI;
    }

    public void setGender_PI(String gender_PI) {
        this.gender_PI = gender_PI;
    }

    public String getPhone_PI() {
        return phone_PI;
    }

    public void setPhone_PI(String phone_PI) {
        this.phone_PI = phone_PI;
    }

    public String getDob_PI() {
        return dob_PI;
    }

    public void setDob_PI(String dob_PI) {
        this.dob_PI = dob_PI;
    }

    public String getAddress_PI() {
        return address_PI;
    }

    public void setAddress_PI(String address_PI) {
        this.address_PI = address_PI;
    }

    public String getAvatar_PI() {
        return avatar_PI;
    }

    public void setAvatar_PI(String avatar_PI) {
        this.avatar_PI = avatar_PI;
    }
}
