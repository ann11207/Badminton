package com.example.badminton.View;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.badminton.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    private EditText inputNameAccount, inputPassword, inputPhoneNumber, inputEmail;
    private Button btnSignUp, btnBack;
    private RadioButton radioAdmin, radioStaff, radioCustomer;
    private RadioGroup radioGroupPermissions;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        inputNameAccount = findViewById(R.id.edtSignUp_nameAccount);

        inputEmail = findViewById(R.id.edtSignUp_email);
        inputPassword = findViewById(R.id.edtSignUp_password);
        inputPhoneNumber = findViewById(R.id.edtSignUp_phoneNumber);
        btnSignUp = findViewById(R.id.btnSignUp_signUp);
        btnBack = findViewById(R.id.btnSignUp_back);
        radioAdmin = findViewById(R.id.radioButtonSignUp_admin);
        radioStaff = findViewById(R.id.radioButtonSignUp_staff);
        radioCustomer = findViewById(R.id.radioButtonSignUp_customer);
        radioGroupPermissions = findViewById(R.id.radioGroupPermissions);

        btnSignUp.setOnClickListener(v -> {
            // Lấy thông tin người dùng từ các EditText
            String nameAccount = inputNameAccount.getText().toString().trim();
            String avatarUrl = "";
            String email = inputEmail.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();
            String phoneNumber = inputPhoneNumber.getText().toString().trim();

            // Kiểm tra định thông tin hợp lệ
            if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Log.e("SignUp", "Địa chỉ email không hợp lệ: " + email);
                Toast.makeText(SignUp.this, "Vui lòng nhập địa chỉ email hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(password) || password.length() < 6) {
                Toast.makeText(SignUp.this, "Vui lòng nhập mật khẩu có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(phoneNumber)) {
                Toast.makeText(SignUp.this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
                return;
            }
            int roleSelect = radioGroupPermissions.getCheckedRadioButtonId();
            if (roleSelect == -1) {
                Toast.makeText(SignUp.this, "Vui lòng chọn vai trò", Toast.LENGTH_SHORT).show();
                return;
            }
            String salt = BCrypt.gensalt();
            String hashedPassword = BCrypt.hashpw(password, salt);

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        String uid = user.getUid();
                        Toast.makeText(SignUp.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                        DocumentReference df = firebaseFirestore.collection("Users").document(uid);
                        Map<String, Object> userInfo = new HashMap<>();
                        userInfo.put("id", uid);
                        userInfo.put("nameAccount", nameAccount);
                        userInfo.put("email", email);
                        userInfo.put("password", hashedPassword);
                        userInfo.put("phoneNumber", phoneNumber);
                        userInfo.put("avatarUrl", avatarUrl);
                        String role = "";
                        if (roleSelect == radioAdmin.getId()) {
                            role = "admin";
                        } else if (roleSelect == radioStaff.getId()) {
                            role = "staff";
                        } else if (roleSelect == radioCustomer.getId()) {
                            role = "user";
                        }
                        userInfo.put("role", role);
                        df.set(userInfo);

                        startActivity(new Intent(getApplicationContext(), Login.class));
                        finish();
                    } else {
                        Toast.makeText(SignUp.this, "User creation failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(e -> {
                Log.e("SignUp", "Error: " + e.getMessage());
                Toast.makeText(SignUp.this, "Không tạo được tài khoản: " + e.getMessage(), Toast.LENGTH_LONG).show();
            });
        });

        btnBack.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Login.class)));
    }
}
