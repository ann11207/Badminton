package com.example.badminton.View.Admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.badminton.Model.BookingDBModel;
import com.example.badminton.Model.CourtDBModel;
import com.example.badminton.Model.CustomerDBModel;
import com.example.badminton.Model.Queries.bookingDB;
import com.example.badminton.Model.Queries.courtDB;
import com.example.badminton.Model.Queries.customerDB;
import com.example.badminton.R;

public class Order extends AppCompatActivity {

    private int courtId;
    private int customerId;
    private TextView textViewCourtName;
    private TextView textViewCustomerName;
    private TextView textViewTime;
    private Button startButton;
    private Button endButton;

    private courtDB courtDB;
    private customerDB customerDB;
    private bookingDB bookingDB;

    private long startTime;
    private long endTime;
    private double price;
    private Handler handler;
    private Runnable timerRunnable;
    private long elapsedTime = 0;
    private boolean isTimerRunning = false;

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

        handler = new Handler(Looper.getMainLooper());

        Intent intent = getIntent();
        if (intent != null) {
            courtId = intent.getIntExtra("court_id", -1);
            customerId = intent.getIntExtra("customer_id", -1);
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
            startTimer(); // Bắt đầu đếm thời gian nếu có thời gian bắt đầu
        }

        startButton.setOnClickListener(v -> startCourt());
        endButton.setOnClickListener(v -> endCourt());
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Lưu thời gian khi Activity bị tạm dừng
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
        SharedPreferences.Editor editor = getSharedPreferences("OrderPrefs", MODE_PRIVATE).edit();
        editor.putLong("startTime", startTime);
        editor.apply();

        long bookingId = bookingDB.createBooking(courtId, customerId, startTime, 0, 0.0);
        if (bookingId != -1) {
            CourtDBModel court = courtDB.getCourtById(courtId);
            if (court != null) {
                courtDB.updateCourtStatus(courtId, "Hoạt động");
                Toast.makeText(this, "Sân đã được cập nhật trạng thái là 'Hoạt động'", Toast.LENGTH_SHORT).show();
                startTimer();
            } else {
                Log.e("OrderActivity", "Court not found with ID: " + courtId);
                Toast.makeText(this, "Không tìm thấy sân với ID: " + courtId, Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e("OrderActivity", "Failed to create booking");
            Toast.makeText(this, "Không thể tạo đơn đặt sân", Toast.LENGTH_SHORT).show();
        }
    }

    private void endCourt() {
        endTime = System.currentTimeMillis();
        bookingDB bookingHelper = new bookingDB(this);
        BookingDBModel booking = bookingHelper.getLatestBookingByCourtId(courtId);
        if (booking != null) {
            booking.setEndTime(endTime);
            price = calculatePrice(booking.getStartTime(), endTime);
            booking.setPrice(price);
            bookingHelper.updateBooking(booking);

            // Update court status to "Trống"
            CourtDBModel court = courtDB.getCourtById(courtId);
            if (court != null) {
                courtDB.updateCourtStatus(courtId, "Trống");
                Toast.makeText(this, "Sân đã được cập nhật trạng thái là 'Trống'", Toast.LENGTH_SHORT).show();
            } else {
                Log.e("OrderActivity", "Court not found with ID: " + courtId);
                Toast.makeText(this, "Không tìm thấy sân với ID: " + courtId, Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e("OrderActivity", "No booking found for court ID: " + courtId);
            Toast.makeText(this, "Không tìm thấy đơn đặt sân cho sân với ID: " + courtId, Toast.LENGTH_SHORT).show();
        }
        stopTimer();
        finish();
    }

    private void startTimer() {
        if (isTimerRunning) return;

        isTimerRunning = true;
        handler.post(timerRunnable = new Runnable() {
            @Override
            public void run() {
                if (isTimerRunning) {
                    elapsedTime = System.currentTimeMillis() - startTime;
                    updateTimeTextView();
                    handler.postDelayed(this, 1000); // Cập nhật mỗi giây
                }
            }
        });
    }

    private void stopTimer() {
        isTimerRunning = false;
        if (timerRunnable != null) {
            handler.removeCallbacks(timerRunnable);
        }
    }

    private void updateTimeTextView() {
        long seconds = (elapsedTime / 1000) % 60;
        long minutes = (elapsedTime / (1000 * 60)) % 60;
        long hours = (elapsedTime / (1000 * 60 * 60)) % 24;

        String timeFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        textViewTime.setText("Thời gian: " + timeFormatted);
    }

    private double calculatePrice(long startTime, long endTime) {
        long duration = endTime - startTime;
        // Ensure duration is positive
        if (duration <= 0) {
            Log.e("OrderActivity", "Invalid duration: " + duration);
            return 0.0;
        }
        return (duration / (1000 * 60 * 60)) * 10.0; // Example rate: 10 per hour
    }
}
