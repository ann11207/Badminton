package com.example.badminton.View.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.badminton.R;

public class Setting extends AppCompatActivity {

    ImageButton imgbtn_editInfo,imgbtn_manageCourt, imgbtn_back, imgbtn_addCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        imgbtn_back = findViewById(R.id.btn_back);
        imgbtn_editInfo = findViewById(R.id.imgbtn_editInfo);
        imgbtn_manageCourt = findViewById(R.id.imgbtn_manageCourt);
        imgbtn_addCustomer = findViewById(R.id.imgbtn_manageCustomer);
        imgbtn_addCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openListCustomer = new Intent(Setting.this, ManageCustomer.class);
                startActivity(openListCustomer);
                finish();
            }
        });



        imgbtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backSetting = new Intent(Setting.this, DashBoardAdmin.class);
                startActivity(backSetting);
                finish();
            }
        });

        imgbtn_editInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openListAccount = new Intent(Setting.this, ManageUserAdmin.class);
                startActivity(openListAccount);
            }
        });

        imgbtn_manageCourt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openListCourt = new Intent(Setting.this, ManageCourt.class);

                startActivity(openListCourt);
            }
        });
    }
}