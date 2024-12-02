package com.wlu.tourguys.project;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;

public class ProfileSettingsActivityTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    FirebaseAuth mockFirebaseAuth;

    @Mock
    FirebaseUser mockFirebaseUser;

    @Mock
    FirebaseDatabase mockFirebaseDatabase;

    @Mock
    DatabaseReference mockDatabaseReference;

    @Mock
    StorageReference mockStorageReference;

    private ProfileSettingsActivity activity;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Mock Firebase Auth
        when(mockFirebaseAuth.getCurrentUser()).thenReturn(mockFirebaseUser);
        when(mockFirebaseUser.getUid()).thenReturn("mockUserId");

        // Mock Firebase Database
        when(mockFirebaseDatabase.getReference(anyString())).thenReturn(mockDatabaseReference);
        when(mockDatabaseReference.child(anyString())).thenReturn(mockDatabaseReference);

        // Mock Firebase Storage
        mockStorageReference = mock(StorageReference.class);
    }

    @Test
    public void testLoadUserData_Success() {
        // Arrange
        ProfileSettingsActivity activity = new ProfileSettingsActivity();
        activity.firebaseAuth = mockFirebaseAuth;
        activity.databaseReference = mockDatabaseReference;

        DataSnapshot mockDataSnapshot = mock(DataSnapshot.class);
        when(mockDataSnapshot.exists()).thenReturn(true);
        when(mockDataSnapshot.child("name").getValue(String.class)).thenReturn("Test User");
        when(mockDataSnapshot.child("email").getValue(String.class)).thenReturn("test@example.com");

        doAnswer(invocation -> {
            ValueEventListener listener = invocation.getArgument(0);
            listener.onDataChange(mockDataSnapshot);
            return null;
        }).when(mockDatabaseReference).addListenerForSingleValueEvent(any(ValueEventListener.class));

        // Act
        activity.loadUserData();

        // Assert
        Mockito.verify(mockDatabaseReference).addListenerForSingleValueEvent(any(ValueEventListener.class));
    }

    @Test
    public void testUploadProfileImage_Success() {
        // Arrange
        ProfileSettingsActivity activity = new ProfileSettingsActivity();
        activity.firebaseAuth = mockFirebaseAuth;
        activity.databaseReference = mockDatabaseReference;

        Bitmap mockBitmap = mock(Bitmap.class);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        when(mockBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)).thenReturn(true);

        // Act
        activity.uploadProfileImage(mockBitmap);

        // Assert
        // Verify if StorageReference was called to upload the image
        Mockito.verify(mockStorageReference, Mockito.times(1)).putBytes(any(byte[].class));
    }

    @Test
    public void testSaveUserData_Success() {
        // Arrange
        ProfileSettingsActivity activity = new ProfileSettingsActivity();
        activity.firebaseAuth = mockFirebaseAuth;
        activity.databaseReference = mockDatabaseReference;

        String testName = "Test User";
        String testCity = "Test City";

        // Act
        activity.saveUserData();

        // Assert
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(mockDatabaseReference.child("mockUserId").child("name")).setValue(captor.capture());
        assertEquals(testName, captor.getValue());
    }

    @Test
    public void testDeleteOldProfileImage_Success() {
        // Arrange
        ProfileSettingsActivity activity = new ProfileSettingsActivity();
        activity.firebaseAuth = mockFirebaseAuth;
        activity.databaseReference = mockDatabaseReference;

        doAnswer(invocation -> {
            // Simulate successful deletion
            return null;
        }).when(mockStorageReference).delete();

        // Act
        activity.deleteOldProfileImage();

        // Assert
        Mockito.verify(mockStorageReference, Mockito.times(1)).delete();
    }
}

