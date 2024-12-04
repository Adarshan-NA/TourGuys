package com.wlu.tourguys.project;

import static org.mockito.Mockito.*;

import android.widget.EditText;
import android.widget.Toast;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class AddTripActivityTest {

    private AddTripActivity addTripActivity;
    private FirebaseAuth mockAuth;
    private FirebaseUser mockUser;
    private DatabaseReference mockDatabaseReference;

    @Before
    public void setUp() {
        // Initialize mocks
        mockAuth = mock(FirebaseAuth.class);
        mockUser = mock(FirebaseUser.class);
        mockDatabaseReference = mock(DatabaseReference.class);

        // Create an Activity Scenario
        ActivityScenario<AddTripActivity> scenario = ActivityScenario.launch(AddTripActivity.class);

        // Use the activity within the scenario to set up mocks
        scenario.onActivity(activity -> {
            addTripActivity = activity; // Initialize the AddTripActivity
            addTripActivity.mAuth = mockAuth;
            addTripActivity.mDatabaseReference = mockDatabaseReference;
        });

        // Set the mocks
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        when(mockDatabaseReference.child(anyString())).thenReturn(mockDatabaseReference);
    }

    @Test
    public void testAddTripToDatabase_Success() {
        // Arrange - mock valid input data
        EditText sourceCountryField = mock(EditText.class);
        when(sourceCountryField.getText()).thenReturn("USA");

        // Simulate a successful Firebase operation
        doNothing().when(mockDatabaseReference).setValue(any());

        // Act - trigger the method to add the trip
        addTripActivity.addTripToDatabase();

        // Assert - verify if the Firebase call was made successfully
        verify(mockDatabaseReference, times(1)).setValue(any());
    }

    @Test
    public void testAddTripToDatabase_Failure() {
        // Arrange - simulate a failure
        doThrow(new RuntimeException("Firebase error")).when(mockDatabaseReference).setValue(any());

        // Act - trigger the method to add the trip
        addTripActivity.addTripToDatabase();

        // Assert - verify if Toast shows error
        verify(addTripActivity, times(1)).showToast("Failed to add trip");
    }
}
