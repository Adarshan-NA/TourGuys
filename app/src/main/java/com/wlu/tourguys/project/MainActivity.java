package com.wlu.tourguys.project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;

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

        // Save sample data to Firebase
        saveSampleDataToFirebase();

        // Set up RecyclerView
        recyclerViewTrips.setLayoutManager(new LinearLayoutManager(this));

        // Set up the adapter with sample data
        DestinationAdapter adapter = new DestinationAdapter(getSampleDestinations());
        recyclerViewTrips.setAdapter(adapter);

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

    // Method to save sample destinations to Firebase
    private void saveSampleDataToFirebase() {
        List<Destination> destinations = getSampleDestinations();
        for (Destination destination : destinations) {
            // Push each destination as a child in the Firebase database
            String key = databaseReference.push().getKey();
            if (key != null) {
                databaseReference.child(key).setValue(destination)
                        .addOnSuccessListener(aVoid -> Log.d("Firebase", "Data saved successfully"))
                        .addOnFailureListener(e -> Log.e("Firebase", "Failed to save data", e));
            }
        }
    }

    // Sample data method for the destinations
    private List<Destination> getSampleDestinations() {
        List<Destination> destinations = new ArrayList<>();
        destinations.add(new Destination("Ann Press", "December 15 - December 20", "7", "Mount Everest", "Nepal"));
        destinations.add(new Destination("Ann Press", "December 15 - December 20", "7", "Borobudur", "Magelang, Indonesia"));
        // Add more sample destinations if needed
        return destinations;
    }
}
