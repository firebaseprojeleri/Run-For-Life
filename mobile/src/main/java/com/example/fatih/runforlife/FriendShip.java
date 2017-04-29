package com.example.fatih.runforlife;

/**
 * Created by Fatih on 20.04.2017.
 */

public class FriendShip {
    String userID;
    String myFriendsID;

    public FriendShip(String userID, String myFriendsID) {
        this.userID = userID;
        this.myFriendsID = myFriendsID;
    }

    public FriendShip() {
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getMyFriendsID() {
        return myFriendsID;
    }

    public void setMyFriendsID(String myFriendsID) {
        this.myFriendsID = myFriendsID;
    }
}
