package com.example.badminton.Model.Queries;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.badminton.Model.DBHelper.DBHelper;
import com.example.badminton.Model.ProductDBModel;

import java.util.ArrayList;
import java.util.List;

public class catalogDB extends DBHelper {
    public catalogDB(Context context) {
        super(context);
    }
    public long addCatalog(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);

        return db.insert("Catalog", null, contentValues);
    }
    public int deleteCatalog(int catalogId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("Catalog", "catalog_id = ?", new String[]{String.valueOf(catalogId)});
    }
    public List<String> getAllCatalogs() {
        List<String> catalogs = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM Catalog", null);
        if (cursor.moveToFirst()) {
            do {
                catalogs.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return catalogs;
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
//    public List<String> getProductsByCatalog(int catalogId) {
//        List<String> products = new ArrayList<>();
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT name, quantity FROM Product WHERE catalog_id = ?", new String[]{String.valueOf(catalogId)});
//
//        if (cursor.moveToFirst()) {
//            do {
//                String productInfo = cursor.getString(0) + " - Quantity: " + cursor.getInt(1);
//                products.add(productInfo);
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        return products;
//    }
    public List<ProductDBModel> getProductsByCatalog(int catalogId) {
        List<ProductDBModel> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM Product WHERE catalog_id = ?", new String[]{String.valueOf(catalogId)});
        if (cursor.moveToFirst()) {
            do {
                int productIdIndex = cursor.getColumnIndex("product_id");
                int nameIndex = cursor.getColumnIndex("name");
                int priceIndex = cursor.getColumnIndex("price");
                int imageIndex = cursor.getColumnIndex("image");
                int quantityIndex = cursor.getColumnIndex("quantity");

                if (productIdIndex != -1 && nameIndex != -1 && priceIndex != -1 && imageIndex != -1 && quantityIndex != -1) {
                    int productId = cursor.getInt(productIdIndex);
                    String name = cursor.getString(nameIndex);
                    double price = cursor.getDouble(priceIndex);
                    byte[] image = cursor.getBlob(imageIndex);
                    int quantity = cursor.getInt(quantityIndex);

                    ProductDBModel product = new ProductDBModel(productId, name, price, image, quantity);
                    productList.add(product);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return productList;
    }
}
