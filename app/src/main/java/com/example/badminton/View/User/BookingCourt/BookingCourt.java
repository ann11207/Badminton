package com.example.badminton.View.User.BookingCourt;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.badminton.Controller.CourtManagerController;
import com.example.badminton.Model.CourtSyncModel;
import com.example.badminton.R;

import java.util.List;

public class BookingCourt extends AppCompatActivity {

    private TableLayout tableLayout;
    private final int START_HOUR = 0;  // Start time (inclusive)
    private final int END_HOUR = 23;   // End time (inclusive)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_booking_court);

        // Apply insets for edge-to-edge UI
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the TableLayout
        tableLayout = findViewById(R.id.tableLayout);

        // Fetch and display court data
        fetchAndDisplayCourtData();
    }

    private void fetchAndDisplayCourtData() {
        CourtManagerController courtManagerController = new CourtManagerController();
        courtManagerController.getAllCourts(new CourtManagerController.CourtDataCallback() {
            @Override
            public void onSuccess(List<CourtSyncModel> courtList) {
                // Add header row
                TableRow headerRow = new TableRow(BookingCourt.this);
                headerRow.addView(createTextView("Sân"));
                for (int i = START_HOUR; i <= END_HOUR; i++) {
                    headerRow.addView(createTextView(String.format("%02d:00", i)));
                }
                tableLayout.addView(headerRow);

                // Add data rows
                for (CourtSyncModel court : courtList) {
                    TableRow row = new TableRow(BookingCourt.this);

                    // Add court name as the first column
                    TextView courtName = new TextView(BookingCourt.this);
                    courtName.setText(court.getName());
                    courtName.setPadding(10, 10, 10, 10);
                    row.addView(courtName);

                    // Add time slots for each hour from 00:00 to 23:00
                    for (int hour = START_HOUR; hour <= END_HOUR; hour++) {
                        Button timeSlot = new Button(BookingCourt.this);
                        timeSlot.setText(hour + "h");
                        timeSlot.setPadding(10, 10, 10, 10);

                        // Set an OnClickListener for each time slot button
                        timeSlot.setOnClickListener(v -> {
                            // Handle court booking logic here
                        });

                        // Example logic to set color based on court status
                        if (isTimeSlotAvailable(court.getStatusCourt(), hour)) {
                            timeSlot.setBackgroundColor(Color.GREEN); // Available slots in green
                        }

                        row.addView(timeSlot);
                    }

                    // Add the row to the TableLayout
                    tableLayout.addView(row);
                }
            }

            @Override
            public void onFailure(Exception e) {
                // Handle failure (e.g., display an error message)
            }
        });
    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(8, 8, 8, 8);
        return textView;
    }

    private boolean isTimeSlotAvailable(String statusCourt, int hour) {
        // Implement your logic to determine if the slot is available based on court status
        return "available".equals(statusCourt);
    }
}
