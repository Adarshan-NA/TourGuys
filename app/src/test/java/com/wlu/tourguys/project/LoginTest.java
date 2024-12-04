package com.wlu.tourguys.project;

import static org.mockito.Mockito.*;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Patterns;

import androidx.test.core.app.ApplicationProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

public class LoginTest {

    @Mock
    public FirebaseAuth mockFirebaseAuth;
    @Mock
    public SharedPreferences mockSharedPreferences;
    @Mock
    public SharedPreferences.Editor mockEditor;
    @Mock
    public Task<AuthResult> mockAuthResultTask;

    private Context context;
    private Login loginActivity;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        context = ApplicationProvider.getApplicationContext();

        // Mock SharedPreferences behavior
        when(mockSharedPreferences.edit()).thenReturn(mockEditor);
        when(mockEditor.putBoolean(anyString(), anyBoolean())).thenReturn(mockEditor);
        when(mockEditor.putString(anyString(), anyString())).thenReturn(mockEditor);

        loginActivity = new Login();
        loginActivity.firebaseAuth = mockFirebaseAuth;
        loginActivity.sharedPreferences = mockSharedPreferences;

        // Mock TextUtils static methods
        mockStatic(TextUtils.class);
        when(TextUtils.isEmpty(anyString())).thenAnswer(invocation -> {
            CharSequence arg = invocation.getArgument(0);
            return arg == null || arg.length() == 0;
        });

        // Mock Patterns static methods
        mockStatic(Patterns.class);
        when(Patterns.EMAIL_ADDRESS.matcher(anyString())).thenAnswer(invocation -> {
            String arg = invocation.getArgument(0);
            return arg != null && arg.contains("@");
        });
    }

    @Test
    public void validateInputs_validInputs_returnsTrue() {
        assertTrue(loginActivity.validateInputs("user@example.com", "password123"));
    }

    @Test
    public void validateInputs_emptyEmail_returnsFalse() {
        assertFalse(loginActivity.validateInputs("", "password123"));
    }

    @Test
    public void validateInputs_invalidEmail_returnsFalse() {
        assertFalse(loginActivity.validateInputs("invalid-email", "password123"));
    }

    @Test
    public void validateInputs_emptyPassword_returnsFalse() {
        assertFalse(loginActivity.validateInputs("user@example.com", ""));
    }

    @Test
    public void signIn_successfulLogin_savesLoginState() {
        when(mockAuthResultTask.isSuccessful()).thenReturn(true);
        doAnswer(invocation -> {
            OnCompleteListener<AuthResult> listener = invocation.getArgument(0);
            listener.onComplete(mockAuthResultTask);
            return null;
        }).when(mockFirebaseAuth).signInWithEmailAndPassword(anyString(), anyString());

        loginActivity.signIn("user@example.com", "password123");

        verify(mockEditor).putBoolean("isLoggedIn", true);
        verify(mockEditor).putString("userEmail", "user@example.com");
        verify(mockEditor).apply();
    }

    @Test
    public void signIn_failedLogin_showsToast() {
        when(mockAuthResultTask.isSuccessful()).thenReturn(false);
        doAnswer(invocation -> {
            OnCompleteListener<AuthResult> listener = invocation.getArgument(0);
            listener.onComplete(mockAuthResultTask);
            return null;
        }).when(mockFirebaseAuth).signInWithEmailAndPassword(anyString(), anyString());

        loginActivity.signIn("user@example.com", "wrongpassword");

        verify(mockEditor, never()).putBoolean(anyString(), anyBoolean());
        verify(mockEditor, never()).apply();
    }

    @Test
    public void togglePasswordVisibility_togglesStateCorrectly() {
        loginActivity.isPasswordVisible = false;
        loginActivity.togglePasswordVisibility();
        assertTrue(loginActivity.isPasswordVisible);

        loginActivity.togglePasswordVisibility();
        assertFalse(loginActivity.isPasswordVisible);
    }

    @Test
    public void saveLoginState_savesCorrectValues() {
        loginActivity.saveLoginState("user@example.com");
        verify(mockEditor).putBoolean("isLoggedIn", true);
        verify(mockEditor).putString("userEmail", "user@example.com");
        verify(mockEditor).apply();
    }

    @Test
    public void saveEmailToPreferences_savesEmailCorrectly() {
        loginActivity.saveEmailToPreferences("user@example.com");
        verify(mockEditor).putString("DefaultEmail", "user@example.com");
        verify(mockEditor).apply();
    }
}
