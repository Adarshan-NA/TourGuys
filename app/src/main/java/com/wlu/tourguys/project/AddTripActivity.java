package com.wlu.tourguys.project;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddTripActivity extends AppCompatActivity {

    private EditText sourceCountryField, sourceCityField, destinationCountryField, destinationCityField;
    private EditText startDateField, endDateField, numDaysField, numPeopleField, maleCountField, femaleCountField, budgetField;
    private Button addTripButton;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        // Initialize fields
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

        // Set input types for text or numbers only
        sourceCountryField.setInputType(InputType.TYPE_CLASS_TEXT); // Only text
        destinationCountryField.setInputType(InputType.TYPE_CLASS_TEXT); // Only text
        numPeopleField.setInputType(InputType.TYPE_CLASS_NUMBER); // Only numbers
        maleCountField.setInputType(InputType.TYPE_CLASS_NUMBER); // Only numbers
        femaleCountField.setInputType(InputType.TYPE_CLASS_NUMBER); // Only numbers
        budgetField.setInputType(InputType.TYPE_CLASS_NUMBER); // Only numbers

        // Initialize Firebase Database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Trips");

        // Date picker setup
        startDateField.setOnClickListener(v -> showDatePickerDialog(startDateField));
        endDateField.setOnClickListener(v -> showDatePickerDialog(endDateField));

        addTripButton.setOnClickListener(v -> addTripToDatabase());
    }

    private void showDatePickerDialog(EditText targetField) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (DatePicker view, int selectedYear, int selectedMonth, int selectedDay) -> {
                    // Format the selected date
                    String formattedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                    targetField.setText(formattedDate);

                    // Automatically calculate number of days if both dates are filled
                    calculateNumberOfDays();
                },
                year, month, day);
        datePickerDialog.show();
    }

    private void calculateNumberOfDays() {
        String startDate = startDateField.getText().toString().trim();
        String endDate = endDateField.getText().toString().trim();

        if (!TextUtils.isEmpty(startDate) && !TextUtils.isEmpty(endDate)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date start = sdf.parse(startDate);
                Date end = sdf.parse(endDate);

                if (start != null && end != null && !end.before(start)) {
                    long difference = end.getTime() - start.getTime();
                    long days = difference / (1000 * 60 * 60 * 24); // Convert milliseconds to days
                    numDaysField.setText(String.valueOf(days));
                } else {
                    numDaysField.setText("");
                    Toast.makeText(this, "End date must be after start date", Toast.LENGTH_SHORT).show();
                }
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(this, "Invalid date format", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addTripToDatabase() {
        String sourceCountry = sourceCountryField.getText().toString().trim();
        String sourceCity = sourceCityField.getText().toString().trim();
        String destinationCountry = destinationCountryField.getText().toString().trim();
        String destinationCity = destinationCityField.getText().toString().trim();
        String startDate = startDateField.getText().toString().trim();
        String endDate = endDateField.getText().toString().trim();
        String numDays = numDaysField.getText().toString().trim();
        String numPeople = numPeopleField.getText().toString().trim();
        String maleCount = maleCountField.getText().toString().trim();
        String femaleCount = femaleCountField.getText().toString().trim();
        String budget = budgetField.getText().toString().trim();

        // Validation check
        if (TextUtils.isEmpty(sourceCountry) || TextUtils.isEmpty(sourceCity) || TextUtils.isEmpty(destinationCountry) ||
                TextUtils.isEmpty(destinationCity) || TextUtils.isEmpty(startDate) || TextUtils.isEmpty(endDate) ||
                TextUtils.isEmpty(numDays) || TextUtils.isEmpty(numPeople) || TextUtils.isEmpty(maleCount) ||
                TextUtils.isEmpty(femaleCount) || TextUtils.isEmpty(budget)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create trip object
        Trip trip = new Trip(
                sourceCountry, sourceCity, destinationCountry, destinationCity,
                startDate, endDate, Integer.parseInt(numDays),
                Integer.parseInt(numPeople), Integer.parseInt(maleCount),
                Integer.parseInt(femaleCount), Double.parseDouble(budget)
        );

        // Save trip to database
        String tripId = databaseReference.push().getKey();
        if (tripId != null) {
            databaseReference.child(tripId).setValue(trip);
            Toast.makeText(this, "Trip added successfully", Toast.LENGTH_SHORT).show();

            // Navigate back to MainActivity (Home page)
            Intent intent = new Intent(AddTripActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clear the back stack
            startActivity(intent);
            finish(); // Close AddTripActivity
        }
    }
}