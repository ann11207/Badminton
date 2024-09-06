package com.example.badminton.View.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.badminton.Model.ProductDBModel;
import com.example.badminton.R;

import java.util.List;

public class GridViewProductAdapter extends BaseAdapter {
    private Context context;
    private List<ProductDBModel> products;



    public GridViewProductAdapter(Context context, List<ProductDBModel> products) {
        this.context = context;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.activity_grid_view_product, null);

        }
        ImageView imageProduct = convertView.findViewById(R.id.imageProduct);
        TextView textProductName = convertView.findViewById(R.id.textProductName);
        TextView textProductPrice = convertView.findViewById(R.id.textProductPrice);

        ProductDBModel product = products.get(position);


        textProductName.setText(product.getName());
        textProductPrice.setText(String.valueOf(product.getPrice()));


        byte[] imageByteArray = product.getImage();
        if (imageByteArray != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
            imageProduct.setImageBitmap(bitmap);
        } else {
            imageProduct.setImageResource(R.drawable.products);
        }

        return convertView;
    }
}