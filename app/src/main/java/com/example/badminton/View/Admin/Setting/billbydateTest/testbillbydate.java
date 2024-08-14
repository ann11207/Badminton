package com.example.badminton.View.Admin.Setting.billbydateTest;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.badminton.Model.BillDBModel;
import com.example.badminton.Model.Queries.billDB;
import com.example.badminton.R;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class testbillbydate extends AppCompatActivity {
    private DatePicker datePicker;
    private ListView listViewInvoices;
    private billDB dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_testbillbydate);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        datePicker = findViewById(R.id.datePicker);
        listViewInvoices = findViewById(R.id.listViewInvoices);
        dbHelper = new billDB(this);

        // Thiết lập sự kiện thay đổi ngày cho DatePicker
        datePicker.init(
                datePicker.getYear(),
                datePicker.getMonth(),
                datePicker.getDayOfMonth(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    // Chuyển đổi ngày thành định dạng dd/MM/yyyy
                    String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, monthOfYear + 1, year);
                    loadInvoices(selectedDate);
                }
        );

        // Hiển thị hóa đơn cho ngày hiện tại khi Activity được tạo
        Calendar calendar = Calendar.getInstance();
        String currentDate = String.format(Locale.getDefault(), "%02d/%02d/%d", calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
        loadInvoices(currentDate);
    }

    private void loadInvoices(String date) {
        List<BillDBModel> invoiceList = dbHelper.getBillsByDate(date);

        // Chuyển hóa đơn thành chuỗi hiển thị
        ArrayAdapter<BillDBModel> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, invoiceList);
        listViewInvoices.setAdapter(adapter);
    }
}
