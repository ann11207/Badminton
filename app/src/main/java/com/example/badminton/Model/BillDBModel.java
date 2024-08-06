package com.example.badminton.Model;

public class BillDBModel {
    private int billId;
    private int courtId;
    private int customerId;
    private String customerName;
    private double totalPrice;
    private String date;
    private int playTimeMinutes;



        // Constructors
        public BillDBModel() {
        }

        public BillDBModel(int billId, int courtId, int customerId, double totalPrice, String date, int playTimeMinutes, String customerName) {
            this.billId = billId;
            this.courtId = courtId;
            this.customerId = customerId;
            this.totalPrice = totalPrice;
            this.date = date;
            this.playTimeMinutes = playTimeMinutes;
            this.customerName = customerName;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }


        public int getBillId() {
            return billId;
        }

        public void setBillId(int billId) {
            this.billId = billId;
        }

        public int getCourtId() {
            return courtId;
        }

        public void setCourtId(int courtId) {
            this.courtId = courtId;
        }

        public int getCustomerId() {
            return customerId;
        }

        public void setCustomerId(int customerId) {
            this.customerId = customerId;
        }

        public double getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(double totalPrice) {
            this.totalPrice = totalPrice;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getPlayTimeMinutes() {
            return playTimeMinutes;
        }

        public void setPlayTimeMinutes(int playTimeMinutes) {
            this.playTimeMinutes = playTimeMinutes;
        }
    }

