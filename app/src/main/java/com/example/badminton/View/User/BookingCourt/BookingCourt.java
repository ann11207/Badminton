package com.example.badminton.View.User.BookingCourt;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.badminton.Controller.CourtManagerController;
import com.example.badminton.Model.BookingCourtSync;
import com.example.badminton.Model.CourtSyncModel;
import com.example.badminton.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BookingCourt extends AppCompatActivity {


    private TimePicker startTimePicker, endTimePicker; // Define TimePicker variables
    private Button confirmButton;
    private TextView textViewName;
    private String nameAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_booking_court);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Spinner courtSpinner = findViewById(R.id.courtSpinner);
        TextView dateTextView = findViewById(R.id.dateTextView);
        Calendar calendar = Calendar.getInstance();
        confirmButton = findViewById(R.id.confirmButton);
        textViewName = findViewById(R.id.userName);


        startTimePicker = findViewById(R.id.startTimePicker);
        endTimePicker = findViewById(R.id.endTimePicker);

        CourtManagerController courtManagerController = new CourtManagerController();


        courtManagerController.getAllCourts(new CourtManagerController.CourtDataCallback() {
            @Override
            public void onSuccess(List<CourtSyncModel> courtList) {

                List<String> courtNames = new ArrayList<>();
                for (CourtSyncModel court : courtList) {
                    courtNames.add(court.getName());
                }

                // Create an ArrayAdapter to populate the Spinner
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, courtNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                courtSpinner.setAdapter(adapter);
            }

            @Override
            public void onFailure(Exception e) {
                // Handle failure
                Toast.makeText(getApplicationContext(), "Failed to load courts", Toast.LENGTH_SHORT).show();
            }
        });

        // Date selection
        dateTextView.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    BookingCourt.this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    (view, year1, month1, dayOfMonth) -> {
                        calendar.set(year1, month1, dayOfMonth);
                        String selectedDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(calendar.getTime());
                        dateTextView.setText(selectedDate);
                    },
                    year, month, day);

            datePickerDialog.getDatePicker().setCalendarViewShown(false);
            datePickerDialog.show();
        });



        confirmButton.setOnClickListener(v -> {
            String selectedDate = dateTextView.getText().toString();
            String selectedCourtName = courtSpinner.getSelectedItem() != null ? courtSpinner.getSelectedItem().toString() : "";
            int startHour = startTimePicker.getHour();
            int startMinute = startTimePicker.getMinute();
            int endHour = endTimePicker.getHour();
            int endMinute = endTimePicker.getMinute();


            if (selectedDate.isEmpty() || selectedCourtName.isEmpty() || startHour == 0 || endHour == 0) {
                Toast.makeText(getApplicationContext(), "Vui lòng chọn đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else if (endHour < startHour || (endHour == startHour && endMinute <= startMinute)) {
                Toast.makeText(getApplicationContext(), "Chọn thời gian phù hợp ", Toast.LENGTH_SHORT).show();
            } else {
                String startTime = String.format("%02d:%02d", startHour, startMinute);
                String endTime = String.format("%02d:%02d", endHour, endMinute);


                showConfirmationDialog(selectedCourtName, selectedDate, startTime, endTime);
            }
        });


        // Load user data
        loadUserData();
    }

    private void showConfirmationDialog(String courtName, String date, String startTime, String endTime) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_dialog_confirmation, null);
        dialogBuilder.setView(dialogView);

        TextView textViewName = dialogView.findViewById(R.id.userName);
        TextView tvSelectedCourt = dialogView.findViewById(R.id.tvSelectedCourt);
        TextView tvSelectedDate = dialogView.findViewById(R.id.tvSelectedDate);
        TextView tvStartTime = dialogView.findViewById(R.id.tvStartTime);
        TextView tvEndTime = dialogView.findViewById(R.id.tvEndTime);
        Button btnConfirmDialog = dialogView.findViewById(R.id.btnConfirmDialog);
        Button btnCancelDialog = dialogView.findViewById(R.id.btnCancelDialog);

        // Set the text for the TextViews
        textViewName.setText("Tên: "+nameAccount);
        tvSelectedCourt.setText("Court: " + courtName);
        tvSelectedDate.setText("Date: " + date);
        tvStartTime.setText("Start Time: " + startTime);
        tvEndTime.setText("End Time: " + endTime);


        AlertDialog alertDialog = dialogBuilder.create();


        btnConfirmDialog.setOnClickListener(v -> {

            saveBookingToFirebase(courtName, date, startTime, endTime, nameAccount);
            alertDialog.dismiss();
        });


        btnCancelDialog.setOnClickListener(v -> alertDialog.dismiss());

        alertDialog.show();
    }

    private void saveBookingToFirebase(String courtName, String date, String startTime, String endTime, String username) {

        BookingCourtSync bookingCourtSync = new BookingCourtSync(courtName, date, startTime, endTime, username);

        DatabaseReference bookingRef = FirebaseDatabase.getInstance().getReference("bookings");


        bookingRef.push().setValue(bookingCourtSync)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Đặt sân thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Đặt sân thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadUserData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Users").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            nameAccount = documentSnapshot.getString("nameAccount");
                            textViewName.setText(nameAccount);
                        } else {
                            Toast.makeText(this, "Không tiìm thấy uer", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Error loading user data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }
}