package com.wlu.tourguys.project;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Login extends AppCompatActivity {
    EditText passwordEditText;
    TextView signupOption;
    boolean isPasswordVisible = false;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login_screen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        passwordEditText = findViewById(R.id.passwordEditText);

        // Set a touch listener to the drawable (end icon)
        passwordEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    // Check if the touch is within the drawable bounds
                    if (event.getRawX() >= (passwordEditText.getRight() - passwordEditText.getCompoundDrawables()[2].getBounds().width())) {
                        // Toggle the visibility mode
                        togglePasswordVisibility();
                        return true;
                    }
                }
                return false;
            }
        });

        signupOption = findViewById(R.id.signupOption);
        signupOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, signUp.class);
                startActivity(intent);
                // Apply the slide-in and slide-out animations
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, signUp.class);
                startActivity(intent);
                // Apply the slide-in and slide-out animations
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        // Initialize the button and set an onClick listener
        Button unlockWithFaceButton = findViewById(R.id.FaceID_loginButton);
        unlockWithFaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to FaceUnlockActivity
                Intent intent = new Intent(Login.this, faceUnlock.class);
                startActivity(intent);

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                finish();
            }

        });
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Hide password
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.password_eye, 0);
        } else {
            // Show password
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passwordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.password_eye_hide, 0);
        }
        // Move the cursor to the end of the text
        passwordEditText.setSelection(passwordEditText.length());
        isPasswordVisible = !isPasswordVisible;
    }



}