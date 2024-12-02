package com.wlu.tourguys.project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private List<Destination> destinationList = new ArrayList<>();
    private DestinationAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Save the userName in the bundle
        if (savedInstanceState != null) {
            savedInstanceState.putString("USER_NAME", getIntent().getStringExtra("USER_NAME"));
        }

        setContentView(R.layout.activity_main);

        // Initialize Views
        greetingText = findViewById(R.id.greeting_text);
        searchDestination = findViewById(R.id.search_destination);
        searchIcon = findViewById(R.id.searchIcon);
        recyclerViewTrips = findViewById(R.id.recyclerView_trips);
        bottomNavigation = findViewById(R.id.bottom_navigation);

        // Retrieve the name passed from Login
//        String userName = getIntent().getStringExtra("USER_NAME");
//        if (userName != null) {
//            greetingText.setText("Welcome, " + userName + "!");
//        } else {
//            greetingText.setText("Welcome!");
//        }

        String userName;
        if (savedInstanceState != null) {
            // Restore the userName from savedInstanceState
            userName = savedInstanceState.getString("USER_NAME");
        } else {
            // Retrieve the name passed from Login
            userName = getIntent().getStringExtra("USER_NAME");
        }

        if (userName != null) {
            greetingText.setText("Welcome, " + userName + "!");
        }
//        else {
//            greetingText.setText("Welcome!");
//        }

        // Initialize Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Trips");

        // Set up RecyclerView
        recyclerViewTrips.setLayoutManager(new LinearLayoutManager(this));
//        adapter = new DestinationAdapter(destinationList);
//        recyclerViewTrips.setAdapter(adapter);

        // Updated adapter initialization
        adapter = new DestinationAdapter(destinationList, this);
        recyclerViewTrips.setAdapter(adapter);

        // Fetch trips from Firebase
        fetchTripsFromFirebase();

        // Set up the search functionality
        searchDestination.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                filterTrips(charSequence.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) {}
        });

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

    // Fetch trips from Firebase
    private void fetchTripsFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                destinationList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Destination destination = snapshot.getValue(Destination.class);
                    if (destination != null) {
                        destinationList.add(destination);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Error fetching data", databaseError.toException());
                Toast.makeText(MainActivity.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Filter trips based on search query (country or city)
//    private void filterTrips(String query) {
//        List<Destination> filteredList = new ArrayList<>();
//        for (Destination destination : destinationList) {
//            if (destination.getDestinationCountry().toLowerCase().contains(query.toLowerCase()) ||
//                    destination.getDestinationCity().toLowerCase().contains(query.toLowerCase())) {
//                filteredList.add(destination);
//            }
//        }
//        // Update the adapter with the filtered list
//        adapter.updateData(filteredList);
//    }

    private void filterTrips(String query) {
        List<Destination> filteredList = new ArrayList<>();

        if (query.isEmpty()) {
            // If the search query is empty, reload all trips from the Firebase list
           // filteredList.addAll(destinationList);
            // Fetch trips from Firebase
            fetchTripsFromFirebase();
        } else {
            // Filter trips based on query (country or city)
            for (Destination destination : destinationList) {
                if (destination.getDestinationCountry().toLowerCase().contains(query.toLowerCase()) ||
                        destination.getDestinationCity().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(destination);
                }
            }
        }

        // Update the adapter with the filtered or full list
        adapter.updateData(filteredList);
    }
}