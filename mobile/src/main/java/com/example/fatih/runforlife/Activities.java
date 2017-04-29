package com.example.fatih.runforlife;

/**
 * Created by Fatih on 20.04.2017.
 */

public class Activities {
    String userID;
    double startLat;
    double finishLat;
    double startLong;
    double finishLong;
    String totalRoute;
    String totalTime;

    public Activities(String userID, double startLat, double finishLat, double startLong, double finishLong, String totalRoute, String totalTime) {
        this.userID = userID;
        this.startLat = startLat;
        this.finishLat = finishLat;
        this.startLong = startLong;
        this.finishLong = finishLong;
        this.totalRoute = totalRoute;
        this.totalTime = totalTime;

    }

    public Activities() {
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public double getStartLat() {
        return startLat;
    }

    public void setStartLat(double startLat) {
        this.startLat = startLat;
    }

    public double getFinishLat() {
        return finishLat;
    }

    public void setFinishLat(double finishLat) {
        this.finishLat = finishLat;
    }

    public double getStartLong() {
        return startLong;
    }

    public void setStartLong(double startLong) {
        this.startLong = startLong;
    }

    public double getFinishLong() {
        return finishLong;
    }

    public void setFinishLong(double finishLong) {
        this.finishLong = finishLong;
    }

    public String getTotalRoute() {
        return totalRoute;
    }

    public void setTotalRoute(String totalRoute) {
        this.totalRoute = totalRoute;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

}
