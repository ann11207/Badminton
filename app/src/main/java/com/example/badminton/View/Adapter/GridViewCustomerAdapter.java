package com.example.badminton.View.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.badminton.Model.CustomerDBModel;
import com.example.badminton.R;
import com.example.badminton.View.Admin.Order;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class GridViewCustomerAdapter extends BaseAdapter {
    private Context context;
    private List<CustomerDBModel> customerList;
    private int courtId;

    public GridViewCustomerAdapter(Context context, List<CustomerDBModel> customerList, int courtId) {
        this.context = context;
        this.customerList = customerList;
        this.courtId = courtId;
    }

    @Override
    public int getCount() {
        return customerList.size();
    }

    @Override
    public Object getItem(int position) {
        return customerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_grid_view_customer, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.imgViewCustomer);
        TextView nameView = convertView.findViewById(R.id.textViewNameCustomer);
        TextView priceView = convertView.findViewById(R.id.textViewPriceCustomer);
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedPrice = numberFormat.format(customerList.get(position).getPrice());
        priceView.setText("Price: " + formattedPrice);
        CustomerDBModel customer = customerList.get(position);
        nameView.setText(customer.getName());
//        priceView.setText("Price: " + customer.getPrice() + " VND");

        if (customer.getImage() != null) {
            imageView.setImageBitmap(convertToBitmap(customer.getImage()));
        } else {
            imageView.setImageResource(R.drawable.ic_info1);
        }

        convertView.setOnClickListener(v -> {
            Intent openListCustomer = new Intent(context, Order.class);
            openListCustomer.putExtra("customer_id", customer.getId());
            openListCustomer.putExtra("court_id", courtId);
            openListCustomer.putExtra("customer_price", customer.getPrice());
            context.startActivity(openListCustomer);
        });

        return convertView;
    }

    private Bitmap convertToBitmap(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
