package com.example.badminton.View.Admin.Setting.Court;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.badminton.Model.CourtSyncModel;
import com.example.badminton.Controller.CourtManagerController;
import com.example.badminton.Model.CourtDBModel;
import com.example.badminton.Model.Queries.courtDB;
import com.example.badminton.R;
import com.example.badminton.View.Adapter.CourtAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ManageCourt extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CourtAdapter courtAdapter;
    private courtDB courtDatabase;
    private CourtManagerController courtManagerController;

    private FloatingActionButton btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_court);

        recyclerView = findViewById(R.id.rcv_court);
        btnAdd = findViewById(R.id.button_add_court);

        courtDatabase = new courtDB(this);
        courtManagerController = new CourtManagerController();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadCourts();

        btnAdd.setOnClickListener(v -> openAddCourtDialog());

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }

    private void loadCourts() {
        List<CourtDBModel> courtList = courtDatabase.getAllCourts();
        courtAdapter = new CourtAdapter(courtList);
        recyclerView.setAdapter(courtAdapter);

        courtAdapter.setOnItemClickListener(new CourtAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(int position) {
                openEditCourtDialog(courtList.get(position));
            }

            @Override
            public void onDeleteClick(int position) {
                boolean result = courtDatabase.deleteCourt(courtList.get(position).getId());
                if (result) {
                    Toast.makeText(ManageCourt.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    loadCourts();
                } else {
                    Toast.makeText(ManageCourt.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openAddCourtDialog() {
        AddEditCourtDialog dialog = new AddEditCourtDialog(this, null, false);
        dialog.setOnDismissListener(dialogInterface -> loadCourts());
        dialog.show();
    }

    private void openEditCourtDialog(CourtDBModel court) {
        AddEditCourtDialog dialog = new AddEditCourtDialog(this, court, true);
        dialog.setOnDismissListener(dialogInterface -> loadCourts());
        dialog.show();
    }

    // Khi thêm sân mới, cũng đồng bộ dữ liệu với Firebase
    private void addCourtToFirebase(CourtSyncModel court) {
        courtManagerController.addCourt(court);
    }
}
