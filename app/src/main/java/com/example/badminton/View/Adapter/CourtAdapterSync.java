package com.example.badminton.View.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.badminton.Model.CourtSyncModel;
import com.example.badminton.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class CourtAdapterSync extends RecyclerView.Adapter<CourtAdapterSync.CourtViewHolderSync> {

    private List<CourtSyncModel> courtList;

    public CourtAdapterSync(List<CourtSyncModel> courtList) {
        this.courtList = courtList;
    }

    @NonNull
    @Override
    public CourtViewHolderSync onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_booking_court, parent, false);
        return new CourtViewHolderSync(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourtViewHolderSync holder, int position) {
        CourtSyncModel court = courtList.get(position);
        holder.tvCourtName.setText("Court " + court.getName());

        // Lắng nghe thay đổi trạng thái sân từ Firebase
        DatabaseReference courtRef = FirebaseDatabase.getInstance().getReference("courts").child(String.valueOf(court.getId()));
        courtRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String statusCourt = snapshot.child("statusCourt").getValue(String.class);
                if ("Trống".equals(statusCourt)) {
                    holder.itemView.setBackgroundColor(Color.WHITE); // Màu trắng
                    holder.tvStatus.setText("Trống");
                } else {
                    holder.itemView.setBackgroundColor(Color.GREEN); // Màu xanh
                    holder.tvStatus.setText("Đã đặt");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi
            }
        });
    }

    @Override
    public int getItemCount() {
        return courtList.size();
    }

    public static class CourtViewHolderSync extends RecyclerView.ViewHolder {
        TextView tvCourtName, tvStatus;

        public CourtViewHolderSync(@NonNull View itemView) {
            super(itemView);
            tvCourtName = itemView.findViewById(R.id.tvCourtName);
//            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
