package com.example.badminton.View.Admin;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.badminton.Controller.UserAccountController;
import com.example.badminton.Model.UserAccountModel;
import com.example.badminton.R;
import com.example.badminton.View.Adapter.UserAccountAdapter;
import com.example.badminton.View.Admin.Setting.Setting;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ManageUserAdmin extends AppCompatActivity {

    private RecyclerView recyclerViewUsers;
    private UserAccountController userController;
    private List<UserAccountModel> userList;
    private UserAccountAdapter userAdapter;
    private ImageButton button_Back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_user_admin);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        button_Back = findViewById(R.id.btn_back);
        button_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBackSetting = new Intent(getApplicationContext(), Setting.class);
                startActivity(intentBackSetting);
                finish();
            }
        });

        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);

        userController = new UserAccountController();
        userList = new ArrayList<>();
        userAdapter = new UserAccountAdapter(userList, new UserAccountAdapter.OnUserClickListener() {
            @Override
            public void onInfoClick(UserAccountModel user) {
                infoUser(user);
            }

            @Override
            public void onEditClick(UserAccountModel user) {
                editUser(user);
            }

            @Override
            public void onDeleteClick(UserAccountModel user) {
                userController.deleteUser(user, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        userList.remove(user);
                        userAdapter.notifyDataSetChanged();
                        Toast.makeText(ManageUserAdmin.this, "Đã xoá tài khoản", Toast.LENGTH_SHORT).show();
                    }
                }, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("ManageUserAdmin", "Error deleting user", e);
                    }
                });
            }
        });


        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewUsers.setAdapter(userAdapter);

        loadUsers();
    }

    private void loadUsers() {
        userController.loadUsers(new OnSuccessListener<List<UserAccountModel>>() {
            @Override
            public void onSuccess(List<UserAccountModel> users) {
                userList.clear();
                userList.addAll(users);
                userAdapter.notifyDataSetChanged();
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("ManageUserAdmin", "Error loading users", e);
            }
        });
    }

    private void editUser(UserAccountModel user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.activity_dialog_edit_user, null);
        EditText editTextName = view.findViewById(R.id.editTextName);
        EditText editTextPhone = view.findViewById(R.id.editTextPhone);
        editTextName.setText(user.getNameAccount());
        editTextPhone.setText(user.getPhoneNumber());

        builder.setView(view)
                .setTitle("Cập nhật thông tin")
                .setPositiveButton("Save", (dialog, which) -> {
                    String nameAccount = editTextName.getText().toString().trim();
                    String phone = editTextPhone.getText().toString().trim();
                    user.setNameAccount(nameAccount);
                    user.setPhoneNumber(phone);
                    userController.updateUser(user.getId(), nameAccount, phone, new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            loadUsers();
                            Toast.makeText(ManageUserAdmin.this, "Đã cập nhật thông tin", Toast.LENGTH_SHORT).show();
                        }
                    }, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("ManageUserAdmin", "Error updating user", e);
                        }
                    });
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void infoUser(UserAccountModel user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.activity_dialog_info_detail, null);
        ImageView avatar = view.findViewById(R.id.detailInfo_avatar);
        TextView textViewName = view.findViewById(R.id.detailInfo_fullName);
        TextView textViewEmail = view.findViewById(R.id.detailInfo_email);
        TextView textViewPhone = view.findViewById(R.id.detailInfo_phoneNumber);
        TextView textViewRole = view.findViewById(R.id.detailInfot_role);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference userRef = firebaseFirestore.collection("Users").document(user.getId());
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String name = documentSnapshot.getString("nameAccount");
                    String email = documentSnapshot.getString("email");
                    String phoneNumber = documentSnapshot.getString("phoneNumber");
                    String role = documentSnapshot.getString("role");

                    textViewName.setText(name);
                    textViewEmail.setText(email);
                    textViewPhone.setText(phoneNumber);
                    textViewRole.setText(role);
                } else {
                    Log.d("ManageUserAdmin", "No such document");
                }
            }
        }).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("ManageUserAdmin", "get failed with ", e);
                    }
                });
        builder.setView(view).setTitle("Thông tin chi tiết").setPositiveButton("OK", null).create().show();

    }
}
