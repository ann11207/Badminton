package com.example.badminton.View.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.badminton.Model.CustomerDBModel;
import com.example.badminton.R;
import com.example.badminton.View.Admin.ManageCustomer;
import com.example.badminton.View.Admin.Order;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {
    private Context context;
    private List<CustomerDBModel> customerList;

    public CustomerAdapter(Context context, List<CustomerDBModel> customerList) {
        this.context = context;
        this.customerList = customerList;
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_customer_item, parent, false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        CustomerDBModel customer = customerList.get(position);

        NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        numberFormat.setGroupingUsed(true);
        String formattedPrice = numberFormat.format(customer.getPrice());
        // Set thông tin khách hàng
        holder.nameTextView.setText(customer.getName());
        holder.priceTextView.setText(formattedPrice + " VND");

        // Giải mã ảnh và thiết lập cho ImageView
        if (customer.getImage() != null && customer.getImage().length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(customer.getImage(), 0, customer.getImage().length);
            if (holder.imageView != null) {
                holder.imageView.setImageBitmap(bitmap);
            }
        } else {
            // Xử lý trường hợp ảnh là null hoặc rỗng
            if (holder.imageView != null) {
                holder.imageView.setImageResource(R.drawable.ic_info1);
            }
        }

        // Thiết lập sự kiện click cho các nút
        holder.editButton.setOnClickListener(v -> {
            if (context instanceof ManageCustomer) {
                ((ManageCustomer) context).showEditCustomerDialog(customer);
            }
        });

        holder.deleteButton.setOnClickListener(v -> {
            if (context instanceof ManageCustomer) {
                ((ManageCustomer) context).deleteCustomer(customer.getId());
            }
        });

        holder.itemView.setOnClickListener(v -> {
            Intent openListCustomer = new Intent(context, Order.class);
            openListCustomer.putExtra("customer_id", customer.getId());
            context.startActivity(openListCustomer);
        });
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    public static class CustomerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView, priceTextView;
        ImageButton editButton, deleteButton;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageViewCustomer);
            nameTextView = itemView.findViewById(R.id.textViewCustomer);
            priceTextView = itemView.findViewById(R.id.textViewPrice);
            editButton = itemView.findViewById(R.id.buttonEdit);
            deleteButton = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
