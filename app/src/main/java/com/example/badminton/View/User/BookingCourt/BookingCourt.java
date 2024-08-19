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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BookingCourt extends AppCompatActivity {

    private TimePicker startTimePicker, endTimePicker;
    private Button confirmButton, btnBoooking;
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
//        btnBoooking = findViewById(R.id.BookingButton);

//        btnBoooking.setOnClickListener(v -> {
//            Intent intent = new Intent(BookingCourt.this, ItemBookingCourt.class);
//            startActivity(intent);
//        });
        Button btnShowAvailableTimes = findViewById(R.id.btnShowAvailableTimes);
        btnShowAvailableTimes.setOnClickListener(v -> showAvailableTimeSlots());

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

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, courtNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                courtSpinner.setAdapter(adapter);
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to load courts", Toast.LENGTH_SHORT).show();
            }
        });

        dateTextView.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    BookingCourt.this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    (view, year1, month1, dayOfMonth) -> {
                        calendar.set(year1, month1, dayOfMonth);
                        String selectedDate = String.format("%02d/%02d/%04d", dayOfMonth, month1 + 1, year1);
                        dateTextView.setText(selectedDate);
                    },
                    year, month, day);

            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
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

            if (selectedDate.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Vui lòng chọn ngày", Toast.LENGTH_SHORT).show();
            } else if (dateTextView.getText().toString().isEmpty()){
                Toast.makeText(getApplicationContext(), "Vui lòng chọn ngày", Toast.LENGTH_SHORT).show();
            }

            else if (selectedCourtName.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Vui lòng chọn sân", Toast.LENGTH_SHORT).show();
            } else if (startHour == 0 || endHour == 0) {
                Toast.makeText(getApplicationContext(), "Vui lòng chọn thời gian", Toast.LENGTH_SHORT).show();
            } else if (endHour < startHour || (endHour == startHour && endMinute <= startMinute)) {
                Toast.makeText(getApplicationContext(), "Chọn thời gian phù hợp", Toast.LENGTH_SHORT).show();
            } else {
                String startTime = String.format("%02d:%02d", startHour, startMinute);
                String endTime = String.format("%02d:%02d", endHour, endMinute);

                showConfirmationDialog(selectedCourtName, selectedDate, startTime, endTime);
            }
        });

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

        textViewName.setText("Tên: " + nameAccount);
        tvSelectedCourt.setText("Sân: " + courtName);
        tvSelectedDate.setText("Ngày đặt sân: " + date);
        tvStartTime.setText("Thời gian bắt đầu: " + startTime);
        tvEndTime.setText("Thời gian kết thúc: " + endTime);

        AlertDialog alertDialog = dialogBuilder.create();

        btnConfirmDialog.setOnClickListener(v -> {
            saveBookingToFirebase(courtName, date, startTime, endTime, nameAccount);
            alertDialog.dismiss();
        });

        btnCancelDialog.setOnClickListener(v -> alertDialog.dismiss());

        alertDialog.show();
    }

    private void saveBookingToFirebase(String courtName, String date, String startTime, String endTime, String username) {
        DatabaseReference bookingRef = FirebaseDatabase.getInstance().getReference("bookings");

        // Truy vấn để kiểm tra xem có đặt sân nào cùng sân và ngày
        bookingRef.orderByChild("courtName_date").equalTo(courtName + "_" + date)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean isConflict = false;

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            BookingCourtSync existingBooking = snapshot.getValue(BookingCourtSync.class);

                            // Kiểm tra trùng thời gian
                            if (isTimeConflict(existingBooking.getStartTime(), existingBooking.getEndTime(), startTime, endTime)) {
                                isConflict = true;
                                break;
                            }
                        }

                        if (isConflict) {
                            Toast.makeText(getApplicationContext(), "Thời gian đặt sân bị trùng. Vui lòng chọn thời gian khác.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Nếu không có trùng lặp, lưu thông tin đặt sân vào Firebase
                            saveBooking(courtName, date, startTime, endTime, username);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Đặt sân thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }



//    private void saveBooking(String courtName, String date, String startTime, String endTime, String username) {
//        BookingCourtSync bookingCourtSync = new BookingCourtSync(courtName, date, startTime, endTime, username);
//        DatabaseReference bookingRef = FirebaseDatabase.getInstance().getReference("bookings");
//
//        // Thêm một khóa duy nhất kết hợp tên sân và ngày để dễ dàng truy vấn
//        bookingCourtSync.setCourtName_date(courtName + "_" + date);
//
//        bookingRef.push().setValue(bookingCourtSync)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        Toast.makeText(getApplicationContext(), "Đặt sân thành công", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(getApplicationContext(), "Đặt sân thất bại", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
private void saveBooking(String courtName, String date, String startTime, String endTime, String username) {
    DatabaseReference bookingRef = FirebaseDatabase.getInstance().getReference("bookings");

    // Generate a unique key for the booking
    String bookingId = bookingRef.push().getKey();

    // Create a new booking object with the generated ID
    BookingCourtSync bookingCourtSync = new BookingCourtSync(courtName, date, startTime, endTime, username);
    bookingCourtSync.setIdBooking(bookingId);
    bookingCourtSync.setCourtName_date(courtName + "_" + date);

    // Save the booking to Firebase
    bookingRef.child(bookingId).setValue(bookingCourtSync)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Đặt sân thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Đặt sân thất bại", Toast.LENGTH_SHORT).show();
                }
            });
}


    private boolean isTimeConflict(String existingStartTime, String existingEndTime, String newStartTime, String newEndTime) {
        // Chuyển đổi thời gian từ String sang Date hoặc thời gian phút nếu cần
        int existingStart = convertTimeToMinutes(existingStartTime);
        int existingEnd = convertTimeToMinutes(existingEndTime);
        int newStart = convertTimeToMinutes(newStartTime);
        int newEnd = convertTimeToMinutes(newEndTime);

        // Kiểm tra xem thời gian mới có trùng với thời gian hiện tại không
        return (newStart < existingEnd && newEnd > existingStart);
    }

    private int convertTimeToMinutes(String time) {
        String[] parts = time.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return hours * 60 + minutes;
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
                            Toast.makeText(this, "Không tìm thấy người dùng", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Lỗi tải dữ liệu người dùng: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }
    private void showAvailableTimeSlots() {
        Spinner courtSpinner = findViewById(R.id.courtSpinner);
        TextView dateTextView = findViewById(R.id.dateTextView);
        String selectedDate = dateTextView.getText().toString();
        String selectedCourtName = courtSpinner.getSelectedItem() != null ? courtSpinner.getSelectedItem().toString() : "";

        if (selectedDate.isEmpty() || selectedDate.equals("Chọn ngày")) {
            Toast.makeText(getApplicationContext(), "Vui lòng chọn ngày", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedCourtName.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Vui lòng chọn sân", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference bookingRef = FirebaseDatabase.getInstance().getReference("bookings");
        bookingRef.orderByChild("courtName_date").equalTo(selectedCourtName + "_" + selectedDate)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<TimeSlot> bookedTimeSlots = new ArrayList<>();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            BookingCourtSync existingBooking = snapshot.getValue(BookingCourtSync.class);
                            int start = convertTimeToMinutes(existingBooking.getStartTime());
                            int end = convertTimeToMinutes(existingBooking.getEndTime());
                            bookedTimeSlots.add(new TimeSlot(start, end));
                        }

                        List<TimeSlot> availableTimeSlots = calculateAvailableTimeSlots(bookedTimeSlots);
                        showAvailableTimeSlotsDialog(selectedCourtName, availableTimeSlots);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Lỗi tải thời gian trống", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private List<TimeSlot> calculateAvailableTimeSlots(List<TimeSlot> bookedTimeSlots) {
        List<TimeSlot> availableTimeSlots = new ArrayList<>();
        int startOfDay = 0;
        int endOfDay = 24 * 60;

        if (bookedTimeSlots.isEmpty()) {
            availableTimeSlots.add(new TimeSlot(startOfDay, endOfDay));
            return availableTimeSlots;
        }

        int lastEndTime = startOfDay;
        for (TimeSlot slot : bookedTimeSlots) {
            if (slot.getStart() > lastEndTime) {
                availableTimeSlots.add(new TimeSlot(lastEndTime, slot.getStart()));
            }
            lastEndTime = slot.getEnd();
        }

        if (lastEndTime < endOfDay) {
            availableTimeSlots.add(new TimeSlot(lastEndTime, endOfDay));
        }

        return availableTimeSlots;
    }

    private class TimeSlot {
        private int start;
        private int end;

        public TimeSlot(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }

        @Override
        public String toString() {
            return String.format("%02d:%02d - %02d:%02d", start / 60, start % 60, end / 60, end % 60);
        }
    }



    private void showAvailableTimeSlotsDialog(String courtName, List<TimeSlot> availableTimeSlots) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_dialog_available_times, null);
        dialogBuilder.setView(dialogView);

        TextView tvCourtName = dialogView.findViewById(R.id.tvCourtName);
        TextView tvAvailableTimes = dialogView.findViewById(R.id.tvAvailableTimes);

        // Set the court name
        tvCourtName.setText("Sân: " + courtName);

        StringBuilder times = new StringBuilder();
        for (TimeSlot slot : availableTimeSlots) {
            times.append(slot.toString()).append("\n");
        }
        tvAvailableTimes.setText(times.toString());

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }



}
