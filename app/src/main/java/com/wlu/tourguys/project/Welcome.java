package com.wlu.tourguys.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Welcome extends AppCompatActivity {
    Button signIn, signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.welcome_screen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the button and set an onClick listener
        signIn = findViewById(R.id.signInButton);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to FaceUnlockActivity
                Intent intent = new Intent(Welcome.this, Login.class);
                startActivity(intent);
                // Apply the slide-in and slide-out animations
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
            });
        // Initialize the button and set an onClick listener
        signUp = findViewById(R.id.signUpButton);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to FaceUnlockActivity
                Intent intent = new Intent(Welcome.this, signUp.class);
                startActivity(intent);
                // Apply the slide-in and slide-out animations
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                finish();
            }
        });
}
}