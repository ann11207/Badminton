package com.example.badminton.View.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.badminton.Model.BillDBModel;
import com.example.badminton.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.InvoiceViewHolder> {

    private List<BillDBModel> invoiceList;

    public InvoiceAdapter(List<BillDBModel> invoiceList) {
        this.invoiceList = invoiceList;
    }

    @NonNull
    @Override
    public InvoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_invoice, parent, false);
        return new InvoiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InvoiceViewHolder holder, int position) {
        BillDBModel bill = invoiceList.get(position);


        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedTotalPrice = numberFormat.format(bill.getTotalPrice());

//        holder.textViewInvoiceCustomer.setText("Khách hàng: " + bill.getCustomerName());
        holder.textViewInvoiceCourt.setText("Sân: " + bill.getCourtId());
        holder.textViewInvoiceTotalPrice.setText("Tổng giá: " + formattedTotalPrice);
        holder.textViewInvoicePlayTime.setText("Thời gian chơi: " + bill.getPlayTimeMinutes() + " phút");
        holder.textViewInvoiceDate.setText("Ngày: " + bill.getDate());
    }

    @Override
    public int getItemCount() {
        return invoiceList.size();
    }

    static class InvoiceViewHolder extends RecyclerView.ViewHolder {
        TextView textViewInvoiceCustomer;
        TextView textViewInvoiceCourt;
        TextView textViewInvoiceTotalPrice;
        TextView textViewInvoicePlayTime;
        TextView textViewInvoiceDate;

        public InvoiceViewHolder(@NonNull View itemView) {
            super(itemView);
//            textViewInvoiceCustomer = itemView.findViewById(R.id.textViewInvoiceCustomer);
            textViewInvoiceCourt = itemView.findViewById(R.id.textViewInvoiceCourt);
            textViewInvoiceTotalPrice = itemView.findViewById(R.id.textViewInvoiceTotalPrice);
            textViewInvoicePlayTime = itemView.findViewById(R.id.textViewInvoicePlayTime);
            textViewInvoiceDate = itemView.findViewById(R.id.textViewInvoiceDate);
        }
    }
}
