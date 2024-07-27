package com.example.badminton.View.Admin;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.badminton.Model.CourtDBModel;
import com.example.badminton.Model.Queries.courtDB;
import com.example.badminton.R;

import java.io.ByteArrayOutputStream;

public class AddEditCourtDialog extends Dialog {
    private courtDB courtDatabase;
    private EditText edtName, edtStatus;
    private ImageView imgCourt;
    private Button btnSave, btnCancel;
    private CourtDBModel court;
    private boolean isEditMode;

    public AddEditCourtDialog(@NonNull Context context, CourtDBModel court, boolean isEditMode) {
        super(context);
        this.courtDatabase = new courtDB(context);
        this.court = court;
        this.isEditMode = isEditMode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_court_dialog);

        edtName = findViewById(R.id.edt_name);
        edtStatus = findViewById(R.id.edt_status);
        imgCourt = findViewById(R.id.img_court);
        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);

        if (isEditMode && court != null) {
            edtName.setText(court.getName());
            edtStatus.setText(court.getStatusCourt());
            imgCourt.setImageBitmap(convertToBitmap(court.getImage()));
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString().trim();
                String status = edtStatus.getText().toString().trim();
                byte[] image = convertToByteArray(((BitmapDrawable) imgCourt.getDrawable()).getBitmap());

                if (name.isEmpty() || status.isEmpty() || image == null) {
                    Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean result;
                if (isEditMode) {
                    result = courtDatabase.updateCourt(court.getId(), name, status, image);
                } else {
                    result = courtDatabase.addCourt(name, status, image);
                }

                if (result) {
                    Toast.makeText(getContext(), "Lưu thành công", Toast.LENGTH_SHORT).show();
                    dismiss();
                } else {
                    Toast.makeText(getContext(), "Lưu thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        imgCourt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement logic to select image
            }
        });
    }

    private Bitmap convertToBitmap(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    private byte[] convertToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}