package com.wlu.tourguys.project;

import android.content.Intent;
import android.widget.TextView;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ContactDetailsActivityTest {

    @Rule
    public ActivityTestRule<ContactDetailsActivity> activityRule = new ActivityTestRule<>(ContactDetailsActivity.class);

    private ContactDetailsActivity activity;

    @Before
    public void setUp() {
        activity = activityRule.getActivity();
    }

    @Test
    public void testDataDisplay() {
        // Simulating Intent data
        Intent intent = new Intent();
        intent.putExtra("userName", "John Doe");
        intent.putExtra("userEmail", "john@example.com");
        intent.putExtra("userPhone", "1234567890");
        activity.getIntent().putExtras(intent);

        activity.runOnUiThread(() -> {
            TextView profileName = activity.findViewById(R.id.profileName);
            TextView emailText = activity.findViewById(R.id.emailText);
            TextView phoneText = activity.findViewById(R.id.phoneText);

            // Check if the views are set correctly
            assertEquals("John Doe", profileName.getText().toString());
            assertEquals("john@example.com", emailText.getText().toString());
            assertEquals("1234567890", phoneText.getText().toString());
        });
    }

    @Test
    public void testBackButtonNavigation() {
        // Simulate back button click
        activity.runOnUiThread(() -> {
            activity.findViewById(R.id.back_button).performClick();
        });

        // Test that the Intent is triggered correctly (we'll mock the Intent if necessary)
        Intent expectedIntent = new Intent(activity, DetailsActivity.class);
        // You can verify this with an actual check using a Mock Context
        assertEquals(expectedIntent.getComponent(), activity.getIntent().getComponent());
    }

    @Test
    public void testBottomNavigation() {
        // Simulate navigation item click (home)
        activity.runOnUiThread(() -> {
            activity.findViewById(R.id.nav_home).performClick();
        });

        // Verify the correct activity is triggered (MainActivity)
        Intent expectedIntent = new Intent(activity, MainActivity.class);
        assertEquals(expectedIntent.getComponent(), activity.getIntent().getComponent());
    }
}