package com.wlu.tourguys.project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
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
    EditText loginPasswordEditText, loginEmailEditText;
    TextView signupOption;
    boolean isPasswordVisible = false;
    Button loginButton;
    public SharedPreferences sharedPreferences;

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


        loginPasswordEditText = findViewById(R.id.loginPasswordEditText);

        // Set a touch listener to the drawable (end icon)
        loginPasswordEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    // Check if the touch is within the drawable bounds
                    if (event.getRawX() >= (loginPasswordEditText.getRight() - loginPasswordEditText.getCompoundDrawables()[2].getBounds().width())) {
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

                saveEmailToPreferences();

                Intent intent = new Intent(Login.this, MainActivity.class);
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

        loginEmailEditText = findViewById(R.id.loginEmailEditText);

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        String storedEmail = sharedPreferences.getString("DefaultEmail", "email@domain.com");
        loginEmailEditText.setText(storedEmail);


    }

    public void saveEmailToPreferences() {

        String email = loginEmailEditText.getText().toString().trim();
        String password = loginPasswordEditText.getText().toString().trim();

        if (email.isEmpty()) {
            loginEmailEditText.setError("Email field cannot be empty. Please enter your email.");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginEmailEditText.setError("Please enter a valid email address");
            return;
        }
        if (password.isEmpty()) {
            loginPasswordEditText.setError("Password field cannot be empty. Please enter your password.");
            return;
        }

//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("DefaultEmail", email);
//        editor.apply();

        Intent intent = new Intent(Login.this, signUp.class);
        startActivity(intent);
        // Apply the slide-in and slide-out animations
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Hide password
            loginPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            loginPasswordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.password_eye, 0);
        } else {
            // Show password
            loginPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            loginPasswordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.password_eye_hide, 0);
        }
        // Move the cursor to the end of the text
        loginPasswordEditText.setSelection(loginPasswordEditText.length());
        isPasswordVisible = !isPasswordVisible;
    }



}