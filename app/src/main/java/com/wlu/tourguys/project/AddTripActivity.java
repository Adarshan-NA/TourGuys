package com.wlu.tourguys.project;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddTripActivity extends AppCompatActivity {

    private EditText sourceCountryField, sourceCityField, destinationCountryField, destinationCityField;
    private EditText startDateField, endDateField, numDaysField, numPeopleField, maleCountField, femaleCountField, budgetField;
    private Button addTripButton;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

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

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Trips");

        addTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTripToDatabase();
            }
        });
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

        if (TextUtils.isEmpty(sourceCountry) || TextUtils.isEmpty(sourceCity) || TextUtils.isEmpty(destinationCountry) ||
                TextUtils.isEmpty(destinationCity) || TextUtils.isEmpty(startDate) || TextUtils.isEmpty(endDate) ||
                TextUtils.isEmpty(numDays) || TextUtils.isEmpty(numPeople) || TextUtils.isEmpty(maleCount) ||
                TextUtils.isEmpty(femaleCount) || TextUtils.isEmpty(budget)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Destination trip = new Destination(sourceCountry, startDate + " to " + endDate, numPeople, destinationCity, destinationCountry);

        String tripId = databaseReference.push().getKey();
        if (tripId != null) {
            databaseReference.child(tripId).setValue(trip);
            Toast.makeText(this, "Trip added successfully", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
