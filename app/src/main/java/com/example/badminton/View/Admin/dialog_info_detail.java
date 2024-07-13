package com.example.badminton.View.Admin;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.badminton.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class dialog_info_detail extends AppCompatActivity {

  private   TextView textViewName, textViewEmail, textViewPhone, textViewRole;
   private ImageView avatar;
    private FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dialog_info_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
//        avatar = findViewById(R.id.detailInfo_avatar);
//        textViewEmail =findViewById(R.id.detailInfo_email);
//        textViewName = findViewById(R.id.detailInfo_fullName);
//        textViewPhone = findViewById(R.id.detailInfo_phoneNumber);
//        textViewRole = findViewById(R.id.detailInfot_role);
//
//
//        String userId = getIntent().getStringExtra("userId");
//        if (userId != null ){
//            loadUser(userId);
//        }else {
//            Toast.makeText(this, "Lỗi", Toast.LENGTH_SHORT).show();
//        }
//
//        String name = getIntent().getStringExtra("nameAccount");
//        String email = getIntent().getStringExtra("email");
//        String phoneNumber = getIntent().getStringExtra("phoneNumber");
//        String role = getIntent().getStringExtra("role");
//
//
//        textViewName.setText(name);
//        textViewEmail.setText(email);
//        textViewPhone.setText(phoneNumber);
//        textViewRole.setText(role);
    }
}