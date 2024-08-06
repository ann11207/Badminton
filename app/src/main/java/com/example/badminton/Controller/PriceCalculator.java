package com.example.badminton.Controller;

public class PriceCalculator {


    private static final double DISCOUNT_AMOUNT = 10000;

    public static double calculateTotalPrice(double basePrice, long playTimeMinutes, int startHour, int endHour) {
        double totalPrice = (basePrice * playTimeMinutes) / 60.0;

        if (isWithinDiscountPeriod(startHour, endHour)) {
            totalPrice -= DISCOUNT_AMOUNT;
        }


        return Math.max(totalPrice, 0);// ko âm
    }

    private static boolean isWithinDiscountPeriod(int startHour, int endHour) {

        return (startHour >= 4 && startHour < 16 && endHour >= 4 && endHour < 16);
    }
}
