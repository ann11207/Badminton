package com.example.badminton.View.Admin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.badminton.Model.CourtDBModel;
import com.example.badminton.Model.CourtSyncModel;
import com.example.badminton.Model.Queries.courtDB;
import com.example.badminton.R;
import com.example.badminton.View.Adapter.GridViewCourtAdapter;
import com.example.badminton.View.Admin.Setting.Setting;
import com.example.badminton.View.Login;
import com.github.mikephil.charting.charts.BarChart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DashBoardAdmin extends AppCompatActivity {
    private Button btnSetting;
    private Button btnQualityControl;
    private Button btnHistory;
    private Button btnLogout;
    private GridView gridViewCourt;
    private GridViewCourtAdapter gridViewCourtAdapter;
    private courtDB courtDatabase;
    private BarChart barChart;
    private TextView textViewName, textViewRole, textViewDate, textViewTime;
    private Handler handler;
    private Runnable runnable;
    private TableLayout tableLayout;
    private final int START_HOUR = 0;
    private final int END_HOUR = 23;

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

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                updateDateTime();
                updateDashboard();
                handler.postDelayed(this, 1000);
            }
        };

        textViewName = findViewById(R.id.textView_name);
        textViewRole = findViewById(R.id.textView_role);
        textViewDate = findViewById(R.id.textView_date);
        textViewTime = findViewById(R.id.textView_time);

        gridViewCourt = findViewById(R.id.gridView_Court);
        courtDatabase = new courtDB(this);

        btnSetting = findViewById(R.id.Setting);
        btnLogout = findViewById(R.id.btn_Logout);
        tableLayout = findViewById(R.id.tableLayout);

        btnSetting.setOnClickListener(v -> {
            Intent intentOpenSettingDetail = new Intent(DashBoardAdmin.this, Setting.class);
            startActivity(intentOpenSettingDetail);
        });

        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(getApplicationContext(), "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        });

        loadUserData();
        handler.post(runnable);

    }

    private void updateDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

        String currentDateTime = dateFormat.format(new Date());
        String currentTime = timeFormat.format(new Date());

        Log.d("DashBoardAdmin", "Date: " + currentDateTime + " Time: " + currentTime); // Debug log

        textViewDate.setText(currentDateTime);
        textViewTime.setText(currentTime);
    }

    private void updateDashboard() {
        loadCourts();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        Log.d("DashBoardAdmin", "Activity destroyed, Runnable stopped");
    }

    private void loadCourts() {
        List<CourtDBModel> courtList = courtDatabase.getAllCourts();
        gridViewCourtAdapter = new GridViewCourtAdapter(this, courtList);
        gridViewCourt.setAdapter(gridViewCourtAdapter);
    }


    private void loadUserData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Users").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String nameAccount = documentSnapshot.getString("nameAccount");
                            String role = documentSnapshot.getString("role");

                            // Hiển thị tên tài khoản
                            textViewName.setText("Tên: " + nameAccount);
                            textViewRole.setText("Quyền: " + role);
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

