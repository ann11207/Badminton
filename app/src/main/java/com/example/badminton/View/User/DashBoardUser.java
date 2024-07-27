package com.example.badminton.View.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.badminton.R;
import com.example.badminton.View.Login;
import com.google.firebase.auth.FirebaseAuth;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class DashBoardUser extends AppCompatActivity {
    private ImageButton btn_Information, btn_logOut;
    private ImageView dashBoard_avatar;
    private TextView textViewName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dash_board_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn_logOut = findViewById(R.id.dashboard_LogOut);
        btn_logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getApplicationContext(), " Đăng xuất thành công ", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });

        btn_Information = findViewById(R.id.dsbCus_info);
        btn_Information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentOpenSettingDetail = new Intent(getApplicationContext(), InfomationAccountCustomer.class);
                startActivity(intentOpenSettingDetail);
            }
        });

        dashBoard_avatar = findViewById(R.id.dashBoard_avatar);
        textViewName = findViewById(R.id.textViewName);
        loadUserData();
    }



    private void loadUserData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Users").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String avatarUrl = documentSnapshot.getString("avatarUrl");
                            String nameAccount = documentSnapshot.getString("nameAccount");

                            // Load avatar image using Glide
                            Glide.with(this)
                                    .load(avatarUrl)
                                    .placeholder(R.drawable.background) // Placeholder image
                                    .error(R.drawable.ic_info1) // Error image if load fails
                                    .into(dashBoard_avatar);

                            // Hiển thị tên tài khoản
                            textViewName.setText("Xin chào, "+nameAccount);
                        } else {
                            Toast.makeText(this, "Không tìm thấy tài liệu", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Lỗi khi tải dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }

    }
}