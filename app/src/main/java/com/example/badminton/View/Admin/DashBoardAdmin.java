package com.example.badminton.View.Admin;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

import java.util.List;

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


}
