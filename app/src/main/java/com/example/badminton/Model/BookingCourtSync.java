package com.example.badminton.Model;

public class BookingCourtSync {
    private String courtName;
    private String date;
    private String startTime;
    private String endTime;
    private String userName;

    public BookingCourtSync() {}

    public BookingCourtSync(String courtName, String date, String startTime, String endTime, String userName) {
        this.courtName = courtName;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.userName = userName;
    }

    public String getCourtName() {
        return courtName;
    }

    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
