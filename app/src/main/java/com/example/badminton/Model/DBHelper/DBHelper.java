package com.example.badminton.Model.DBHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "QL_BadmintonCourt.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create Table Court(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT NOT NULL UNIQUE,  statusCourt TEXT NOT NULL DEFAULT 'không hoạt động', image BLOB)");
        db.execSQL("create Table Customer(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT NOT NULL UNIQUE, price DOUBLE NOT NULL, image BLOB)");

//        String createCourtTable = "CREATE TABLE Court (" +
//                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                "name TEXT NOT NULL UNIQUE, " +
//                "statusCourt TEXT NOT NULL DEFAULT 'không hoạt động', " +
//                "image BLOB)";
//        db.execSQL(createCourtTable);
//        String createCustomerTable = "CREATE TABLE Customer (" +
//                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                "name TEXT NOT NULL UNIQUE, " +
//                "price DOUBLE NOT NULL UNIQUE, " +
//                "image BLOB)";
//        db.execSQL(createCustomerTable);
//        String createBangGiaTable = "CREATE TABLE BangGia (" +
//                "id_gia_thue INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                "id_san INTEGER NOT NULL, " +
//                "id_doi_tuong INTEGER NOT NULL, " +
//                "start_time REAL NOT NULL, " +
//                "end_time REAL NOT NULL, " +
//                "price DOUBLE NOT NULL, " +
//                "FOREIGN KEY(id_san) REFERENCES Court(id), " +
//                "FOREIGN KEY(id_doi_tuong) REFERENCES DoiTuong(id_doi_tuong))";
//        db.execSQL(createBangGiaTable);

//        db.execSQL("create Table Customer(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT NOT NULL UNIQUE, image BLOB)");
//        db.execSQL("create Table PriceCourt(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT NOT NULL UNIQUE, price INTEGER NOT NULL)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop Table if exists Court");
        db.execSQL("drop Table if exists Customer");
//        db.execSQL("drop Table if exists BangGia");
        onCreate(db);
    }
}
//package com.example.badminton.Model.DBHelper;
//
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//public class DBHelper extends SQLiteOpenHelper {
//
//    public DBHelper(Context context) {
//        super(context, "QL_BadmintonCourt.db", null, 1);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        String createCourtTable = "CREATE TABLE Court (" +
//                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                "name TEXT NOT NULL UNIQUE, " +
//                "statusCourt TEXT NOT NULL DEFAULT 'không hoạt động', " +
//                "image BLOB)";
//        db.execSQL(createCourtTable);
//
//        String createCustomerTable = "CREATE TABLE Customer (" +
//                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                "name TEXT NOT NULL UNIQUE, " +
//                "price DOUBLE NOT NULL, " + // Removed UNIQUE constraint for price
//                "image BLOB)";
//        db.execSQL(createCustomerTable);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS Court");
//        db.execSQL("DROP TABLE IF EXISTS Customer");
//        onCreate(db);
//    }
//}
