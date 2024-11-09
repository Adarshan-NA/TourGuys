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

public class signUp extends AppCompatActivity {
    EditText passwordEditText;
    EditText passwordConfirmEditText;
    boolean isPasswordVisible = false;
    Button signUpButton;
    TextView loginOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.sign_up_screen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        passwordEditText = findViewById(R.id.signupPasswordEditText);
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

        passwordConfirmEditText = findViewById(R.id.signupPasswordConfirmEditText);
        // Set a touch listener to the drawable (end icon)
        passwordConfirmEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    // Check if the touch is within the drawable bounds
                    if (event.getRawX() >= (passwordConfirmEditText.getRight() - passwordConfirmEditText.getCompoundDrawables()[2].getBounds().width())) {
                        // Toggle the visibility mode
                        toggleConfirmPasswordVisibility();
                        return true;
                    }
                }
                return false;
            }
        });

        loginOption = findViewById(R.id.loginOption);
        loginOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signUp.this, Login.class);
                startActivity(intent);
                // Apply the slide-in and slide-out animations
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        signUpButton = findViewById(R.id.signupButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signUp.this, Login.class);
                startActivity(intent);
                // Apply the slide-in and slide-out animations
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

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

    private void toggleConfirmPasswordVisibility() {
        if (isPasswordVisible) {
            // Hide password
            passwordConfirmEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordConfirmEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.password_eye, 0);
        } else {
            // Show password
            passwordConfirmEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passwordConfirmEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.password_eye_hide, 0);
        }
        // Move the cursor to the end of the text
        passwordConfirmEditText.setSelection(passwordConfirmEditText.length());
        isPasswordVisible = !isPasswordVisible;
    }


}