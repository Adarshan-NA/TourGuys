package com.wlu.tourguys.project;

import android.content.Intent;
import android.widget.TextView;

import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class ContactDetailsActivityInstrumentationTest {

    @Rule
    public ActivityTestRule<ContactDetailsActivity> activityRule = new ActivityTestRule<>(ContactDetailsActivity.class);

    @Test
    public void testIntentDataDisplayed() {
        // Simulate intent data
        Intent intent = new Intent();
        intent.putExtra("userName", "John Doe");
        intent.putExtra("userEmail", "john@example.com");
        intent.putExtra("userPhone", "1234567890");

        activityRule.launchActivity(intent);

        // Check if the data is displayed on the screen
        onView(withId(R.id.profileName)).check(matches(withText("John Doe")));
        onView(withId(R.id.emailText)).check(matches(withText("john@example.com")));
        onView(withId(R.id.phoneText)).check(matches(withText("1234567890")));
    }

    @Test
    public void testBackButtonFunctionality() {
        onView(withId(R.id.back_button)).perform(ViewActions.click());

        // Verify the navigation intent (you can use the Activity result mechanism to check the intent)
    }

    @Test
    public void testBottomNavigationClick() {
        onView(withId(R.id.nav_home)).perform(ViewActions.click());
        // Verify MainActivity is started using matching components or other checks
    }
}
