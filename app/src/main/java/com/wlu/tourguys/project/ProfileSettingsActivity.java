package com.wlu.tourguys.project;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ProfileSettingsActivity extends AppCompatActivity {

    protected static final String TAG = "ProfileSettingsActivity";
    protected static final int CAMERA_PERMISSION_REQUEST_CODE = 100;

    protected EditText nameField, emailField, passwordField, mobileField, cityField, countryField;
    protected Button saveButton;
    protected ImageView backButton, cameraIcon, profileImageView;

    protected FirebaseAuth firebaseAuth;
    protected FirebaseUser currentUser;
    protected DatabaseReference databaseReference;

    // ActivityResultLauncher for the camera
    protected ActivityResultLauncher<Intent> cameraLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Initialize views
        nameField = findViewById(R.id.name_field);
        emailField = findViewById(R.id.email_field);
        passwordField = findViewById(R.id.password_field);
        mobileField = findViewById(R.id.mobile_field);
        cityField = findViewById(R.id.city_field);
        countryField = findViewById(R.id.country_field);
        saveButton = findViewById(R.id.save_button);
        backButton = findViewById(R.id.back_button);
        cameraIcon = findViewById(R.id.camera_icon);
        profileImageView = findViewById(R.id.profile_image);

        // Load user data
        loadUserData();

        // Save updated profile data
        saveButton.setOnClickListener(v -> saveUserData());

        // Navigate back to Profile activity
        backButton.setOnClickListener(v -> navigateBackToProfile());

        // Camera setup
        setupCameraLauncher();

        // Handle camera icon click
        cameraIcon.setOnClickListener(v -> openCamera());
    }

    protected void setupCameraLauncher() {
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getExtras() != null) {
                            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                            profileImageView.setImageBitmap(imageBitmap); // Set the captured image
                        } else {
                            Log.d(TAG, "Camera activity returned no data");
                        }
                    } else {
                        Log.d(TAG, "Camera activity not completed successfully");
                    }
                });
    }

    protected void openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            launchCameraIntent();
        }
    }

    protected void launchCameraIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> cameraApps = getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);

        if (cameraApps.isEmpty()) {
            Toast.makeText(this, "No camera app found.", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "Launching camera intent...");
        cameraLauncher.launch(takePictureIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Camera permission granted, launching camera...");
                launchCameraIntent();
            } else {
                Toast.makeText(this, "Camera permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void loadUserData() {
        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated. Please log in.", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();

        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String mobile = snapshot.child("mobile").getValue(String.class);
                    String city = snapshot.child("city").getValue(String.class);
                    String country = snapshot.child("country").getValue(String.class);

                    nameField.setText(name);
                    emailField.setText(email);
                    mobileField.setText(mobile);
                    cityField.setText(city);
                    countryField.setText(country);
                } else {
                    Toast.makeText(ProfileSettingsActivity.this, "No user data found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileSettingsActivity.this, "Failed to load user data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void saveUserData() {
        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated. Please log in.", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();
        String name = nameField.getText().toString().trim();
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        String mobile = mobileField.getText().toString().trim();
        String city = cityField.getText().toString().trim();
        String country = countryField.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Name and email are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update Firebase fields
        databaseReference.child(userId).child("name").setValue(name);
        databaseReference.child(userId).child("email").setValue(email);
        databaseReference.child(userId).child("mobile").setValue(mobile);
        databaseReference.child(userId).child("city").setValue(city);
        databaseReference.child(userId).child("country").setValue(country);

        // Update password if entered
        if (!password.isEmpty()) {
            currentUser.updatePassword(password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Password updated successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to update password.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
    }

    protected void navigateBackToProfile() {
        Intent intent = new Intent(ProfileSettingsActivity.this, Profile.class);
        startActivity(intent);
        finish();
    }
}
