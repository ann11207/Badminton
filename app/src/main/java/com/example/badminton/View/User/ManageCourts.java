package com.example.badminton.View.User;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.badminton.Model.BookingCourtSync;
import com.example.badminton.R;
import com.example.badminton.View.Adapter.TimeSlotAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ManageCourts extends AppCompatActivity {

    private RecyclerView recyclerViewTimeSlots;
    private TimeSlotAdapter timeSlotAdapter;
    private List<BookingCourtSync> bookingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_courts);

        recyclerViewTimeSlots = findViewById(R.id.recyclerViewTimeSlots);
        recyclerViewTimeSlots.setLayoutManager(new LinearLayoutManager(this));

        bookingList = new ArrayList<>();
        timeSlotAdapter = new TimeSlotAdapter(bookingList, this);
        recyclerViewTimeSlots.setAdapter(timeSlotAdapter);

        loadTimeSlotsFromFirebase();
    }

    private void loadTimeSlotsFromFirebase() {
        String currentDate = getCurrentDate(); // Get the current date

        DatabaseReference bookingsRef = FirebaseDatabase.getInstance().getReference("bookings");
        bookingsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookingList.clear();
                for (DataSnapshot bookingSnapshot : snapshot.getChildren()) {
                    BookingCourtSync booking = bookingSnapshot.getValue(BookingCourtSync.class);
                    if (booking != null && booking.getDate().equals(currentDate)) {
                        bookingList.add(booking);
                    }
                }
                timeSlotAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error if needed
            }
        });
    }


    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        return dateFormat.format(new Date());
    }

}
