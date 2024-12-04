package com.wlu.tourguys.project;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class AddTripActivity extends AppCompatActivity {

    private EditText sourceCountryField, sourceCityField, destinationCountryField, destinationCityField;
    private EditText startDateField, endDateField, numDaysField, numPeopleField, maleCountField, femaleCountField, budgetField;
    private Button addTripButton;

    protected FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private DatabaseReference databaseReference, databaseReferenceUser;

    private Calendar startDateCalendar = Calendar.getInstance();
    private Calendar endDateCalendar = Calendar.getInstance();

    // User details
    public String userName, userEmail, userPhone;
    ImageView backButton;

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

        backButton = findViewById(R.id.back_button);

        addTripButton = findViewById(R.id.addTripButton);

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Trips");

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        databaseReferenceUser = database.getReference("Users");

        // Load user data
        loadUserData();

        // Date pickers for start date and end date
        startDateField.setOnClickListener(v -> showDatePickerDialog(startDateField, startDateCalendar));
        endDateField.setOnClickListener(v -> {
            showDatePickerDialog(endDateField, endDateCalendar);
            calculateNumDays();
        });

        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(AddTripActivity.this, MainActivity.class);
            startActivity(intent);
        });

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

    public void loadUserData(){
        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated. Please log in.", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();

        databaseReferenceUser.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String mobile = snapshot.child("phone").getValue(String.class);
                    userName = name;
                    userEmail = email;
                    userPhone = mobile;
                } else {
                    Toast.makeText(AddTripActivity.this, "No user data found.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddTripActivity.this, "Failed to load user data.", Toast.LENGTH_SHORT).show();
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
        String numDaysStr = numDaysField.getText().toString().trim();
        String numPeopleStr = numPeopleField.getText().toString().trim();
        String maleCountStr = maleCountField.getText().toString().trim();
        String femaleCountStr = femaleCountField.getText().toString().trim();
        String budgetStr = budgetField.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(sourceCountry) || TextUtils.isEmpty(sourceCity) ||
                TextUtils.isEmpty(destinationCountry) || TextUtils.isEmpty(destinationCity) ||
                TextUtils.isEmpty(startDate) || TextUtils.isEmpty(endDate) ||
                TextUtils.isEmpty(numDaysStr) || TextUtils.isEmpty(numPeopleStr) ||
                TextUtils.isEmpty(maleCountStr) || TextUtils.isEmpty(femaleCountStr) || TextUtils.isEmpty(budgetStr)) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new trip object with user details
        HashMap<String, String> trip = new HashMap<>();
        trip.put("sourceCountry", sourceCountry);
        trip.put("sourceCity", sourceCity);
        trip.put("destinationCountry", destinationCountry);
        trip.put("destinationCity", destinationCity);
        trip.put("startDate", startDate);
        trip.put("endDate", endDate);
        trip.put("numDays", numDaysStr);
        trip.put("numPeople", numPeopleStr);
        trip.put("maleCount", maleCountStr);
        trip.put("femaleCount", femaleCountStr);
        trip.put("budget", budgetStr);
        trip.put("userName", userName);  // Include user details
        trip.put("userEmail", userEmail);
        trip.put("userPhone", userPhone);

        // Push to Firebase
        String tripId = databaseReference.push().getKey();
        if (tripId != null) {
            databaseReference.child(tripId).setValue(trip)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddTripActivity.this, "Trip Added Successfully!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddTripActivity.this, MainActivity.class)); // Redirect to MainActivity
                        } else {
                            Toast.makeText(AddTripActivity.this, "Failed to add trip", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void showDatePickerDialog(EditText dateField, Calendar calendar) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateDateField(dateField, calendar);
                    calculateNumDays();
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void updateDateField(EditText dateField, Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        dateField.setText(sdf.format(calendar.getTime()));
    }



//    private void calculateNumDays() {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//        try {
//            String startDate = startDateField.getText().toString();
//            String endDate = endDateField.getText().toString();
//
//            if (!TextUtils.isEmpty(startDate) && !TextUtils.isEmpty(endDate)) {
//                long start = sdf.parse(startDate).getTime();
//                long end = sdf.parse(endDate).getTime();
//                long diff = end - start;
//                int days = (int) TimeUnit.MILLISECONDS.toDays(diff) + 1;
//                numDaysField.setText(String.valueOf(days));
//            }
//        } catch (ParseException e) {
//            Toast.makeText(this, "Invalid dates selected", Toast.LENGTH_SHORT).show();
//        }
//    }

    private void calculateNumDays() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            String startDate = startDateField.getText().toString();
            String endDate = endDateField.getText().toString();

            if (!TextUtils.isEmpty(startDate) && !TextUtils.isEmpty(endDate)) {
                long start = sdf.parse(startDate).getTime();
                long end = sdf.parse(endDate).getTime();

                if (end < start) {
                    Toast.makeText(this, "End date cannot be less than start date", Toast.LENGTH_SHORT).show();
                    endDateField.setText(""); // Clear the invalid end date
                    numDaysField.setText(""); // Clear the number of days
                    return;
                }

                long diff = end - start;
                int days = (int) TimeUnit.MILLISECONDS.toDays(diff) + 1;
                numDaysField.setText(String.valueOf(days));
            }
        } catch (ParseException e) {
            Toast.makeText(this, "Invalid dates selected", Toast.LENGTH_SHORT).show();
        }
    }
}