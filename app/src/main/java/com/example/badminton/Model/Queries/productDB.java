package com.example.badminton.Model.Queries;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.badminton.Model.DBHelper.DBHelper;
import com.example.badminton.Model.ProductDBModel;

import java.util.ArrayList;
import java.util.List;

public class productDB extends DBHelper {
    public productDB(Context context) {
        super(context);
    }
    public long addProduct(String name, double price, byte[] image, int quantity, int catalog_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("price", price);
        contentValues.put("image", image);
        contentValues.put("quantity", quantity);
        contentValues.put("catalog_id", catalog_id);  // Liên kết sản phẩm với danh mục

        return db.insert("Product", null, contentValues);
    }
    public int updateProduct(int product_id, String name, double price, byte[] image, int quantity, int catalog_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("price", price);
        contentValues.put("image", image);
        contentValues.put("quantity", quantity);
        contentValues.put("catalog_id", catalog_id);

        return db.update("Product", contentValues, "product_id = ?", new String[]{String.valueOf(product_id)});
    }
    public int deleteProduct(int product_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("Product", "product_id = ?", new String[]{String.valueOf(product_id)});
    }

    public List<ProductDBModel> getAllProduct() {
        List<ProductDBModel> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to select all products
        Cursor cursor = db.rawQuery("SELECT * FROM Product", null);

        // Check if cursor has data
        if (cursor.moveToFirst()) {
            do {
                // Retrieve data from the cursor and create a ProductDBModel object
                int product_id = cursor.getInt(cursor.getColumnIndexOrThrow("product_id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));


                // Create a ProductDBModel object
                ProductDBModel product = new ProductDBModel(product_id, name, price, image, quantity);

                // Add product to the list
                productList.add(product);
            } while (cursor.moveToNext());
        }

        // Close cursor and database connection
        cursor.close();
        db.close();

        return productList;
    }


}
