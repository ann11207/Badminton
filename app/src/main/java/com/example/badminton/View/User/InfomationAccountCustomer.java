package com.example.badminton.View.User;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.badminton.Controller.UserAccountController;
import com.example.badminton.Model.UserAccountModel;
import com.example.badminton.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class InfomationAccountCustomer extends AppCompatActivity {

    private EditText editTextName, edtTextEmail, edtInfoCustomer_phoneNumber, edtInfoCustomer_oldPassword, edtInfoCustomer_newPassword;

    private Button btn_updateInfo, btn_updatePass, btn_updateAvatar;
    private UserAccountController userAccountController;
    private FirebaseUser currentUser;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_infomation_account_customer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });




        editTextName = findViewById(R.id.edtInfoCustomer_fullName);
        edtTextEmail = findViewById(R.id.edtInfoCustomer_email);
        edtInfoCustomer_phoneNumber = findViewById(R.id.edtInfoCustomer_phoneNumber);
        edtInfoCustomer_oldPassword = findViewById(R.id.edtInfoCustomer_oldPassword);
        edtInfoCustomer_newPassword = findViewById(R.id.edtInfoCustomer_newPassword);


        btn_updateInfo = findViewById(R.id.buttonUpdateInfo);
        btn_updatePass = findViewById(R.id.buttonUpdatePassword);
        btn_updateAvatar = findViewById(R.id.buttonSelectImage);

        userAccountController = new UserAccountController();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            loadUserInfo(currentUser.getUid());
        }



        btn_updateInfo.setOnClickListener(v -> {
            String name = editTextName.getText().toString().trim();
            String phone = edtInfoCustomer_phoneNumber.getText().toString().trim();
            updateUserInfo(currentUser.getUid(), name, phone);
        });

        btn_updatePass.setOnClickListener(v -> {
            String currentPassword = edtInfoCustomer_oldPassword.getText().toString().trim();
            String newPassword = edtInfoCustomer_newPassword.getText().toString().trim();
            updateUserPassword(currentPassword, newPassword);
        });
    }

    private void updateUserPassword(String currentPassword, String newPassword) {
        userAccountController.updateUserPassword(currentPassword, newPassword,
                aVoid -> Toast.makeText(getApplicationContext(), "đã cạp nhật mật khẩu", Toast.LENGTH_SHORT).show(),
                e -> {
                    Log.e("InfomationAccountCustomer", "Lỗi khi cập nhật mật khẩu", e);
                    Toast.makeText(getApplicationContext(), "Sai mật khẩu cũ", Toast.LENGTH_SHORT).show();
                });
    }

    private void loadUserInfo(String userId) {
        userAccountController.loadUserById(userId, new OnSuccessListener<UserAccountModel>() {
            @Override
            public void onSuccess(UserAccountModel user) {
                editTextName.setText(user.getNameAccount());
                edtTextEmail.setText(user.getEmail());
                edtInfoCustomer_phoneNumber.setText(user.getPhoneNumber());
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("InfomationAccountCustomer", "Error loading user info", e);
            }
        });
    }

    private void updateUserInfo(String userId, String name, String phone) {
        userAccountController.updateUser(userId, name, phone, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(InfomationAccountCustomer.this, "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("InfomationAccountCustomer", "Error updating user info", e);
            }
        });
    }


}

