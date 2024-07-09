package com.example.badminton.View.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.badminton.R;
import com.example.badminton.View.Login;
import com.example.badminton.View.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

public class DashBoardAdmin extends AppCompatActivity {
    private Button btnSetting;
    private Button btnQualityControl;
    private Button btnHistory;
    private Button btnTotalInDay;
    private Button btnLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dash_board_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnHistory = findViewById(R.id.History);
        btnLogout = findViewById(R.id.btn_Logout);
        btnSetting = findViewById(R.id.Setting);

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentOpenSettingDetail = new Intent(DashBoardAdmin.this, Setting.class);
                startActivity(intentOpenSettingDetail);

            }
        });




        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentOpenHistory = new Intent(DashBoardAdmin.this, test.class);
                startActivity(intentOpenHistory);

            }
        });


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getApplicationContext(), " Đăng xuất thành công ", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });
    }
}