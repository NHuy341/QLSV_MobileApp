package com.example.quanlysinhvien_gk.model;

public class LoginHistory {
    private String userID;
    private long timestamp;

    public LoginHistory() {}

    public LoginHistory(String userID, long timestamp) {
        this.userID = userID;
        this.timestamp = timestamp;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

