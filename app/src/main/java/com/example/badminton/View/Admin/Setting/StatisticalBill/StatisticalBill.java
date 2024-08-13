package com.example.badminton.View.Admin.Setting.StatisticalBill;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.badminton.Chart.BarChartRevenue;
import com.example.badminton.Model.BillDBModel;
import com.example.badminton.Model.Queries.billDB;
import com.example.badminton.R;
import com.example.badminton.View.Adapter.InvoiceAdapter;
import com.example.badminton.View.Admin.Setting.billbydateTest.testbillbydate;
import com.github.mikephil.charting.charts.BarChart;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StatisticalBill extends AppCompatActivity {
    private TextView textViewDate;
    private TextView textViewTotalCustomers, textViewTotalRevenue;
    private Button buttonPickDate;
    private Button buttonLoadData;
    private Button buttonLoadAllData;
    private RecyclerView recyclerViewInvoices, recyclerViewInvoicesByDate;
    private InvoiceAdapter invoiceAdapter;


    private List<BillDBModel> invoiceList = new ArrayList<>();
    private billDB billDB;
    private BarChart barChart;
    private Date selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_statistical_bill);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo các thành phần giao diện


        buttonLoadData = findViewById(R.id.buttonLoadData);
        buttonLoadAllData = findViewById(R.id.buttonLoadAllData);
        textViewTotalCustomers = findViewById(R.id.textViewTotalCustomers);
        textViewTotalRevenue = findViewById(R.id.textViewTotalRevenue);
        recyclerViewInvoices = findViewById(R.id.recyclerViewInvoices);

        barChart = findViewById(R.id.barChart);

        billDB = new billDB(this);

        // Thiết lập RecyclerView

        recyclerViewInvoices.setLayoutManager(new LinearLayoutManager(this));


        invoiceAdapter = new InvoiceAdapter(invoiceList);
        recyclerViewInvoices.setAdapter(invoiceAdapter);

        // Cài đặt sự kiện cho các nút

        buttonLoadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openTestBill = new Intent(StatisticalBill.this, testbillbydate.class);
                startActivity(openTestBill);
            }
        });


        buttonLoadAllData.setOnClickListener(v -> loadAllInvoices());


        BarChartRevenue.updateBarChart(invoiceList, barChart);
    }


    private void loadAllInvoices() {
        invoiceList.clear();
        invoiceList.addAll(billDB.getAllBills());
        invoiceAdapter.notifyDataSetChanged();

        updateStatistics();
    }

    private void updateStatistics() {
        int totalCustomers = invoiceList.size();
        double totalRevenue = 0;
        for (BillDBModel bill : invoiceList) {
            totalRevenue += bill.getTotalPrice();
        }
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedTotalRevenue = numberFormat.format(totalRevenue);

        textViewTotalCustomers.setText("Tổng số khách hàng: " + totalCustomers);
        textViewTotalRevenue.setText("Tổng doanh thu: " + formattedTotalRevenue);

        BarChartRevenue.updateBarChart(invoiceList, barChart);
    }
}
