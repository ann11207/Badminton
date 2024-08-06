package com.example.badminton.View.Admin.Setting.StatisticalBill;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.github.mikephil.charting.charts.BarChart;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StatisticalBill extends AppCompatActivity {
    private TextView textViewDate;
    private TextView textViewStatistics, textViewTotalCustomers, textViewTotalRevenue;
    private Button buttonPickDate;
    private Button buttonLoadData; // Button for loading data by date
    private Button buttonLoadAllData; // New button for loading all data
    private RecyclerView recyclerViewInvoices;
    private InvoiceAdapter invoiceAdapter;
    private List<BillDBModel> invoiceList = new ArrayList<>();
    private billDB billDB;
    private BarChart barChart;
    private Date selectedDate; // Store the selected date

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

        textViewDate = findViewById(R.id.textViewDate);
        textViewStatistics = findViewById(R.id.textViewStatistics);
        buttonPickDate = findViewById(R.id.buttonPickDate);
        buttonLoadData = findViewById(R.id.buttonLoadData);
        buttonLoadAllData = findViewById(R.id.buttonLoadAllData); // Initialize the new button
        textViewTotalCustomers = findViewById(R.id.textViewTotalCustomers);
        textViewTotalRevenue = findViewById(R.id.textViewTotalRevenue);
        recyclerViewInvoices = findViewById(R.id.recyclerViewInvoices);
        barChart = findViewById(R.id.barChart);

        billDB = new billDB(this);

        recyclerViewInvoices.setLayoutManager(new LinearLayoutManager(this));
        invoiceAdapter = new InvoiceAdapter(invoiceList);
        recyclerViewInvoices.setAdapter(invoiceAdapter);

        buttonPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        buttonLoadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedDate != null) {
                    loadInvoicesByDate(selectedDate);
                } else {
                    textViewTotalCustomers.setText("Vui lòng chọn ngày trước");
                    textViewTotalRevenue.setText("");
                }
            }
        });

        buttonLoadAllData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAllInvoices();
            }
        });

        // Initialize with the current date
        selectedDate = new Date();
        loadInvoicesByDate(selectedDate);
        BarChartRevenue.updateBarChart(invoiceList, barChart);
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                selectedDate = calendar.getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                textViewDate.setText("Ngày: " + dateFormat.format(selectedDate));
                loadInvoicesByDate(selectedDate);
            }
        },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void loadInvoicesByDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = dateFormat.format(date);
        invoiceList.clear();
        invoiceList.addAll(billDB.getBillsByDate(dateString));
        invoiceAdapter.notifyDataSetChanged();

        updateStatistics();
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
