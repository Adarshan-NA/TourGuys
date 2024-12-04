package com.wlu.tourguys.project;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ContactDetailsActivity extends AppCompatActivity {

    private ImageButton backButton;
    private ImageView profileImage;
    private TextView profileName, profileLocation, phoneText, emailText;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        // Initialize views
        backButton = findViewById(R.id.backButton);
        profileImage = findViewById(R.id.profileImage);
        profileName = findViewById(R.id.profileName);
        profileLocation = findViewById(R.id.profileLocation);
        phoneText = findViewById(R.id.phoneText);
        emailText = findViewById(R.id.emailText);

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Fetch user data from Firebase
        String userId = firebaseAuth.getCurrentUser().getUid();
        fetchUserData(userId);

        // Back button click listener
        backButton.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void fetchUserData(String userId) {
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Get user data from Firebase
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String phone = dataSnapshot.child("phone").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String city = dataSnapshot.child("city").getValue(String.class);
                    String country = dataSnapshot.child("country").getValue(String.class);

                    // Set the user data to views
                    profileName.setText(name);
                    phoneText.setText(phone);
                    emailText.setText(email);
                    profileLocation.setText(city + ", " + country);

                    // You can add logic here to load the profile image if it's stored in Firebase Storage
                    // For now, assuming you have a default profile image
                    profileImage.setImageResource(R.drawable.sample_profile_image);
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
