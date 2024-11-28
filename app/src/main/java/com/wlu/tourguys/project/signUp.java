package com.wlu.tourguys.project;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class signUp extends AppCompatActivity {
    EditText signupPasswordEditText, signupPasswordConfirmEditText, signupEmailEditText, signupNameEditText;
    CheckBox notARobotCheckbox;
    Button signUpButton;
    TextView loginOption;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    boolean isPasswordVisible = false;

    @SuppressLint("RestrictedApi")
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
        signupNameEditText = findViewById(R.id.signupNameEditText);
        signupPasswordConfirmEditText = findViewById(R.id.signupPasswordConfirmEditText);
        notARobotCheckbox = findViewById(R.id.not_a_robot_checkbox);
        signUpButton = findViewById(R.id.signupButton);
        loginOption = findViewById(R.id.loginOption);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        signupPasswordEditText.setOnTouchListener((v, event) -> handlePasswordToggle(event, signupPasswordEditText));
        signupPasswordConfirmEditText.setOnTouchListener((v, event) -> handlePasswordToggle(event, signupPasswordConfirmEditText));

        loginOption.setOnClickListener(v -> {
            Intent intent = new Intent(signUp.this, Login.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        signUpButton.setOnClickListener(v -> {
            String email = signupEmailEditText.getText().toString().trim();
            String password = signupPasswordEditText.getText().toString().trim();
            String retypePassword = signupPasswordConfirmEditText.getText().toString().trim();
            String name = signupNameEditText.getText().toString().trim();

            if (validateInputs(email, password, retypePassword, name)) {
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                String userId = firebaseAuth.getCurrentUser().getUid();
                                addUserToDatabase(userId, name, email);
                                Toast.makeText(signUp.this, "Sign-Up Successful!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(signUp.this, Login.class));
                                finish();
                            } else {
                                Toast.makeText(signUp.this, "Sign-Up Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private boolean validateInputs(String email, String password, String retypePassword, String name) {
        if (name.isEmpty()) {
            signupNameEditText.setError("Name cannot be empty.");
            return false;
        }
        if (email.isEmpty()) {
            signupEmailEditText.setError("Email cannot be empty.");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signupEmailEditText.setError("Please enter a valid email.");
            return false;
        }
        if (password.isEmpty()) {
            signupPasswordEditText.setError("Password cannot be empty.");
            return false;
        }
        if (password.length() < 6) {
            signupPasswordEditText.setError("Password must be at least 6 characters long.");
            return false;
        }
        if (!password.equals(retypePassword)) {
            signupPasswordConfirmEditText.setError("Passwords do not match.");
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

    private void addUserToDatabase(String userId, String name, String email) {
        HashMap<String, String> user = new HashMap<>();
        user.put("name", name);
        user.put("email", email);

        databaseReference.child(userId).setValue(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(signUp.this, "User added to database!", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(TAG, "Database Error: " + task.getException().getMessage());
                        Toast.makeText(signUp.this, "Database Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}


