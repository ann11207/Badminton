package com.example.badminton.View.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.badminton.Model.BookingCourtSync;
import com.example.badminton.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder> {

    private List<BookingCourtSync> bookingList;
    private Context context;

    public TimeSlotAdapter(List<BookingCourtSync> bookingList, Context context) {
        this.bookingList = bookingList;
        this.context = context;
    }

    @NonNull
    @Override
    public TimeSlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_booking_court, parent, false);
        return new TimeSlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeSlotViewHolder holder, int position) {
        BookingCourtSync booking = bookingList.get(position);
        String timeRange = booking.getStartTime() + " - " + booking.getEndTime();

        holder.tvTimeSlot.setText(timeRange);
//        holder.tvStatus.setText("Đã đặt sân");
        holder.tvNameCourt.setText(booking.getCourtName());
        holder.tvUser.setText(booking.getUserName());


        String formattedDate = formatDate(booking.getDate());
        holder.tvDate.setText(formattedDate);
        holder.itemView.setOnClickListener(v -> {
            showDeleteConfirmationDialog(booking, position);
        });
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class TimeSlotViewHolder extends RecyclerView.ViewHolder {
        TextView tvTimeSlot, tvStatus, tvNameCourt, tvUser, tvDate;

        public TimeSlotViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameCourt = itemView.findViewById(R.id.tvCourtName);
            tvTimeSlot = itemView.findViewById(R.id.tvTimeSlot);

            tvUser = itemView.findViewById(R.id.tvNameUser);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }

    private String formatDate(String dateString) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date;
        try {
            date = inputFormat.parse(dateString);
            return outputFormat.format(date);
        } catch (ParseException e)
        {
            e.printStackTrace();
            return dateString;
        }
    }
    private void showDeleteConfirmationDialog(BookingCourtSync booking, int position) {
        new AlertDialog.Builder(context)
                .setTitle(" Xác nhận đã nhận sân ")
                .setMessage("")
                .setPositiveButton(" Xác nhận ", (dialog, which) -> {

                    deleteBookingFromFirebase(booking.getIdBooking(), position);
                })
                .setNegativeButton("Khách huỷ", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    private void deleteBookingFromFirebase(String bookingId, int position) {
        DatabaseReference bookingsRef = FirebaseDatabase.getInstance().getReference("bookings");
        bookingsRef.child(bookingId).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                if (position >= 0 && position < bookingList.size()) {
                    bookingList.remove(position);
                    notifyItemRemoved(position);
                } else {

                    Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                }

            } else {
                // Xoá thất bại
                Toast.makeText(context, "Lỗi khi xoá đặt sân", Toast.LENGTH_SHORT).show();
            }
        });
    }




}
