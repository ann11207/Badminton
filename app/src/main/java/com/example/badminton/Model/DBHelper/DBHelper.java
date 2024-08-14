package com.example.badminton.Model.DBHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "QL_BadmintonCourt.db", null, 8);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL("create Table Court(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL UNIQUE,  " +
                "statusCourt TEXT NOT NULL DEFAULT 'không hoạt động', " +
                "image BLOB )");

        db.execSQL("create Table Customer(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL UNIQUE," +
                " price DOUBLE NOT NULL, " +
                "image BLOB)");

        db.execSQL("create Table Booking (booking_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "court_id INTEGER, " +
                "customer_id INTEGER, " +
                "start_time INTEGER, " +
                "end_time INTEGER," +
                " price DOUBLE,   " +
                "FOREIGN KEY(court_id) REFERENCES Court(id), " +
                "FOREIGN KEY(customer_id) REFERENCES Customer(id))");
        db.execSQL("create Table Bill (bill_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "court_id INTEGER, " +
                "customer_id INTEGER, "+
                "total_price DOUBLE," +
                "date TEXT," +
                " play_time_minutes INTEGER,"+
                "FOREIGN KEY(court_id) REFERENCES Court(id), " +
                "FOREIGN KEY(customer_id) REFERENCES Customer(id))" );
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop Table if exists Court");
        db.execSQL("drop Table if exists Customer");
        db.execSQL("drop Table if exists Booking");
        db.execSQL("drop Table if exists Bill");

        onCreate(db);
    }
}
