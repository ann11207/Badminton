package com.example.badminton.Controller;

import android.util.Log;

import com.example.badminton.Model.CourtSyncModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CourtManagerController {
    private DatabaseReference databaseReference;

    public CourtManagerController() {
        // Khởi tạo DatabaseReference
        databaseReference = FirebaseDatabase.getInstance().getReference("courts");
    }

    // Thêm dữ liệu sân vào Firebase
    public void addCourt(CourtSyncModel court) {
        String courtId = String.valueOf(court.getId());
        databaseReference.child(courtId).setValue(court)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Firebase", "Court data uploaded successfully");
                    } else {
                        Log.e("Firebase", "Failed to upload court data", task.getException());
                    }
                });
    }
    public void getAllCourts(final CourtDataCallback callback) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<CourtSyncModel> courtList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CourtSyncModel court = snapshot.getValue(CourtSyncModel.class);
                    courtList.add(court);
                }
                callback.onSuccess(courtList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Failed to read data", databaseError.toException());
                callback.onFailure(databaseError.toException());
            }
        });
    }
    // Lắng nghe thay đổi dữ liệu từ Firebase
    public void listenForChanges(OnCourtDataChangedListener listener) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Xử lý dữ liệu thay đổi
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CourtSyncModel court = snapshot.getValue(CourtSyncModel.class);
                    if (court != null) {
                        listener.onCourtDataChanged(court);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Failed to read data", databaseError.toException());
            }
        });
    }

    public void deleteCourt(int courtId) {
        databaseReference.child(String.valueOf(courtId)).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Firebase", "Court data deleted successfully");
                    } else {
                        Log.e("Firebase", "Failed to delete court data", task.getException());
                    }
                });
    }

    public interface OnCourtDataChangedListener {

        void onCourtDataChanged(CourtSyncModel court);
    }
    public interface CourtDataCallback {
        void onSuccess(List<CourtSyncModel> courtList);
        void onFailure(Exception e);
    }
}
