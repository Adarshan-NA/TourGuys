package com.wlu.tourguys.project;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProfileTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private FirebaseAuth mockFirebaseAuth;

    @Mock
    private FirebaseUser mockFirebaseUser;

    @Mock
    private DatabaseReference mockDatabaseReference;

    @Mock
    private DataSnapshot mockDataSnapshot;

    @Mock
    private Profile mockActivity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        // Mock Firebase Auth
        when(mockFirebaseAuth.getCurrentUser()).thenReturn(mockFirebaseUser);

        // Mock FirebaseUser
        when(mockFirebaseUser.getUid()).thenReturn("mockUserId");

        // Mock Firebase Database Reference
        when(mockDatabaseReference.child("mockUserId")).thenReturn(mockDatabaseReference);
    }

    @Test
    public void testLoadUserData_UserExists() {
        // Arrange
        doAnswer(invocation -> {
            ValueEventListener listener = invocation.getArgument(0);
            when(mockDataSnapshot.exists()).thenReturn(true);
            when(mockDataSnapshot.child("name").getValue(String.class)).thenReturn("Test User");
            when(mockDataSnapshot.child("city").getValue(String.class)).thenReturn("Waterloo");
            when(mockDataSnapshot.child("country").getValue(String.class)).thenReturn("Canada");
            listener.onDataChange(mockDataSnapshot);
            return null;
        }).when(mockDatabaseReference).addListenerForSingleValueEvent(any(ValueEventListener.class));

        // Act
        Profile activity = new Profile();
        activity.firebaseAuth = mockFirebaseAuth;
        activity.currentUser = mockFirebaseUser;
        activity.databaseReference = mockDatabaseReference;
        activity.loadUserData();

        // Assert
        verify(mockDatabaseReference).addListenerForSingleValueEvent(any(ValueEventListener.class));
        assert(activity.userNameTextView.getText().toString().equals("Test User"));
        assert(activity.locationTextView.getText().toString().equals("Waterloo, Canada"));
    }

    @Test
    public void testLoadUserData_UserNotExists() {
        // Arrange
        doAnswer(invocation -> {
            ValueEventListener listener = invocation.getArgument(0);
            when(mockDataSnapshot.exists()).thenReturn(false);
            listener.onDataChange(mockDataSnapshot);
            return null;
        }).when(mockDatabaseReference).addListenerForSingleValueEvent(any(ValueEventListener.class));

        // Act
        Profile activity = new Profile();
        activity.firebaseAuth = mockFirebaseAuth;
        activity.currentUser = mockFirebaseUser;
        activity.databaseReference = mockDatabaseReference;
        activity.loadUserData();

        // Assert
        verify(mockDatabaseReference).addListenerForSingleValueEvent(any(ValueEventListener.class));
        assert(activity.userNameTextView.getText().toString().equals("Unknown"));
        assert(activity.locationTextView.getText().toString().equals("Unknown City, Unknown Country"));
    }

    @Test
    public void testProfileSettingsButtonClick_NavigatesToProfileSettingsActivity() {
        // Arrange
        Profile activity = Mockito.spy(new Profile());

        doNothing().when(activity).startActivity(any(Intent.class));
        doNothing().when(activity).finish();

        // Act
        activity.profileSettingsButton.performClick();

        // Assert
        verify(activity).startActivity(any(Intent.class));
    }

    @Test
    public void testLogoutButtonClick_NavigatesToLoginActivity() {
        // Arrange
        Profile activity = Mockito.spy(new Profile());

        doNothing().when(activity).startActivity(any(Intent.class));
        doNothing().when(activity).finish();

        // Act
        activity.logoutButton.performClick();

        // Assert
        verify(activity).startActivity(any(Intent.class));
    }
}
