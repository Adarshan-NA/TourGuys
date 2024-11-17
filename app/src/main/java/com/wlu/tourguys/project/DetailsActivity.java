package com.wlu.tourguys.project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DetailsActivity extends AppCompatActivity {

    private TextView destinationName, destinationLocation, daysCount, travelerName, travelDates, source, totalPeople, maleCount, femaleCount, budget;
    private Button contactInfoButton;
    private ImageButton backButton;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Initialize views
        destinationName = findViewById(R.id.tv_destination_name);
        destinationLocation = findViewById(R.id.tv_location);
        daysCount = findViewById(R.id.tv_duration);
        travelerName = findViewById(R.id.tv_traveler_name);
        travelDates = findViewById(R.id.tv_travel_dates);
        source = findViewById(R.id.tv_source);
        totalPeople = findViewById(R.id.tv_total_people);
        maleCount = findViewById(R.id.tv_male_count);
        femaleCount = findViewById(R.id.tv_female_count);
        budget = findViewById(R.id.tv_budget);
        contactInfoButton = findViewById(R.id.btn_contact_info);
        backButton = findViewById(R.id.back_button);
        bottomNavigation = findViewById(R.id.bottom_navigation);

        // Get data from intent
        Intent intent = getIntent();
        if (intent != null) {
            String name = intent.getStringExtra("TRAVELER_NAME");
            String locationName = intent.getStringExtra("DESTINATION_NAME");
            String locationCountry = intent.getStringExtra("DESTINATION_LOCATION");
            String duration = intent.getStringExtra("DAYS_COUNT") + " days";
            String dates = intent.getStringExtra("TRAVEL_DATES");
            String sourceText = intent.getStringExtra("SOURCE");
            String totalPeopleText = intent.getStringExtra("TOTAL_PEOPLE");
            String maleCountText = intent.getStringExtra("MALE_COUNT");
            String femaleCountText = intent.getStringExtra("FEMALE_COUNT");
            String budgetText = intent.getStringExtra("BUDGET");

            destinationName.setText(locationName); // Mount Everest
            destinationLocation.setText(locationCountry); // Nepal
            daysCount.setText(duration); // "5 days"
            travelerName.setText(name); // Ann Press under "Name" label
            travelDates.setText(dates); // December 15 - December 20
            source.setText(sourceText); // Source location
            totalPeople.setText(totalPeopleText); // 7
            maleCount.setText(maleCountText); // 3
            femaleCount.setText(femaleCountText); // 3
            budget.setText(budgetText); // $360
        }

        // Handle back button click
        backButton.setOnClickListener(v -> finish());

        // Handle contact info button click
        contactInfoButton.setOnClickListener(view -> {
            // Placeholder: Open a dialog or activity for contact details
        });

        // Set up Bottom Navigation View
        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                // Handle home action
                return true;
            } else if (item.getItemId() == R.id.add_trip) {
                // Handle add trip action
                return true;
            } else if (item.getItemId() == R.id.guide) {
                // Handle guide action
                return true;
            } else if (item.getItemId() == R.id.profile) {
                // Handle profile action
                return true;
            }
            return false;
        });
    }
}