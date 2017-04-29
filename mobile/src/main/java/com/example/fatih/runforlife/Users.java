package com.example.fatih.runforlife;

/**
 * Created by Fatih on 20.04.2017.
 */
public class Users {
    String uId;
    String displayName;
    String email;
    String photoUrl;

    public Users() {
    }

    public Users(String uId, String displayName, String email, String photoUrl) {
        this.uId = uId;
        this.displayName = displayName;
        this.email = email;
        this.photoUrl = photoUrl;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}

