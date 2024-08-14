package com.example.badminton.Controller;

import android.util.Log;

import com.example.badminton.Model.CourtSyncModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CourtListener {
    private DatabaseReference databaseReference;

    public CourtListener() {
        databaseReference = FirebaseDatabase.getInstance().getReference("courts");
    }

    public void listenForChanges() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Xử lý dữ liệu thay đổi
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CourtSyncModel court = snapshot.getValue(CourtSyncModel.class);
                    if (court != null) {
                        Log.d("Firebase", "Court data: " + court.getName() + " - " + court.getStatusCourt());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Failed to read data", databaseError.toException());
            }
        });
    }
}
