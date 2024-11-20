package com.wlu.tourguys.project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class signUp extends AppCompatActivity {
    EditText signupPasswordEditText, signupPasswordConfirmEditText, signupEmailEditText;
    boolean isPasswordVisible = false;
    CheckBox notARobotCheckbox;
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

        signupPasswordEditText = findViewById(R.id.signupPasswordEditText);
        signupEmailEditText = findViewById(R.id.signupEmailEditText);
        // Set a touch listener to the drawable (end icon)

        signupPasswordEditText.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    // Check if the touch is within the drawable bounds
                    if (event.getRawX() >= (signupPasswordEditText.getRight() - signupPasswordEditText.getCompoundDrawables()[2].getBounds().width())) {
                        // Toggle the visibility mode
                        togglePasswordVisibility();
                        return true;
                    }
                }
                return false;
            }
        });

        signupPasswordConfirmEditText = findViewById(R.id.signupPasswordConfirmEditText);
        // Set a touch listener to the drawable (end icon)
        signupPasswordConfirmEditText.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    // Check if the touch is within the drawable bounds
                    if (event.getRawX() >= (signupPasswordConfirmEditText.getRight() - signupPasswordConfirmEditText.getCompoundDrawables()[2].getBounds().width())) {
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

        notARobotCheckbox = findViewById(R.id.not_a_robot_checkbox);
        notARobotCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        // Checkbox is checked, enable the button
                        Intent intent = new Intent(signUp.this, faceUnlock.class);
                        startActivity(intent);

                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                });

        signUpButton = findViewById(R.id.signupButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!notARobotCheckbox.isChecked()) {
                    // Show a message if the checkbox is not checked
                    Toast.makeText(signUp.this, "Please confirm you're not a robot!", Toast.LENGTH_SHORT).show();
                } else {
                    if (signupPasswordEditText.equals(signupPasswordConfirmEditText)) {
                        Toast.makeText(signUp.this, "Sign-Up Successful!", Toast.LENGTH_SHORT).show();
                        // Add your sign-up logic here (e.g., saving user data)
                    } else {
                        Toast.makeText(signUp.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                    }
                }
                }
        });
        saveEmailToPreferences();


    }


    public void saveEmailToPreferences() {

        String email = signupEmailEditText.getText().toString().trim();
        String password = signupPasswordEditText.getText().toString().trim();
        String retypePassword = signupPasswordConfirmEditText.getText().toString().trim();

        if (email.isEmpty()) {
            signupEmailEditText.setError("Email field cannot be empty. Please enter your email.");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signupEmailEditText.setError("Please enter a valid email address");
            return;
        }
        if (password.isEmpty()) {
            signupPasswordEditText.setError("Password field cannot be empty. Please enter your password.");
            return;
        }
        if (retypePassword.isEmpty()) {
            signupPasswordConfirmEditText.setError("Password field cannot be empty. Please enter your password.");
            return;
        }

        Intent intent = new Intent(signUp.this, Login.class);
        startActivity(intent);
        // Apply the slide-in and slide-out animations
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }



    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Hide password
            signupPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            signupPasswordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.password_eye, 0);
        } else {
            // Show password
            signupPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            signupPasswordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.password_eye_hide, 0);
        }
        // Move the cursor to the end of the text
        signupPasswordEditText.setSelection(signupPasswordEditText.length());
        isPasswordVisible = !isPasswordVisible;
    }

    private void toggleConfirmPasswordVisibility() {
        if (isPasswordVisible) {
            // Hide password
            signupPasswordConfirmEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            signupPasswordConfirmEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.password_eye, 0);
        } else {
            // Show password
            signupPasswordConfirmEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            signupPasswordConfirmEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.password_eye_hide, 0);
        }
        // Move the cursor to the end of the text
        signupPasswordConfirmEditText.setSelection(signupPasswordConfirmEditText.length());
        isPasswordVisible = !isPasswordVisible;
    }


}