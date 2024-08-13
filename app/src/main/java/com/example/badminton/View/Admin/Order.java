package com.example.badminton.View.Admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.badminton.Controller.PriceCalculator;
import com.example.badminton.Model.BillDBModel;
import com.example.badminton.Model.BookingDBModel;
import com.example.badminton.Model.CourtDBModel;
import com.example.badminton.Model.CustomerDBModel;
import com.example.badminton.Model.Queries.billDB;
import com.example.badminton.Model.Queries.bookingDB;
import com.example.badminton.Model.Queries.courtDB;
import com.example.badminton.Model.Queries.customerDB;
import com.example.badminton.R;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Order extends AppCompatActivity {

    private int courtId;
    private int customerId;
    private double customerPrice;
    private TextView textViewCourtName;
    private TextView textViewCustomerName;
    private TextView textViewTime;
    private Button startButton;
    private Button endButton;

    private courtDB courtDB;
    private customerDB customerDB;
    private bookingDB bookingDB;
    private billDB billDB;

    private long startTime;
    private long elapsedTime = 0;
    private boolean isTimerRunning = false;
    private Handler handler;
    private Runnable timerRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        textViewCourtName = findViewById(R.id.textViewCourtName);
        textViewCustomerName = findViewById(R.id.textViewCustomerName);
        textViewTime = findViewById(R.id.textViewTime);
        startButton = findViewById(R.id.buttonStart);
        endButton = findViewById(R.id.buttonEnd);

        courtDB = new courtDB(this);
        customerDB = new customerDB(this);
        bookingDB = new bookingDB(this);
        billDB = new billDB(this);

        handler = new Handler(Looper.getMainLooper());

        Intent intent = getIntent();
        if (intent != null) {
            courtId = intent.getIntExtra("court_id", -1);
            customerId = intent.getIntExtra("customer_id", -1);
            customerPrice = intent.getDoubleExtra("customer_price", 0.0);
        }

        if (courtId == -1 || customerId == -1) {
            Toast.makeText(this, "Mã sân hoặc mã khách hàng không hợp lệ", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadCourtInfo();
        loadCustomerInfo();

        // Khôi phục thời gian nếu có
        SharedPreferences prefs = getSharedPreferences("OrderPrefs", MODE_PRIVATE);
        startTime = prefs.getLong("startTime", 0);
        elapsedTime = prefs.getLong("elapsedTime", 0);
        if (startTime > 0) {
            startTimer();
        }

        startButton.setOnClickListener(v -> startCourt());
        endButton.setOnClickListener(v -> endCourt());
    }

    @Override
    protected void onPause() {
        super.onPause();
        getSharedPreferences("OrderPrefs", MODE_PRIVATE).edit()
                .putLong("startTime", startTime)
                .putLong("elapsedTime", elapsedTime)
                .apply();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("startTime", startTime);
        outState.putLong("elapsedTime", elapsedTime);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            startTime = savedInstanceState.getLong("startTime");
            elapsedTime = savedInstanceState.getLong("elapsedTime");
            if (startTime > 0) {
                startTimer(); // Khôi phục đếm thời gian nếu có thời gian bắt đầu
            }
        }
    }

    private void loadCourtInfo() {
        CourtDBModel court = courtDB.getCourtById(courtId);
        if (court != null) {
            textViewCourtName.setText("Sân: " + court.getName());
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin sân", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadCustomerInfo() {
        CustomerDBModel customer = customerDB.getCustomerById(customerId);
        if (customer != null) {
            textViewCustomerName.setText("Loại khách hàng: " + customer.getName());
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin khách hàng", Toast.LENGTH_SHORT).show();
        }
    }

    private void startCourt() {
        startTime = System.currentTimeMillis();

        // Tạo đơn đặt sân và lưu thông tin bắt đầu cùng giá tiền
        long bookingId = bookingDB.createBooking(courtId, customerId, startTime, 0, customerPrice);
        if (bookingId != -1) {
            SharedPreferences.Editor editor = getSharedPreferences("OrderPrefs", MODE_PRIVATE).edit();
            editor.putLong("startTime", startTime);
            editor.putLong("bookingId", bookingId);
            editor.apply();

            CourtDBModel court = courtDB.getCourtById(courtId);
            if (court != null) {
                courtDB.updateCourtStatus(courtId, "Hoạt động");
                Toast.makeText(this, "Sân đã được cập nhật trạng thái là 'Hoạt động'", Toast.LENGTH_SHORT).show();
                startTimer();
            } else {
                Toast.makeText(this, "Không tìm thấy sân", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Không thể tạo đơn đặt sân", Toast.LENGTH_SHORT).show();
        }
    }

    private void startTimer() {
        if (isTimerRunning) return;

        isTimerRunning = true;

        timerRunnable = new Runnable() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                long elapsedMillis = currentTime - startTime;
                int elapsedSeconds = (int) (elapsedMillis / 1000) % 60;
                int elapsedMinutes = (int) ((elapsedMillis / (1000 * 60)) % 60);
                int elapsedHours = (int) ((elapsedMillis / (1000 * 60 * 60)) % 24);

                String timeString = String.format(Locale.getDefault(), "%02d:%02d:%02d", elapsedHours, elapsedMinutes, elapsedSeconds);
                textViewTime.setText(timeString);

                handler.postDelayed(this, 1000);
            }
        };

        handler.post(timerRunnable);
    }

    private void stopTimer() {
        if (!isTimerRunning) return;

        isTimerRunning = false;
        handler.removeCallbacks(timerRunnable);
    }

    private void endCourt() {
        long endTime = System.currentTimeMillis();
        long playTimeMillis = endTime - startTime;
        elapsedTime += playTimeMillis;
        long playTimeMinutes = elapsedTime / (60 * 1000);

        stopTimer();

        int startHour = getStartHour(startTime);
        int endHour = getEndHour(endTime);

        double price = PriceCalculator.calculateTotalPrice(customerPrice, playTimeMinutes, startHour, endHour);

        // Tạo hóa đơn
        BillDBModel bill = new BillDBModel();
        bill.setDate(new SimpleDateFormat("dd/MM/yyyy ", Locale.getDefault()).format(new Date()));
        bill.setCourtId(courtId);
        bill.setCustomerId(customerId);
        bill.setTotalPrice(price);

        bill.setPlayTimeMinutes((int) playTimeMinutes);


        courtDB.updateCourtStatus(courtId, "Trống");

        // Hiển thị thông tin hóa đơn và reset thời gian
        showInvoiceDialog(bill);
    }

    private void showInvoiceDialog(BillDBModel bill) {
        // Tạo thông báo hóa đơn
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedPrice = numberFormat.format(bill.getTotalPrice());

        String message = String.format(Locale.getDefault(),
                "Sân: %d\nKhách hàng: %d\nThời gian chơi: %d phút\nTổng giá: %s\nNgày: %s",
                bill.getCourtId(),
                bill.getCustomerId(),
                bill.getPlayTimeMinutes(),
                formattedPrice,
                bill.getDate());

        new AlertDialog.Builder(this)
                .setTitle("Hóa Đơn")
                .setMessage(message)
                .setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Lưu hóa đơn vào cơ sở dữ liệu
                        boolean isBillAdded = billDB.addBill(bill);
                        if (isBillAdded) {
                            Toast.makeText(Order.this, "Hóa đơn đã được lưu", Toast.LENGTH_SHORT).show();
                            resetTimeValues();  // Reset lại các giá trị thời gian sau khi lưu hóa đơn
                        } else {
                            Toast.makeText(Order.this, "Không thể lưu hóa đơn", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                        finish();
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    private void resetTimeValues() {
        startTime = 0;
        elapsedTime = 0;
        isTimerRunning = false;
        textViewTime.setText("00:00:00");

        // Xóa các giá trị lưu trữ trong SharedPreferences
        SharedPreferences.Editor editor = getSharedPreferences("OrderPrefs", MODE_PRIVATE).edit();
        editor.remove("startTime");
        editor.remove("elapsedTime");
        editor.remove("bookingId");
        editor.apply();
    }

    private int getStartHour(long timeInMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    private int getEndHour(long timeInMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }
}
