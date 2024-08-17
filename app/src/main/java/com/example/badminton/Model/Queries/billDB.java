package com.example.badminton.Model.Queries;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.badminton.Model.BillDBModel;
import com.example.badminton.Model.DBHelper.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class billDB extends DBHelper {
    public billDB(Context context) {
        super(context);
    }


    public boolean addBill(BillDBModel bill) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("court_id", bill.getCourtId());
        values.put("customer_id", bill.getCustomerId());
        values.put("total_price", bill.getTotalPrice());
        values.put("date", bill.getDate());
        values.put("play_time_minutes", bill.getPlayTimeMinutes());

        long result = db.insert("Bill", null, values);

        if (result == -1) {
            Log.e("billDB", "lỗi bill " + db.getPath());
        }

        db.close();
        return result != -1;
    }



    public List<BillDBModel> getAllBills() {
        List<BillDBModel> billList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Bill", null);

        if (cursor.moveToFirst()) {
            do {
                BillDBModel bill = new BillDBModel();
                bill.setBillId(cursor.getInt(0));
                bill.setCourtId(cursor.getInt(1));
                bill.setCustomerId(cursor.getInt(2));
                bill.setTotalPrice(cursor.getDouble(3));
                bill.setDate(cursor.getString(4));
                bill.setPlayTimeMinutes(cursor.getInt(5));
                billList.add(bill);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return billList;
    }


//    public boolean updateBill(BillDBModel bill) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("court_id", bill.getCourtId());
//        values.put("customer_id", bill.getCustomerId());
//        values.put("total_price", bill.getTotalPrice());
//        values.put("date", bill.getDate());
//        values.put("play_time_minutes", bill.getPlayTimeMinutes());
//
//        int result = db.update("Bill", values, "bill_id = ?", new String[]{String.valueOf(bill.getBillId())});
//        db.close();
//        return result > 0;
//    }


    public boolean deleteBill(int billId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("Bill", "bill_id = ?", new String[]{String.valueOf(billId)});
        db.close();
        return result > 0;
    }
    public List<BillDBModel> getBillsByDate(String date) {
        List<BillDBModel> billList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.rawQuery("SELECT * FROM Bill WHERE date LIKE ?", new String[]{date + "%"});

        if (cursor.moveToFirst()) {
            do {
                BillDBModel bill = new BillDBModel();
                bill.setBillId(cursor.getInt(0));
                bill.setCourtId(cursor.getInt(1));
                bill.setCustomerId(cursor.getInt(2));
                bill.setTotalPrice(cursor.getDouble(3));
                bill.setDate(cursor.getString(4));
                bill.setPlayTimeMinutes(cursor.getInt(5));
                billList.add(bill);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return billList;
    }



}
