package com.wlu.tourguys.project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    protected LinearLayout profileSettingsButton;
    protected LinearLayout logoutButton;
    protected ProgressBar loadingProgress;
    protected TextView userNameTextView;
    protected TextView locationTextView;

    private SharedPreferences sharedPreferences;
    protected FirebaseAuth firebaseAuth;
    protected FirebaseUser currentUser;
    protected DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Initialize views
        profileSettingsButton = findViewById(R.id.profile_settings_button);
        logoutButton = findViewById(R.id.logout_button);
        loadingProgress = findViewById(R.id.loading_progress);
        userNameTextView = findViewById(R.id.user_name);
        locationTextView = findViewById(R.id.location);

        // Load user data
        loadUserData();

        // Set click listener for Profile Settings button
        profileSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgress.setVisibility(View.VISIBLE);

                // Navigate to ProfileSettingsActivity
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingProgress.setVisibility(View.GONE);
                        Intent intent = new Intent(Profile.this, ProfileSettingsActivity.class);
                        startActivity(intent);
                    }
                }, 1000); // 1000ms delay (1 second)
            }
        });

        // Set click listener for Logout button
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to Login screen
//                Intent intent = new Intent(Profile.this, Login.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                finish();
                sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                Intent intent = new Intent(Profile.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        // Handle BottomNavigationView actions
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                // Handle home action
                startActivity(new Intent(this, MainActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_add_trip) {
                // Handle add trip action
                startActivity(new Intent(this, AddTripActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_guide) {
                // Handle guide action
                startActivity(new Intent(this, GuideActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                // Already on the Profile screen
                return true;
            }
            return false;
        });

        // Set the current selection to Profile
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
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
                    String city = snapshot.child("city").getValue(String.class);
                    String country = snapshot.child("country").getValue(String.class);

                    // Update UI
                    userNameTextView.setText(name != null ? name : "Unknown");
                    locationTextView.setText((city != null ? city : "Unknown City") + ", " + (country != null ? country : "Unknown Country"));
                } else {
                    Toast.makeText(Profile.this, "User data not found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Profile.this, "Failed to load user data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
