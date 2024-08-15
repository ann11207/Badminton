package com.example.badminton.View.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.badminton.Model.BookingCourtSync;
import com.example.badminton.R;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder> {

    private List<BookingCourtSync> bookingList;

    public TimeSlotAdapter(List<BookingCourtSync> bookingList) {
        this.bookingList = bookingList;
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
        } catch (ParseException e) {
            e.printStackTrace();
            return dateString;
        }
    }

}
