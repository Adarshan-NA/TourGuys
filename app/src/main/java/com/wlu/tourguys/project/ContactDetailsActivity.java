package com.wlu.tourguys.project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ContactDetailsActivity extends AppCompatActivity {

    private TextView profileName, profileLocation, phoneText, emailText;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private BottomNavigationView bottomNavigation;
    ImageView backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        // Initialize views
        backButton = findViewById(R.id.back_button);
        profileName = findViewById(R.id.profileName);
        profileLocation = findViewById(R.id.profileLocation);
        phoneText = findViewById(R.id.phoneText);
        emailText = findViewById(R.id.emailText);
        bottomNavigation = findViewById(R.id.bottom_navigation);

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Fetch user data from Firebase
        String userId = firebaseAuth.getCurrentUser().getUid();
        fetchUserData(userId);

        // Get data from intent
        String username = (String) getIntent().getSerializableExtra("userName");
        String userEmail = (String) getIntent().getSerializableExtra("userEmail");
        String userPhone = (String) getIntent().getSerializableExtra("userPhone");
        profileName.setText(String.valueOf(username));
        emailText.setText(String.valueOf(userEmail));
        phoneText.setText(String.valueOf(userPhone));


        // Set up Bottom Navigation View
        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                // Handle home action
                startActivity(new Intent(this, MainActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_add_trip) {
                // Navigate to AddTripActivity when Add Trip icon is clicked
                startActivity(new Intent(this, AddTripActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_guide) {
                startActivity(new Intent(this, GuideActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                startActivity(new Intent(this, Profile.class));
                return true;
            }
            return false;
        });

        // Back button click listener
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(ContactDetailsActivity.this, DetailsActivity.class);
            startActivity(intent);
        });

    }

    private void fetchUserData(String userId) {
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Get user data from Firebase

                    String city = dataSnapshot.child("city").getValue(String.class);
                    String country = dataSnapshot.child("country").getValue(String.class);

                    profileLocation.setText(city + ", " + country);
                } else {
                    Toast.makeText(ContactDetailsActivity.this, "User data not found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ContactDetailsActivity.this, "Failed to load user data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
