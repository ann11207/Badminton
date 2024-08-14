package com.example.badminton.View.User;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.badminton.Controller.UserAccountController;
import com.example.badminton.Model.UserAccountModel;
import com.example.badminton.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import org.jetbrains.annotations.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class InfomationAccountCustomer extends AppCompatActivity {

    private EditText editTextName, edtTextEmail, edtInfoCustomer_phoneNumber, edtInfoCustomer_oldPassword, edtInfoCustomer_newPassword;
    private static final int PICK_IMAGE_REQUEST = 1;

    private Button btn_updateInfo, btn_updatePass, btn_updateAvatar;
    private ImageView imgAvatar;

    private UserAccountController userAccountController;
    private FirebaseUser currentUser;
    private StorageReference storageReference;

    private CircleImageView showAvatar;
    private Uri imageUri;
    private String myUri = "";
    private FirebaseAuth mAuth;
    private TextView textView_choseAvatar;
    private StorageTask uploadTask;

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

        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference().child("avatar");
        showAvatar = findViewById(R.id.avatar);

        editTextName = findViewById(R.id.edtInfoCustomer_fullName);
        edtTextEmail = findViewById(R.id.edtInfoCustomer_email);
        edtInfoCustomer_phoneNumber = findViewById(R.id.edtInfoCustomer_phoneNumber);
        edtInfoCustomer_oldPassword = findViewById(R.id.edtInfoCustomer_oldPassword);
        edtInfoCustomer_newPassword = findViewById(R.id.edtInfoCustomer_newPassword);

        btn_updateInfo = findViewById(R.id.buttonUpdateInfo);
        btn_updatePass = findViewById(R.id.buttonUpdatePassword);
        btn_updateAvatar = findViewById(R.id.buttonUpdateAvatar);

        btn_updateAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadAvatar();
            }
        });

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

        textView_choseAvatar = findViewById(R.id.text_avatar);
        textView_choseAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), PICK_IMAGE_REQUEST);
            }
        });

        getAvatar();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Glide.with(this)
                    .load(imageUri)
                    .into(showAvatar);
        }
    }


    private void uploadAvatar() {
        if (imageUri != null) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                String uid = user.getUid();

                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Đang tải ảnh...");
                progressDialog.show();

                StorageReference ref = FirebaseStorage.getInstance().getReference()
                        .child("avatars/" + uid + ".jpg");

                ref.putFile(imageUri)
                        .addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(uri -> {
                            String avatarUrl = uri.toString();

                            FirebaseFirestore.getInstance().collection("Users")
                                    .document(uid)
                                    .update("avatar", avatarUrl)  // Cập nhật dòng dữ liệu avatar
                                    .addOnSuccessListener(aVoid -> {
                                        progressDialog.dismiss();
                                        Toast.makeText(this, "Cập nhật avatar thành công", Toast.LENGTH_SHORT).show();
                                        getAvatar(); // Load avatar mới sau khi cập nhật
                                    })
                                    .addOnFailureListener(e -> {
                                        progressDialog.dismiss();
                                        Log.e("FirebaseFirestoreError", e.getMessage(), e);
                                        Toast.makeText(this, "Lỗi khi cập nhật thông tin: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }))
                        .addOnFailureListener(e -> {
                            progressDialog.dismiss();
                            Log.e("FirebaseStorageError", e.getMessage(), e);
                            Toast.makeText(this, "Lỗi khi tải ảnh: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        })
                        .addOnProgressListener(taskSnapshot -> {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Đã tải " + (int) progress + "%");
                        });
            }
        } else {
            Toast.makeText(this, "Vui lòng chọn ảnh trước", Toast.LENGTH_SHORT).show();
        }
    }


    private void updateUserPassword(String currentPassword, String newPassword) {
        userAccountController.updateUserPassword(currentPassword, newPassword,
                aVoid -> Toast.makeText(getApplicationContext(), "Đã cập nhật mật khẩu", Toast.LENGTH_SHORT).show(),
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

    private void getAvatar() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            FirebaseFirestore.getInstance().collection("Users").document(uid)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String avatarUrl = documentSnapshot.getString("avatar");
                            if (avatarUrl != null && !avatarUrl.isEmpty()) {
                                // Sử dụng Glide để tải và hiển thị ảnh
                                Glide.with(this)
                                        .load(avatarUrl)
                                        .placeholder(R.drawable.ic_info1) // Ảnh mặc định
                                        .error(R.drawable.background) // Ảnh hiển thị khi có lỗi
                                        .into(showAvatar);
                            } else {
                                // Nếu không có avatarUrl, hiển thị ảnh mặc định
                                showAvatar.setImageResource(R.drawable.ic_info1);
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Lỗi khi tải avatar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        // Hiển thị ảnh mặc định khi có lỗi
                        showAvatar.setImageResource(R.drawable.background);
                    });
        } else {

            showAvatar.setImageResource(R.drawable.ic_info1);
        }
    }

}
