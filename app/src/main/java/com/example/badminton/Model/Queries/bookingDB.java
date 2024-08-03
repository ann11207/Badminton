package com.example.badminton.Model.Queries;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.badminton.Model.BookingDBModel;
import com.example.badminton.Model.DBHelper.DBHelper;

public class bookingDB extends DBHelper {
    public bookingDB(Context context) {
        super(context);
    }

    public long createBooking(int courtId, int customerId, long startTime, long endTime, double price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("court_id", courtId);
        values.put("customer_id", customerId);
        values.put("start_time", startTime);
        values.put("end_time", endTime);
        values.put("price", price);
        return db.insert("Booking", null, values);
    }

    public BookingDBModel getLatestBookingByCourtId(int courtId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Booking", null, "court_id = ?", new String[]{String.valueOf(courtId)}, null, null, "booking_id DESC", "1");
        if (cursor != null && cursor.moveToFirst()) {
            BookingDBModel booking = new BookingDBModel();
            int bookingIdIndex = cursor.getColumnIndex("booking_id");
            int courtIdIndex = cursor.getColumnIndex("court_id");
            int customerIdIndex = cursor.getColumnIndex("customer_id");
            int startTimeIndex = cursor.getColumnIndex("start_time");
            int endTimeIndex = cursor.getColumnIndex("end_time");
            int priceIndex = cursor.getColumnIndex("price");

            if (bookingIdIndex != -1) {
                booking.setBookingId(cursor.getInt(bookingIdIndex));
            }
            if (courtIdIndex != -1) {
                booking.setCourtId(cursor.getInt(courtIdIndex));
            }
            if (customerIdIndex != -1) {
                booking.setCustomerId(cursor.getInt(customerIdIndex));
            }
            if (startTimeIndex != -1) {
                booking.setStartTime(cursor.getLong(startTimeIndex));
            }
            if (endTimeIndex != -1) {
                booking.setEndTime(cursor.getLong(endTimeIndex));
            }
            if (priceIndex != -1) {
                booking.setPrice(cursor.getDouble(priceIndex));
            }
            cursor.close();
            return booking;
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    public void updateBooking(BookingDBModel booking) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("start_time", booking.getStartTime());
        values.put("end_time", booking.getEndTime());
        values.put("price", booking.getPrice());
        db.update("Booking", values, "booking_id = ?", new String[]{String.valueOf(booking.getBookingId())});
    }
}
