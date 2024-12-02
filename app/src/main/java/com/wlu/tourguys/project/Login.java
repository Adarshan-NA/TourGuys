package com.wlu.tourguys.project;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    EditText loginPasswordEditText, loginEmailEditText;
    TextView signupOption;
    boolean isPasswordVisible = false;
    Button loginButton;
    public SharedPreferences sharedPreferences;
    private FirebaseAuth firebaseAuth;

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

        firebaseAuth = FirebaseAuth.getInstance();

        // SharedPreferences for login state
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        // Check if user is already logged in
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            String email = sharedPreferences.getString("userEmail", null);
            if (email != null) {
                fetchAndNavigateToMain(email); // Fetch the user's name and navigate
            } else {
                navigateToMain("User"); // Fallback if no email found
            }
            return;
        }

        loginPasswordEditText = findViewById(R.id.loginPasswordEditText);
        loginPasswordEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (loginPasswordEditText.getRight() - loginPasswordEditText.getCompoundDrawables()[2].getBounds().width())) {
                        togglePasswordVisibility();
                        return true;
                    }
                }
                return false;
            }
        });

        signupOption = findViewById(R.id.signupOption);
        signupOption.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, signUp.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(v -> {
            String email = loginEmailEditText.getText().toString().trim();
            String password = loginPasswordEditText.getText().toString().trim();

            if (validateInputs(email, password)) {
                signIn(email, password);
            }
        });

        loginEmailEditText = findViewById(R.id.loginEmailEditText);

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String storedEmail = sharedPreferences.getString("DefaultEmail", "email@domain.com");
        loginEmailEditText.setText(storedEmail);
    }

    private boolean validateInputs(String email, String password) {
        if (email.isEmpty()) {
            loginEmailEditText.setError("Email field cannot be empty. Please enter your email.");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginEmailEditText.setError("Please enter a valid email address.");
            return false;
        }
        if (password.isEmpty()) {
            loginPasswordEditText.setError("Password field cannot be empty. Please enter your password.");
            return false;
        }
        return true;
    }

//    private void signIn(String email, String password) {
//        firebaseAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            Log.d(TAG, "signInWithEmail:success");
//                            FirebaseUser user = firebaseAuth.getCurrentUser();
//                            saveEmailToPreferences(email); // Save email in preferences
//                            navigateToMain(user); // Proceed to MainActivity
//                        } else {
//                            Log.w(TAG, "signInWithEmail:failure", task.getException());
//                            Toast.makeText(Login.this, "Authentication failed: Incorrect Email Id or Password", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }

    private void signIn(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            saveLoginState(email);
                            saveEmailToPreferences(email);
                            fetchAndNavigateToMain(email); // Fetch name and navigate to MainActivity
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed: Incorrect Email Id or Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void saveLoginState(String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putString("userEmail", email);
        editor.apply();
    }

    private void fetchAndNavigateToMain(String email) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        usersRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String name = userSnapshot.child("name").getValue(String.class);
                        navigateToMain(name); // Pass the name to MainActivity
                        break;
                    }
                } else {
                    Toast.makeText(Login.this, "User not found in database.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Database error: ", error.toException());
                Toast.makeText(Login.this, "Failed to fetch user details.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveEmailToPreferences(String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("DefaultEmail", email);
        editor.apply();
    }

//    private void navigateToMain(FirebaseUser user) {
//        if (user != null) {
//            Intent intent = new Intent(Login.this, MainActivity.class);
//            startActivity(intent);
//            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//            finish(); // Ensure the user cannot navigate back to the login screen
//        }
//    }

    private void navigateToMain(String name) {
        Intent intent = new Intent(Login.this, MainActivity.class);
        intent.putExtra("USER_NAME", name != null ? name : "User"); // Pass name or fallback to "User"
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish(); // Ensure the user cannot navigate back to the login screen
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            loginPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            loginPasswordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.password_eye, 0);
        } else {
            loginPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            loginPasswordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.password_eye_hide, 0);
        }
        loginPasswordEditText.setSelection(loginPasswordEditText.length());
        isPasswordVisible = !isPasswordVisible;
    }
}
