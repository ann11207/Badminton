package com.example.badminton.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.badminton.R;
import com.example.badminton.View.Admin.DashBoardAdmin;
import com.example.badminton.View.Staff.DashBoardStaff;
import com.example.badminton.View.User.DashBoardUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {
    private TextView textSignUp;

    private EditText inputLoginEmail, inputLoginPassword;
    private Button btnLogin;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textSignUp = findViewById(R.id.textview_createAccount);
        textSignUp.setOnClickListener(v -> {
            Intent intentOpenSignUp = new Intent(this, SignUp.class);
            startActivity(intentOpenSignUp);
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        TextInputLayout emailLayout = findViewById(R.id.edtLogin_email);
        TextInputLayout passwordLayout = findViewById(R.id.edtLogin_password);
        inputLoginEmail = emailLayout.getEditText();
        inputLoginPassword = passwordLayout.getEditText();

        btnLogin = findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                firebaseAuth.signInWithEmailAndPassword(inputLoginEmail.getText().toString().trim(), inputLoginPassword.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        checkRoleUser(authResult.getUser().getUid());
                        Toast.makeText(Login.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Login", "Đăng nhập thất bại: " + e.getMessage());
                        Toast.makeText(Login.this, "Đăng nhập thất bại " , Toast.LENGTH_SHORT).show();
                    }
                });

                String email = inputLoginEmail.getText().toString().trim();
                String password = inputLoginPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    inputLoginEmail.setError("Email không đuợc để trống");
                    return;

                }
                if (TextUtils.isEmpty(password)) {
                    inputLoginPassword.setError("Mật khẩu  không được để trống");
                    return;
                }
            }

            private void checkRoleUser(String uid) {

                DocumentReference df = firebaseFirestore.collection("Users").document(uid);
                df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

//                        Log.d("TAG", "onSuccess: " + documentSnapshot.getData());
                        String role = documentSnapshot.getString("role");
                        if (role!= null){
                            switch (role.toLowerCase()){
                                case "admin":
                                    Intent intentOpenAdminActivity = new Intent(getApplicationContext(), DashBoardAdmin.class);
                                    startActivity(intentOpenAdminActivity);
                                    finish();
                                    break;
                                case "Staff":
                                    Intent intentOpenStaff = new Intent(getApplicationContext(), DashBoardStaff.class);
                                    startActivity(intentOpenStaff);
                                    finish();
                                    break;
                                case "user":
                                    Intent intentOpenUser = new Intent(getApplicationContext(), DashBoardUser.class);
                                    startActivity(intentOpenUser);
                                    finish();
                                    break;
                            }
                        }

                        if (documentSnapshot.getString("role").equals("admin")) {
                            Intent intentOpenMain = new Intent(getApplicationContext(), DashBoardAdmin.class);
                            startActivity(intentOpenMain);
                            finish();
                        }
                        if (documentSnapshot.getString("role").equals("Staff")) {
                            Intent intentOpenMain = new Intent(getApplicationContext(), DashBoardStaff.class);
                            startActivity(intentOpenMain);
                            finish();
                        }
                        if (documentSnapshot.getString("role").equals("user")) {
                            Intent intentOpenMain = new Intent(getApplicationContext(), DashBoardUser.class);
                            startActivity(intentOpenMain);
                            finish();
                        }
                    }
                });
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DocumentReference df = FirebaseFirestore.getInstance().collection("Users").document(uid);
            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    checkRoleUser(documentSnapshot.getId());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("Login", "Lỗi khi lấy dữ liệu người dùng: " + e.getMessage());
                }
            });
        }
    }

    private void checkRoleUser(String uid) {
        DocumentReference df = firebaseFirestore.collection("Users").document(uid);
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String role = documentSnapshot.getString("role");
                if (role != null) {
                    switch (role.toLowerCase()) {
                        case "admin":
                            startActivity(new Intent(getApplicationContext(), DashBoardAdmin.class));
                            break;
                        case "staff":
                            startActivity(new Intent(getApplicationContext(), DashBoardStaff.class));
                            break;
                        case "user":
                            startActivity(new Intent(getApplicationContext(), DashBoardUser.class));
                            break;
                        default:
                            Log.e("Login", "Vai trò không hợp lệ: " + role);
                            Toast.makeText(Login.this, "Vai trò không hợp lệ", Toast.LENGTH_SHORT).show();
                            return;
                    }
                    finish();
                } else {
                    Log.e("Login", "Không tìm thấy vai trò cho người dùng");
                    Toast.makeText(Login.this, "Không tìm thấy vai trò cho người dùng", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Login", "Lỗi khi lấy dữ liệu người dùng: " + e.getMessage());
                Toast.makeText(Login.this, "Lỗi khi lấy dữ liệu người dùng: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}