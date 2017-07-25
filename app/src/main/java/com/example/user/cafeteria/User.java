package com.example.user.cafeteria;

/**
 * Created by USER on 7/24/2017.
 */

public class User {

    private String phoneNumber;
    private String name;
    private String code;
    private String password;
    private String status;

    public User(){}

    public User(String phoneNumber, String name, String code, String password,String status) {
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.code = code;
        this.password = password;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
