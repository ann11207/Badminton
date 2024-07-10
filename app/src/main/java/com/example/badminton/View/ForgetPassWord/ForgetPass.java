package com.example.badminton.View.ForgetPassWord;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ForgetPass extends AppCompatActivity {
   private TextView textView_login;
    private EditText edtForgetPass_email;
    private FirebaseAuth firebaseAuth;
    private Button btnGetPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forget_pass);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        textView_login = findViewById(R.id.txtForgetPass_login);
        edtForgetPass_email = findViewById(R.id.edtForgetPass_email);
        btnGetPass = findViewById(R.id.btn_login);
        firebaseAuth = FirebaseAuth.getInstance();

        btnGetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtForgetPass_email.getText().toString().trim();
                if (TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(),"Vui long nhap email",Toast.LENGTH_SHORT).show();
                    return;
                }
                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Đã gửi email",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else {
                        Log.e("ForgetPass", "Lỗi khi gửi email: " + task.getException());
                        Toast.makeText(getApplicationContext(),"Email",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });



        textView_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentOpenLogin = new Intent(getApplicationContext(), Login.class);
                startActivity(intentOpenLogin);
                finish();
            }
        });


    }
}