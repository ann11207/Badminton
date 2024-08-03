package com.example.badminton.Model.Queries;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.badminton.Model.CourtDBModel;
import com.example.badminton.Model.DBHelper.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class courtDB extends DBHelper {
    public courtDB(Context context) {
        super(context);
    }

    public boolean addCourt(String name, String statusCourt, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("statusCourt", statusCourt);
        values.put("image", image);
        long result = db.insert("Court", null, values);
        return result != -1;
    }

    public boolean updateCourt(int id, String name, String statusCourt, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("statusCourt", statusCourt);
        values.put("image", image);
        int result = db.update("Court", values, "id=?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public boolean deleteCourt(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("Court", "id=?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public Cursor getCourts() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT id, name FROM Court", null);
    }

    @SuppressLint("Range")
    public List<CourtDBModel> getAllCourts() {
        List<CourtDBModel> courts = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Court", null);
        if (cursor.moveToFirst()) {
            do {
                CourtDBModel court = new CourtDBModel();
                court.setId(cursor.getInt(cursor.getColumnIndex("id")));
                court.setName(cursor.getString(cursor.getColumnIndex("name")));
                court.setStatusCourt(cursor.getString(cursor.getColumnIndex("statusCourt")));
                court.setImage(cursor.getBlob(cursor.getColumnIndex("image")));
                courts.add(court);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return courts;
    }

    public void updateCourtStatus(int courtId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("statusCourt", status);
        db.update("Court", contentValues, "id = ?", new String[]{String.valueOf(courtId)});
    }

    @SuppressLint("Range")
    public CourtDBModel getCourtById(int courtId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        CourtDBModel court = null;

        try {
            cursor = db.query("Court", null, "id = ?", new String[]{String.valueOf(courtId)}, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                court = new CourtDBModel();
                court.setId(cursor.getInt(cursor.getColumnIndex("id")));
                court.setName(cursor.getString(cursor.getColumnIndex("name")));
                court.setStatusCourt(cursor.getString(cursor.getColumnIndex("statusCourt")));
                court.setImage(cursor.getBlob(cursor.getColumnIndex("image")));
                Log.d("courtDB", "Court found: " + court.getName());
            } else {
                Log.e("courtDB", "No court found with ID: " + courtId);
            }
        } catch (Exception e) {
            Log.e("courtDB", "Error getting court by ID", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return court;
    }
}
