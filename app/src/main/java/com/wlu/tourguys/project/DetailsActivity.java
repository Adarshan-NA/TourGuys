package com.wlu.tourguys.project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DetailsActivity extends AppCompatActivity {

    private TextView tvDestinationName, tvLocation, tvDuration, tvTravelerName, tvTravelDates, tvSource, tvTotalPeople, tvMaleCount, tvFemaleCount, tvBudget;
    private BottomNavigationView bottomNavigation;
    private Button btnContactInfo;
    ImageView backButton;
    String userEmail, userPhone, userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Initialize Views
        tvDestinationName = findViewById(R.id.tv_destination_name);
        tvLocation = findViewById(R.id.tv_location);
        tvDuration = findViewById(R.id.tv_duration);
        tvTravelerName = findViewById(R.id.tv_traveler_name);
        tvTravelDates = findViewById(R.id.tv_travel_dates);
        tvSource = findViewById(R.id.tv_source);
        tvTotalPeople = findViewById(R.id.tv_total_people);
        tvMaleCount = findViewById(R.id.tv_male_count);
        tvFemaleCount = findViewById(R.id.tv_female_count);
        tvBudget = findViewById(R.id.tv_budget);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        btnContactInfo = findViewById(R.id.btn_contact_info);
        backButton = findViewById(R.id.back_button);

        // Get data from intent
        Destination destination = (Destination) getIntent().getSerializableExtra("destination");

        // Populate the views
        if (destination != null) {
            // Update the views with the data from the destination object

            tvDestinationName.setText(destination.getDestinationCountry());
            tvLocation.setText(destination.getDestinationCity());
            tvDuration.setText(String.valueOf((destination.getNumDays())) + " days");
            tvTravelerName.setText(destination.getUserName());
            tvTravelDates.setText((destination.getStartDate() + " to " + destination.getEndDate()));
            tvSource.setText(destination.getSourceCity() + ", " + destination.getSourceCountry());
            tvTotalPeople.setText(String.valueOf(destination.getNumPeople()));
            tvMaleCount.setText(String.valueOf(destination.getMaleCount()));
            tvFemaleCount.setText(String.valueOf(destination.getFemaleCount()));
            tvBudget.setText(String.valueOf(destination.getBudget()));
            Log.i("DetailsActivity",destination.getUserEmail()
            );
            Log.i("DetailsActivity",destination.getUserPhone()
            );
            userEmail = destination.getUserEmail();
            userPhone = destination.getUserPhone();
            userName = destination.getUserName();
        }

        // Set up button to navigate to ContactDetails
        btnContactInfo.setOnClickListener(view -> {
            Intent intent = new Intent(DetailsActivity.this, ContactDetailsActivity.class);
            intent.putExtra("userEmail", userEmail);
            intent.putExtra("userPhone", userPhone);
            intent.putExtra("userName", userName);
            startActivity(intent);
        });

        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(DetailsActivity.this, MainActivity.class);
            startActivity(intent);
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
}