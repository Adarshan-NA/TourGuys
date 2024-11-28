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

    private void signIn(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            saveEmailToPreferences(email); // Save email in preferences
                            navigateToMain(user); // Proceed to MainActivity
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed: Incorrect Email Id or Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveEmailToPreferences(String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("DefaultEmail", email);
        editor.apply();
    }

    private void navigateToMain(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish(); // Ensure the user cannot navigate back to the login screen
        }
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






//package com.wlu.tourguys.project;
//
//import static android.content.ContentValues.TAG;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.text.InputType;
//import android.util.Log;
//import android.util.Patterns;
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//
//public class Login extends AppCompatActivity {
//    EditText loginPasswordEditText, loginEmailEditText;
//    TextView signupOption;
//    boolean isPasswordVisible = false;
//    Button loginButton;
//    public SharedPreferences sharedPreferences;
//    private FirebaseAuth firebaseAuth;
//    private DatabaseReference databaseReference;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_login);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login_screen), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//        firebaseAuth = FirebaseAuth.getInstance();
//
//        loginPasswordEditText = findViewById(R.id.loginPasswordEditText);
//
//        // Set a touch listener to the drawable (end icon)
//        loginPasswordEditText.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    // Check if the touch is within the drawable bounds
//                    if (event.getRawX() >= (loginPasswordEditText.getRight() - loginPasswordEditText.getCompoundDrawables()[2].getBounds().width())) {
//                        // Toggle the visibility mode
//                        togglePasswordVisibility();
//                        return true;
//                    }
//                }
//                return false;
//            }
//        });
//
//        signupOption = findViewById(R.id.signupOption);
//        signupOption.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Login.this, signUp.class);
//                startActivity(intent);
//                // Apply the slide-in and slide-out animations
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//            }
//        });
//
//        loginButton = findViewById(R.id.loginButton);
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                saveEmailToPreferences();
//
//                Intent intent = new Intent(Login.this, MainActivity.class);
//                startActivity(intent);
//                // Apply the slide-in and slide-out animations
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//
//            }
//        });
//
//        // Initialize the button and set an onClick listener
//        Button unlockWithFaceButton = findViewById(R.id.FaceID_loginButton);
//        unlockWithFaceButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Navigate to FaceUnlockActivity
//                Intent intent = new Intent(Login.this, faceUnlock.class);
//                startActivity(intent);
//
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//
//                finish();
//            }
//        });
//
//        loginEmailEditText = findViewById(R.id.loginEmailEditText);
//
//        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
//
//        String storedEmail = sharedPreferences.getString("DefaultEmail", "email@domain.com");
//        loginEmailEditText.setText(storedEmail);
//
//
//    }
//
//    private void signIn(String email, String password) {
//        // [START sign_in_with_email]
//        firebaseAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithEmail:success");
//                            FirebaseUser user = firebaseAuth.getCurrentUser();
//                            updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithEmail:failure", task.getException());
//                            Toast.makeText(Login.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
//                        }
//                    }
//                });
//        // [END sign_in_with_email]
//    }
//
//    public void saveEmailToPreferences() {
//
//        String email = loginEmailEditText.getText().toString().trim();
//        String password = loginPasswordEditText.getText().toString().trim();
//
//        if (email.isEmpty()) {
//            loginEmailEditText.setError("Email field cannot be empty. Please enter your email.");
//            return;
//        }
//        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            loginEmailEditText.setError("Please enter a valid email address");
//            return;
//        }
//        if (password.isEmpty()) {
//            loginPasswordEditText.setError("Password field cannot be empty. Please enter your password.");
//            return;
//        }
//
////        SharedPreferences.Editor editor = sharedPreferences.edit();
////        editor.putString("DefaultEmail", email);
////        editor.apply();
//
//        Intent intent = new Intent(Login.this, signUp.class);
//        startActivity(intent);
//        // Apply the slide-in and slide-out animations
//        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//    }
//
//    private void togglePasswordVisibility() {
//        if (isPasswordVisible) {
//            // Hide password
//            loginPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//            loginPasswordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.password_eye, 0);
//        } else {
//            // Show password
//            loginPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
//            loginPasswordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.password_eye_hide, 0);
//        }
//        // Move the cursor to the end of the text
//        loginPasswordEditText.setSelection(loginPasswordEditText.length());
//        isPasswordVisible = !isPasswordVisible;
//    }
//
//    private void updateUI(FirebaseUser user) {
//
//    }
//
//}