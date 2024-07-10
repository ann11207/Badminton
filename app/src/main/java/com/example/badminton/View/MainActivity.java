package com.example.badminton.View;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.example.badminton.R;
import com.example.badminton.View.Login;

public class MainActivity extends AppCompatActivity {

    private LottieAnimationView lottieSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        lottieSplash = findViewById(R.id.splashAnimation);
        lottieSplash.playAnimation();

        lottieSplash.animate().translationY(-1400).setDuration(1200).setStartDelay(6000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intentLogin = new Intent(getApplicationContext(), Login.class);
                startActivity(intentLogin);
            }
        }, 1000);
    }
}