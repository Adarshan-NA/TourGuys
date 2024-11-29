package com.wlu.tourguys.project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private TextView greetingText;
    private EditText searchDestination;
    private ImageView searchIcon;
    private RecyclerView recyclerViewTrips;
    private BottomNavigationView bottomNavigation;

    // Firebase Database reference
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Replace with your actual XML layout file name if it's different

        // Initialize Views
        greetingText = findViewById(R.id.greeting_text);
        searchDestination = findViewById(R.id.search_destination);
        searchIcon = findViewById(R.id.searchIcon); // Ensure this matches the ImageView's actual ID in XML
        recyclerViewTrips = findViewById(R.id.recyclerView_trips);
        bottomNavigation = findViewById(R.id.bottom_navigation);

        // Initialize Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Trips");

        // Set up RecyclerView
        recyclerViewTrips.setLayoutManager(new LinearLayoutManager(this));

        // Assuming the adapter is handled elsewhere
        // You can set it up with actual data here

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
    }
}