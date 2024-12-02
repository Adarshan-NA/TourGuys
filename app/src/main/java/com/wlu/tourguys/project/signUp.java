package com.wlu.tourguys.project;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class signUp extends AppCompatActivity {

    private EditText signupPasswordEditText, signupPasswordConfirmEditText, signupEmailEditText, signupNameEditText, signupPhoneEditText;
    private CheckBox notARobotCheckbox;
    private Button signUpButton;
    private TextView loginOption;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private boolean isPasswordVisible = false;
    private static final int VERIFY_HUMAN_REQUEST_CODE = 1;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize views
        signupPasswordEditText = findViewById(R.id.signupPasswordEditText);
        signupPasswordConfirmEditText = findViewById(R.id.signupPasswordConfirmEditText);
        signupEmailEditText = findViewById(R.id.signupEmailEditText);
        signupPhoneEditText = findViewById(R.id.signupPhoneEditText);
        signupNameEditText = findViewById(R.id.signupNameEditText);
        notARobotCheckbox = findViewById(R.id.not_a_robot_checkbox);
        signUpButton = findViewById(R.id.signupButton);
        loginOption = findViewById(R.id.loginOption);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Toggle password visibility
        signupPasswordEditText.setOnTouchListener((v, event) -> handlePasswordToggle(event, signupPasswordEditText));
        signupPasswordConfirmEditText.setOnTouchListener((v, event) -> handlePasswordToggle(event, signupPasswordConfirmEditText));

        // Navigate to login screen
        loginOption.setOnClickListener(v -> {
            startActivity(new Intent(signUp.this, Login.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        // Checkbox click listener
        notARobotCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Intent intent = new Intent(signUp.this, verifyHuman.class);
                startActivityForResult(intent, VERIFY_HUMAN_REQUEST_CODE);
            }
        });

        // Sign-Up button functionality
        signUpButton.setOnClickListener(v -> {
            String email = signupEmailEditText.getText().toString().trim();
            String password = signupPasswordEditText.getText().toString().trim();
            String retypePassword = signupPasswordConfirmEditText.getText().toString().trim();
            String name = signupNameEditText.getText().toString().trim();
            String phone = signupPhoneEditText.getText().toString().trim();

            if (validateInputs(email, password, retypePassword, name, phone)) {
                registerUser(email, password, name, phone);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("signupName", signupNameEditText.getText().toString());
        outState.putString("signupEmail", signupEmailEditText.getText().toString());
        outState.putString("signupPassword", signupPasswordEditText.getText().toString());
        outState.putString("signupPasswordConfirm", signupPasswordConfirmEditText.getText().toString());
        outState.putBoolean("notARobotChecked", notARobotCheckbox.isChecked());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        signupNameEditText.setText(savedInstanceState.getString("signupName"));
        signupEmailEditText.setText(savedInstanceState.getString("signupEmail"));
        signupPasswordEditText.setText(savedInstanceState.getString("signupPassword"));
        signupPasswordConfirmEditText.setText(savedInstanceState.getString("signupPasswordConfirm"));
        notARobotCheckbox.setChecked(savedInstanceState.getBoolean("notARobotChecked"));
    }

    private boolean validateInputs(String email, String password, String retypePassword, String name, String phone) {
        if (name.isEmpty()) {
            signupNameEditText.setError("Name cannot be empty.");
            signupNameEditText.requestFocus();
            return false;
        }
        if (email.isEmpty()) {
            signupEmailEditText.setError("Email cannot be empty.");
            signupEmailEditText.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signupEmailEditText.setError("Please enter a valid email.");
            signupEmailEditText.requestFocus();
            return false;
        }
        if (phone.isEmpty()) {
            signupPhoneEditText.setError("Phone number cannot be empty.");
            signupPhoneEditText.requestFocus();
            return false;
        }
        if (phone.length() != 10) {
            signupPhoneEditText.setError("Phone number must be exactly 10 digits.");
            signupPhoneEditText.requestFocus();
            return false;
        }
        if (password.isEmpty()) {
            signupPasswordEditText.setError("Password cannot be empty.");
            signupPasswordEditText.requestFocus();
            return false;
        }
        if (password.length() < 6) {
            signupPasswordEditText.setError("Password must be at least 6 characters long.");
            signupPasswordEditText.requestFocus();
            return false;
        }
        if (!password.equals(retypePassword)) {
            signupPasswordConfirmEditText.setError("Passwords do not match.");
            signupPasswordConfirmEditText.requestFocus();
            return false;
        }
        if (!notARobotCheckbox.isChecked()) {
            Toast.makeText(this, "Please verify that you are not a robot.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean handlePasswordToggle(MotionEvent event, EditText editText) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (event.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[2].getBounds().width())) {
                togglePasswordVisibility(editText);
                return true;
            }
        }
        return false;
    }

    private void togglePasswordVisibility(EditText editText) {
        if (isPasswordVisible) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            editText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.password_eye, 0);
        } else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            editText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.password_eye_hide, 0);
        }
        editText.setSelection(editText.length());
        isPasswordVisible = !isPasswordVisible;
    }

    private void registerUser(String email, String password, String name, String phone) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String userId = firebaseAuth.getCurrentUser().getUid();
                        addUserToDatabase(userId, name, email, phone);
                        Toast.makeText(this, "Sign-Up Successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, Login.class));
                        finish();
                    } else {
                        Log.d(TAG, "Sign-Up Error: " + task.getException().getMessage());
                        Toast.makeText(this, "Sign-Up Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addUserToDatabase(String userId, String name, String email, String phone) {
        HashMap<String, String> user = new HashMap<>();
        user.put("name", name);
        user.put("email", email);
        user.put("phone", phone);

        databaseReference.child(userId).setValue(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "User added to database!");
                    } else {
                        Log.d(TAG, "Database Error: " + task.getException().getMessage());
                    }
                });
    }
}
