package com.example.badminton.View.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.badminton.Model.ProductDBModel;
import com.example.badminton.R;

import java.util.HashMap;
import java.util.List;

public class BillProductAdapter extends ArrayAdapter<ProductDBModel> {
    private final Context context;
    private final List<ProductDBModel> productList;
    private final HashMap<Integer, Integer> productQuantities; // Lưu trữ số lượng sản phẩm

    public BillProductAdapter(Context context, List<ProductDBModel> productList, HashMap<Integer, Integer> productQuantities) {
        super(context, 0, productList);
        this.context = context;
        this.productList = productList;
        this.productQuantities = productQuantities;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_bill_product_adapter, parent, false);
        }

        ProductDBModel product = productList.get(position);

        TextView productName = convertView.findViewById(R.id.textViewProductName);
        TextView productQuantity = convertView.findViewById(R.id.textViewProductQuantity);
        TextView productPrice = convertView.findViewById(R.id.textViewProductPrice);

        productName.setText(product.getName());
        productPrice.setText(String.valueOf(product.getPrice()));
        productQuantity.setText(String.valueOf(productQuantities.get(product.getProductId())));

        return convertView;
    }
}
