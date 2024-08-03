package com.example.badminton.Model;

public class BookingDBModel {
    private int bookingId;
    private int courtId;
    private int customerId;
    private long startTime;
    private long endTime;
    private double price;

    // Getter và Setter cho bookingId
    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    // Getter và Setter cho courtId
    public int getCourtId() {
        return courtId;
    }

    public void setCourtId(int courtId) {
        this.courtId = courtId;
    }

    // Getter và Setter cho customerId
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    // Getter và Setter cho startTime
    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    // Getter và Setter cho endTime
    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    // Getter và Setter cho price
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    public BookingDBModel() {
    }
}
