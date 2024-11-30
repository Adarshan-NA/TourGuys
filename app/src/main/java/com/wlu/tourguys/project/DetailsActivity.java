package com.wlu.tourguys.project;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailsActivity extends AppCompatActivity {

    private TextView tvDestinationName, tvLocation, tvDuration, tvTravelerName, tvTravelDates, tvSource, tvTotalPeople, tvMaleCount, tvFemaleCount, tvBudget;

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

        // Get data from intent
        Destination destination = (Destination) getIntent().getSerializableExtra("destination");

        // Populate the views
        if (destination != null) {
            tvDestinationName.setText(String.valueOf(destination.getName()));
            tvLocation.setText(String.valueOf(destination.getLocation()));
            tvDuration.setText(String.valueOf(destination.getDuration()));
            tvTravelerName.setText(String.valueOf(destination.getName()));
            tvTravelDates.setText(String.valueOf(destination.getTravelDates()));
           tvSource.setText(String.valueOf(destination.getSource()));
            tvTotalPeople.setText(String.valueOf(destination.getCount()));
            tvMaleCount.setText(String.valueOf(destination.getMaleCount()));
           tvFemaleCount.setText(String.valueOf(destination.getFemaleCount()));
          tvBudget.setText(String.valueOf(destination.getBudget()));
        }
    }
}