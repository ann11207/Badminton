package com.example.badminton.Controller;

import android.net.Uri;

import com.example.badminton.Model.UserAccountModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserAccountController {
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private FirebaseStorage storage;



    public UserAccountController() {
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
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

    public void loadUserById(String userId, OnSuccessListener<UserAccountModel> successListener, OnFailureListener failureListener) {
        firestore.collection("Users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    UserAccountModel user = documentSnapshot.toObject(UserAccountModel.class);
                    successListener.onSuccess(user);
                })
                .addOnFailureListener(failureListener);
    }

    public void updateUserPassword(String currentPassword, String newPassword, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
        if (currentUser != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail(), currentPassword);

            currentUser.reauthenticate(credential)
                    .addOnSuccessListener(aVoid -> currentUser.updatePassword(newPassword)
                            .addOnSuccessListener(successListener)
                            .addOnFailureListener(failureListener))
                    .addOnFailureListener(failureListener);
        } else {
            failureListener.onFailure(new Exception("Current user is not logged in"));
        }
    }
}
