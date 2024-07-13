package com.example.badminton.Model;

public class UserAccountModel {


    private String id;
    private String nameAccount;
    private String name;
    private String role;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String phoneNumber;
    private String email;

    public UserAccountModel() {
    }


    public UserAccountModel(String id, String name, String role) {
        this.id = id;
        this.nameAccount = name;
        this.role = role;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameAccount() {
        return nameAccount;
    }


    public void setNameAccount(String nameAccount) {
        this.nameAccount = nameAccount;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


}
