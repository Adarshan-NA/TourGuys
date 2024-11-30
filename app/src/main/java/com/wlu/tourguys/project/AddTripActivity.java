package com.wlu.tourguys.project;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTripActivity extends AppCompatActivity {

    private EditText sourceCountryField, sourceCityField, destinationCountryField, destinationCityField;
    private EditText startDateField, endDateField, numDaysField, numPeopleField, maleCountField, femaleCountField, budgetField;
    private Button addTripButton;

    private DatabaseReference databaseReference;

    private Calendar startDateCalendar = Calendar.getInstance();
    private Calendar endDateCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        // Initialize views
        sourceCountryField = findViewById(R.id.sourceCountry);
        sourceCityField = findViewById(R.id.sourceCity);
        destinationCountryField = findViewById(R.id.destinationCountry);
        destinationCityField = findViewById(R.id.destinationCity);
        startDateField = findViewById(R.id.startDate);
        endDateField = findViewById(R.id.endDate);
        numDaysField = findViewById(R.id.numDays);
        numPeopleField = findViewById(R.id.numPeople);
        maleCountField = findViewById(R.id.maleCount);
        femaleCountField = findViewById(R.id.femaleCount);
        budgetField = findViewById(R.id.budget);

        addTripButton = findViewById(R.id.addTripButton);

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Trips");

        // Date pickers for start date and end date
        startDateField.setOnClickListener(v -> showDatePickerDialog(startDateField, startDateCalendar));
        endDateField.setOnClickListener(v -> showDatePickerDialog(endDateField, endDateCalendar));

        // Set up button listener
        addTripButton.setOnClickListener(v -> addTripToDatabase());

        // Handle BottomNavigationView actions
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_add_trip) {
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

        // Set the current selection to Add Trip
        bottomNavigationView.setSelectedItemId(R.id.nav_add_trip);
    }

    private void addTripToDatabase() {
        String sourceCountry = sourceCountryField.getText().toString().trim();
        String sourceCity = sourceCityField.getText().toString().trim();
        String destinationCountry = destinationCountryField.getText().toString().trim();
        String destinationCity = destinationCityField.getText().toString().trim();
        String startDate = startDateField.getText().toString().trim();
        String endDate = endDateField.getText().toString().trim();
        String numPeople = numPeopleField.getText().toString().trim();
        String maleCount = maleCountField.getText().toString().trim();
        String femaleCount = femaleCountField.getText().toString().trim();
        String budget = budgetField.getText().toString().trim();

        // Validate input
        if (!sourceCountry.matches("[a-zA-Z]+") || !destinationCountry.matches("[a-zA-Z]+")) {
            Toast.makeText(this, "Country names must contain only letters", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(sourceCountry) || TextUtils.isEmpty(sourceCity) || TextUtils.isEmpty(destinationCountry) ||
                TextUtils.isEmpty(destinationCity) || TextUtils.isEmpty(startDate) || TextUtils.isEmpty(endDate) ||
                TextUtils.isEmpty(numPeople) || TextUtils.isEmpty(maleCount) || TextUtils.isEmpty(femaleCount) ||
                TextUtils.isEmpty(budget)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Calculate number of days
        String numDays = calculateNumberOfDays(startDate, endDate);
        if (numDays == null) {
            Toast.makeText(this, "Invalid date range", Toast.LENGTH_SHORT).show();
            return;
        }
        numDaysField.setText(numDays);

        Destination trip = new Destination(sourceCountry, startDate + " to " + endDate, numPeople, destinationCity, destinationCountry);

        String tripId = databaseReference.push().getKey();
        if (tripId != null) {
            databaseReference.child(tripId).setValue(trip);
            Toast.makeText(this, "Trip added successfully", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void showDatePickerDialog(EditText field, Calendar calendar) {
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            field.setText(sdf.format(calendar.getTime()));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private String calculateNumberOfDays(String startDate, String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            long start = sdf.parse(startDate).getTime();
            long end = sdf.parse(endDate).getTime();
            long days = (end - start) / (1000 * 60 * 60 * 24);
            return days >= 0 ? String.valueOf(days + 1) : null;
        } catch (ParseException e) {
            return null;
        }
    }
}