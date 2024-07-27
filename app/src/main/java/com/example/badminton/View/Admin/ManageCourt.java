package com.example.badminton.View.Admin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
//    private Button btnAdd;
    private FloatingActionButton btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_court);

        recyclerView = findViewById(R.id.rcv_court);
        btnAdd = findViewById(R.id.button_add_court);
        courtDatabase = new courtDB(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadCourts();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mở dialog để thêm sân mới
                openAddCourtDialog();
            }
        });

        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Quay lại màn hình trước đó
                finish();
            }
        });
    }

    private void loadCourts() {
        List<CourtDBModel> courtList = courtDatabase.getAllCourts();
        courtAdapter = new CourtAdapter(courtList);
        recyclerView.setAdapter(courtAdapter);

        courtAdapter.setOnItemClickListener(new CourtAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(int position) {
                // Mở dialog để chỉnh sửa sân
                openEditCourtDialog(courtList.get(position));
            }

            @Override
            public void onDeleteClick(int position) {
                // Xóa sân
                boolean result = courtDatabase.deleteCourt(courtList.get(position).getId());
                if (result) {
                    Toast.makeText(ManageCourt.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    loadCourts(); // Tải lại danh sách
                } else {
                    Toast.makeText(ManageCourt.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openAddCourtDialog() {
        // Implement the logic to open a dialog for adding a new court
        AddEditCourtDialog dialog = new AddEditCourtDialog(this, null, false);
        dialog.setOnDismissListener(dialogInterface -> loadCourts());
        dialog.show();
    }

    private void openEditCourtDialog(CourtDBModel court) {
        // Implement the logic to open a dialog for editing an existing court
        AddEditCourtDialog dialog = new AddEditCourtDialog(this, court, true);
        dialog.setOnDismissListener(dialogInterface -> loadCourts());
        dialog.show();
    }
}
