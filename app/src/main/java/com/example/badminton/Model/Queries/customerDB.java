package com.example.badminton.Model.Queries;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.badminton.Model.CustomerDBModel;
import com.example.badminton.Model.DBHelper.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class customerDB extends DBHelper {


    public customerDB(Context context) {
        super(context);
    }

    public long insertCustomer(String name, double price, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("price", price);
        values.put("image", image);

        return db.insert("Customer", null, values); // Trả về ID của bản ghi mới
    }



    //    public long insertCustomer(String name, double price, byte[] image) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("name", name);
//        values.put("price", price);
//        values.put("image", image);
//
//        return db.insert("Customer", null, values);
//    }
    public boolean updateCustomer(int id, String name, double price, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("price", price);
        values.put("image", image);

        int rowsAffected = db.update("Customer", values, "id = ?", new String[]{String.valueOf(id)});
        return rowsAffected > 0; // Trả về true nếu có ít nhất một hàng bị ảnh hưởng
    }

    public boolean deleteCustomer(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete("Customer", "id = ?", new String[]{String.valueOf(id)});
        return rowsDeleted > 0; // Trả về true nếu có ít nhất một hàng bị xóa
    }


//    public Cursor getAllCustomers() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        return db.rawQuery("SELECT id, name FROM Customer", null);
//    }

    @SuppressLint("Range")
    public List<CustomerDBModel> getAllCustomers() {
        List<CustomerDBModel> customerList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM Customer", null);
            if (cursor.moveToFirst()) {
                do {
                    CustomerDBModel customer = new CustomerDBModel();
                    customer.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    customer.setName(cursor.getString(cursor.getColumnIndex("name")));
                    customer.setImage(cursor.getBlob(cursor.getColumnIndex("image")));
                    customer.setPrice(cursor.getDouble(cursor.getColumnIndex("price")));
                    customerList.add(customer);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return customerList;
    }
}

