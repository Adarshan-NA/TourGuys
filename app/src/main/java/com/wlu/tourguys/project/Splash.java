package com.wlu.tourguys.project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Splash extends AppCompatActivity {
    private static final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.splash_screen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Delay for 2 seconds, then start OnboardingActivity with a slide animation
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(Splash.this, onBoarding.class);
            startActivity(intent);

            // Apply the slide-in and slide-out animations
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            finish();
        }, SPLASH_DISPLAY_LENGTH); // 2-second delay

    }
}