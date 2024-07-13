package com.example.badminton.Controller;

import com.example.badminton.Model.UserAccountModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

public class UserAccountController {
    private FirebaseFirestore firestore;

    public UserAccountController() {
        firestore = FirebaseFirestore.getInstance();
    }

    public void loadUsers(OnSuccessListener<List<UserAccountModel>> successListener, OnFailureListener failureListener) {
        firestore.collection("Users")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<UserAccountModel> users = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        UserAccountModel user = doc.toObject(UserAccountModel.class);
                        users.add(user);
                    }
                    successListener.onSuccess(users);
                })
                .addOnFailureListener(failureListener);
    }

    public void deleteUser(UserAccountModel user, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
        firestore.collection("Users").document(user.getId())
                .delete()
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    public void updateUser(String userId, String nameAccount, String phoneNumber, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
        firestore.collection("Users").document(userId)
                .update("nameAccount", nameAccount, "phoneNumber", phoneNumber)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    public void detailUser(UserAccountModel user, OnSuccessListener<UserAccountModel> successListener, OnFailureListener failureListener){

    }
}