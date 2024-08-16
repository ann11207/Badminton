package com.example.badminton.Model;

public class BookingCourtSync {
    private String courtName;
    private String date;
    private String startTime;
    private String endTime;
    private String userName;
    private String courtName_date;

    public BookingCourtSync() {}

    public String getCourtName_date() {
        return courtName_date;
    }

    public void setCourtName_date(String courtName_date) {
        this.courtName_date = courtName_date;
    }

    public BookingCourtSync(String courtName, String date, String startTime, String endTime, String username) {
        this.courtName = courtName;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.userName = username;
        this.courtName_date = courtName + "_" + date; // Gán giá trị khi tạo object
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
