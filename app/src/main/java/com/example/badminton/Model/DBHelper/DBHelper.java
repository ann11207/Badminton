package com.example.badminton.Model.DBHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper( Context context) {
        super(context,"QL_BadmintonCourt.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create Table Court(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT NOT NULL UNIQUE, statusCourt TEXT NOT NULL, image BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop Table if exists Court");
        onCreate(db);
    }
}
