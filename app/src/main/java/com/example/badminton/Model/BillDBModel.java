package com.example.badminton.Model;

import java.text.NumberFormat;
import java.util.Locale;

public class BillDBModel {
    private int billId;
    private int quantity;
    private String productName;
    private int courtId;
    private int customerId;
    private String customerName;
    private double totalPrice;
    private String date;
    private int playTimeMinutes;
    private int product_id;




        // Constructors
        public BillDBModel() {
        }

        public BillDBModel(int billId, int courtId, int customerId, double totalPrice, String date, int playTimeMinutes, String customerName, int product_id, String productName, int quantity) {
            this.billId = billId;
            this.courtId = courtId;
            this.customerId = customerId;
            this.totalPrice = totalPrice;
            this.date = date;
            this.playTimeMinutes = playTimeMinutes;
            this.customerName = customerName;
            this.product_id = product_id;

            this.quantity = quantity;

            this.productName = productName;

        }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
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

    @Override
    public String toString() {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedTotalPrice = currencyFormat.format(totalPrice);

        return "===== Hoá đơn" + billId +" ====="+ "\n" +
                "Sân: " + courtId + "\n" +
                "Thời gian chơi: " + playTimeMinutes + " phút\n" +
                "Ngày: " + date + "\n" +
                "Tổng tiền: " + formattedTotalPrice;
    }
    }

