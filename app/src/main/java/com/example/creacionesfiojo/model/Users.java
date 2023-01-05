package com.example.creacionesfiojo.model;

public class Users {
    private String Email;
    private String Name;
    private String LastName;
    private String Uid;
    private String UserType;


    public Users() {
    }

    public Users(String email, String name, String lastName, String uid, String userType) {
        Email = email;
        Name = name;
        LastName = lastName;
        Uid = uid;
        UserType = userType;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String userType) {
        UserType = userType;
    }
}
