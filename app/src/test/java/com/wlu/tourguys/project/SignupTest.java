package com.wlu.tourguys.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

@RunWith(AndroidJUnit4.class)
public class SignupTest {

    private FirebaseAuth mockFirebaseAuth;
    private DatabaseReference mockDatabaseReference;
    private FirebaseUser mockFirebaseUser;

    @Before
    public void setUp() {
        mockFirebaseAuth = Mockito.mock(FirebaseAuth.class);
        mockDatabaseReference = Mockito.mock(DatabaseReference.class);
        mockFirebaseUser = Mockito.mock(FirebaseUser.class);

        when(mockFirebaseAuth.getCurrentUser()).thenReturn(mockFirebaseUser);
        when(mockFirebaseUser.getUid()).thenReturn("testUserId");
    }

    @Test
    public void testValidateInputs_validInputs() {
        try (ActivityScenario<signUp> scenario = ActivityScenario.launch(signUp.class)) {
            scenario.onActivity(activity -> {
                // Set up test input
                EditText nameField = activity.findViewById(R.id.signupNameEditText);
                EditText emailField = activity.findViewById(R.id.signupEmailEditText);
                EditText passwordField = activity.findViewById(R.id.signupPasswordEditText);
                EditText confirmPasswordField = activity.findViewById(R.id.signupPasswordConfirmEditText);
                EditText phoneField = activity.findViewById(R.id.signupPhoneEditText);
                CheckBox robotCheckBox = activity.findViewById(R.id.not_a_robot_checkbox);

                nameField.setText("John Doe");
                emailField.setText("johndoe@example.com");
                passwordField.setText("password123");
                confirmPasswordField.setText("password123");
                phoneField.setText("1234567890");
                robotCheckBox.setChecked(true);

                boolean isValid = activity.validateInputs(
                        "johndoe@example.com",
                        "password123",
                        "password123",
                        "John Doe",
                        "1234567890"
                );

                assert isValid;
            });
        }
    }

    @Test
    public void testValidateInputs_invalidEmail() {
        try (ActivityScenario<signUp> scenario = ActivityScenario.launch(signUp.class)) {
            scenario.onActivity(activity -> {
                boolean isValid = activity.validateInputs(
                        "invalid-email",
                        "password123",
                        "password123",
                        "John Doe",
                        "1234567890"
                );

                assert !isValid;
            });
        }
    }

    @Test
    public void testHandlePasswordToggle() {
        try (ActivityScenario<signUp> scenario = ActivityScenario.launch(signUp.class)) {
            scenario.onActivity(activity -> {
                EditText passwordField = activity.findViewById(R.id.signupPasswordEditText);

                MotionEvent mockEvent = mock(MotionEvent.class);
                when(mockEvent.getAction()).thenReturn(MotionEvent.ACTION_UP);
                when(mockEvent.getRawX()).thenReturn(100f);

                boolean result = activity.handlePasswordToggle(mockEvent, passwordField);
                assert result;
            });
        }
    }



    @Test
    public void testSaveInstanceState() {
        try (ActivityScenario<signUp> scenario = ActivityScenario.launch(signUp.class)) {
            scenario.onActivity(activity -> {
                Bundle outState = new Bundle();
                activity.onSaveInstanceState(outState);

                assert outState.containsKey("signupName");
                assert outState.containsKey("signupEmail");
            });
        }
    }

    @Test
    public void testRestoreInstanceState() {
        try (ActivityScenario<signUp> scenario = ActivityScenario.launch(signUp.class)) {
            scenario.onActivity(activity -> {
                Bundle savedInstanceState = new Bundle();
                savedInstanceState.putString("signupName", "John Doe");
                savedInstanceState.putString("signupEmail", "johndoe@example.com");

                activity.onRestoreInstanceState(savedInstanceState);

                EditText nameField = activity.findViewById(R.id.signupNameEditText);
                EditText emailField = activity.findViewById(R.id.signupEmailEditText);

                assert nameField.getText().toString().equals("John Doe");
                assert emailField.getText().toString().equals("johndoe@example.com");
            });
        }
    }
}
