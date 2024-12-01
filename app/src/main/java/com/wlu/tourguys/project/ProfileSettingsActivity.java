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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class ProfileSettingsActivity extends AppCompatActivity {

    private static final String TAG = "ProfileSettingsActivity";
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;

    private EditText nameField, passwordField, mobileField, cityField, countryField;
    private TextView emailField; // Email is now non-editable
    private Button saveButton;
    private ImageView backButton, cameraIcon, profileImageView;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private DatabaseReference databaseReference;

    // ActivityResultLauncher for the camera
    private ActivityResultLauncher<Intent> cameraLauncher;

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

    private void setupCameraLauncher() {
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getExtras() != null) {
                            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                            profileImageView.setImageBitmap(imageBitmap); // Set the captured image locally
                            deleteOldProfileImage(); // Delete old profile image
                            uploadProfileImage(imageBitmap); // Save new image to Firebase
                        }
                    }
                });
    }

    private void uploadProfileImage(Bitmap imageBitmap) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference profileImageRef = storageRef.child("profile_images/" + currentUser.getUid() + ".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = profileImageRef.putBytes(data);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            profileImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String imageUrl = uri.toString();
                databaseReference.child(currentUser.getUid()).child("profileImageUrl").setValue(imageUrl)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(ProfileSettingsActivity.this, "Profile image updated successfully!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ProfileSettingsActivity.this, "Failed to update profile image.", Toast.LENGTH_SHORT).show();
                            }
                        });
            });
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void deleteOldProfileImage() {
        StorageReference oldImageRef = FirebaseStorage.getInstance().getReference()
                .child("profile_images/" + currentUser.getUid() + ".jpg");

        oldImageRef.delete().addOnSuccessListener(aVoid -> {
            Log.d(TAG, "Old profile image deleted successfully.");
        }).addOnFailureListener(e -> {
            Log.d(TAG, "Failed to delete old profile image: " + e.getMessage());
        });
    }

    private void loadUserData() {
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
                    String profileImageUrl = snapshot.child("profileImageUrl").getValue(String.class);

                    nameField.setText(name);
                    emailField.setText(email);
                    mobileField.setText(mobile);
                    cityField.setText(city);
                    countryField.setText(country);

                    if (profileImageUrl != null) {
                        Glide.with(ProfileSettingsActivity.this)
                                .load(profileImageUrl)
                                .placeholder(R.drawable.profile_image) // Placeholder image
                                .into(profileImageView);
                    }
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

    private void openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            launchCameraIntent();
        }
    }

    private void launchCameraIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> cameraApps = packageManager.queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);

        if (cameraApps == null || cameraApps.isEmpty()) {
            Toast.makeText(this, "No camera app found.", Toast.LENGTH_SHORT).show();
            return;
        }

        cameraLauncher.launch(takePictureIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchCameraIntent();
            } else {
                Toast.makeText(this, "Camera permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveUserData() {
        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated. Please log in.", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();
        String name = nameField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        String mobile = mobileField.getText().toString().trim();
        String city = cityField.getText().toString().trim();
        String country = countryField.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(this, "Name is required!", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference userRef = databaseReference.child(userId);
        userRef.child("name").setValue(name);

        if (!mobile.isEmpty()) {
            userRef.child("mobile").setValue(mobile);
        }
        if (!city.isEmpty()) {
            userRef.child("city").setValue(city);
        }
        if (!country.isEmpty()) {
            userRef.child("country").setValue(country);
        }

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

    private void navigateBackToProfile() {
        Intent intent = new Intent(ProfileSettingsActivity.this, Profile.class);
        startActivity(intent);
        finish();
    }
}
