package com.example.badminton.View.Admin;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.badminton.Chart.ChartUtils;
import com.example.badminton.Model.CourtDBModel;
import com.example.badminton.Model.Queries.courtDB;
import com.example.badminton.R;
import com.example.badminton.View.Adapter.CourtAdapter;
import com.example.badminton.View.Adapter.GridViewCourtAdapter;
import com.example.badminton.View.Login;
import com.example.badminton.View.MainActivity;
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
    private Button btnTotalInDay;
    private Button btnLogout;
    private GridViewCourtAdapter gridViewCourtAdapter;
    private GridView gridViewCourt;
    private courtDB courtDatabase;
    private BarChart barChart;
    private TextView textViewName, textViewRole, textViewDate, textViewTime;
    private Handler handler ;
    private Runnable runnable;



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
                handler.postDelayed(this, 1000); // Cập nhật mỗi giây
            }
        };

        textViewName = findViewById(R.id.textView_name);
        textViewRole = findViewById(R.id.textView_role);
        loadUserData();

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String currentDateTime = dateFormat.format(new Date());
        String currentTime = timeFormat.format(new Date());
        textViewDate = findViewById(R.id.textView_date);
        textViewTime = findViewById(R.id.textView_time);
        textViewTime.setText(currentTime);
        textViewDate.setText(currentDateTime);


        barChart = findViewById(R.id.any_chart_view);


        gridViewCourt = findViewById(R.id.gridView_Court);


        courtDatabase = new courtDB(this);


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


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getApplicationContext(), " Đăng xuất thành công ", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });

        setupChart();
        loadCourts();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable); // Dừng Runnable khi Activity bị hủy
        Log.d("DashBoardAdmin", "Activity destroyed, Runnable stopped"); // Debug log
    }

    private void loadCourts() {
        List<CourtDBModel> courtList = courtDatabase.getAllCourts();
        gridViewCourtAdapter = new GridViewCourtAdapter(this, courtList);
        gridViewCourt.setAdapter(gridViewCourtAdapter);
    }

    private void setupChart() {
        Cursor courtsCursor = courtDatabase.getCourts();
        if (courtsCursor != null) {
            try {
                ChartUtils.setupChart(barChart, courtsCursor);
            } finally {
                courtsCursor.close(); // Đảm bảo đóng cursor
            }
        }
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

                            textViewName.setText("Tên: " +nameAccount);
                            textViewRole.setText("Quyền: " +role);
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
