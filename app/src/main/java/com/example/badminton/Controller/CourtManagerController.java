package com.example.badminton.Controller;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.badminton.Model.CourtSyncModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CourtManagerController {
    private final DatabaseReference databaseReference;

    public CourtManagerController() {
        // Khởi tạo DatabaseReference để trỏ đến "courts" trong Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("courts");
    }

    // Thêm dữ liệu sân vào Firebase
    public void addCourt(CourtSyncModel court) {
        if (court == null) {
            Log.e("Firebase", "Court data is null, cannot add to Firebase.");
            return;
        }

        String courtId = String.valueOf(court.getId());
        databaseReference.child(courtId).setValue(court)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Firebase", "Court data uploaded successfully.");
                    } else {
                        Log.e("Firebase", "Failed to upload court data.", task.getException());
                    }
                });
    }

    // Lấy tất cả dữ liệu sân từ Firebase
    public void getAllCourts(final CourtDataCallback callback) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<CourtSyncModel> courtList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CourtSyncModel court = snapshot.getValue(CourtSyncModel.class);
                    if (court != null) {
                        courtList.add(court);
                    } else {
                        Log.e("Firebase", "Court data is null in snapshot.");
                    }
                }
                callback.onSuccess(courtList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Failed to read data from Firebase.", databaseError.toException());
                callback.onFailure(databaseError.toException());
            }
        });
    }

    // Lắng nghe thay đổi dữ liệu từ Firebase
    public void listenForChanges(final OnCourtDataChangedListener listener) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CourtSyncModel court = snapshot.getValue(CourtSyncModel.class);
                    if (court != null) {
                        listener.onCourtDataChanged(court);
                    } else {
                        Log.e("Firebase", "Court data is null in snapshot.");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Failed to read data from Firebase.", databaseError.toException());
            }
        });
    }

    // Xóa dữ liệu sân từ Firebase
    public void deleteCourt(int courtId) {
        databaseReference.child(String.valueOf(courtId)).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Firebase", "Court data deleted successfully.");
                    } else {
                        Log.e("Firebase", "Failed to delete court data.", task.getException());
                    }
                });
    }

    // Interface để callback khi dữ liệu sân thay đổi
    public interface OnCourtDataChangedListener {
        void onCourtDataChanged(CourtSyncModel court);
    }

    // Interface để callback khi dữ liệu sân được lấy từ Firebase
    public interface CourtDataCallback {
        void onSuccess(List<CourtSyncModel> courtList);
        void onFailure(Exception e);
    }
}
