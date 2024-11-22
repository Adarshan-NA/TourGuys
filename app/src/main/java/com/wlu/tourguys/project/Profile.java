package com.wlu.tourguys.project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
//import com.wlu.tourguys.project.guide.GuideActivity;

public class Profile extends AppCompatActivity {

    private LinearLayout profileSettingsButton;
    private LinearLayout logoutButton;
    private ProgressBar loadingProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize views
        profileSettingsButton = findViewById(R.id.profile_settings_button);
        logoutButton = findViewById(R.id.logout_button);
        loadingProgress = findViewById(R.id.loading_progress);

        // Set click listener for Profile Settings button
        profileSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the ProgressBar
                loadingProgress.setVisibility(View.VISIBLE);

                // Perform the navigation to the fragment
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_profile_settings, new ProfileSettingsFragment())
                        .addToBackStack(null)
                        .commit();

                // Add a delay to hide the ProgressBar after navigation
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingProgress.setVisibility(View.GONE);
                    }
                }, 1000); // 1000ms delay (1 second)
            }
        });

        // Set click listener for Logout button
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to Login screen
                Intent intent = new Intent(Profile.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        // Handle BottomNavigationView actions
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.nav_home:
//                        startActivity(new Intent(Profile.this, MainActivity.class));
//                        return true;
//                    case R.id.nav_add_trip:
//                        startActivity(new Intent(Profile.this, AddTripActivity.class));
//                        return true;
//                    case R.id.nav_guide:
//                        startActivity(new Intent(Profile.this, DetailsActivity.class));
//                        return true;
//                    case R.id.nav_profile:
//                        // Already on the Profile screen
//                        return true;
//                }
//                return false;
//            }
//        });
            // Set up Bottom Navigation View
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
                if (item.getItemId() == R.id.nav_home) {
                    // Handle home action
                    startActivity(new Intent(this, MainActivity.class));
                    return true;
                } else if (item.getItemId() == R.id.nav_add_trip) {
                    // Handle add trip action
                    return true;
                } else if (item.getItemId() == R.id.nav_guide) {
                    startActivity(new Intent(this, GuideActivity.class));
                    // Handle guide action
                    return true;
                } else if (item.getItemId() == R.id.nav_profile) {
                    // Handle profile action
                    return true;
                }
                return false;
            });

        // Set the current selection to Profile
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
    }
}
