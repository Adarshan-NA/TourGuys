package com.wlu.tourguys.project;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.wlu.tourguys.project.AddTripActivity;
import com.wlu.tourguys.project.MainActivity;
import com.wlu.tourguys.project.R;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.contrib.PickerActions.setDate;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

@RunWith(AndroidJUnit4.class)
public class AddTripActivityInstrumentationTest {

    @Rule
    public ActivityScenarioRule<AddTripActivity> activityScenarioRule =
            new ActivityScenarioRule<>(AddTripActivity.class);

    @Test
    public void validateInputFields_showsErrorWhenEmpty() {
        // Click the add trip button without filling the fields
        onView(withId(R.id.addTripButton)).perform(click());
        // Verify the error message is displayed
        onView(withText("Please fill all the fields")).check(matches(isDisplayed()));
    }

    @Test
    public void validateDatePicker_updatesDateField() {
        // Open the date picker
        onView(withId(R.id.startDate)).perform(click());
        // Set the date in the date picker
        onView(withClassName(Matchers.equalTo(android.widget.DatePicker.class.getName())))
                .perform(setDate(2024, 12, 1));
        // Confirm the date selection
        onView(withId(android.R.id.button1)).perform(click());
        // Verify the start date field displays the correct date
        onView(withId(R.id.startDate)).check(matches(withText("2024-12-01")));
    }

    @Test
    public void validateNumberOfDaysCalculation() {
        // Enter the start and end dates
        onView(withId(R.id.startDate)).perform(typeText("2024-12-01"), closeSoftKeyboard());
        onView(withId(R.id.endDate)).perform(typeText("2024-12-05"), closeSoftKeyboard());
        // Verify the number of days field calculates the correct value
        onView(withId(R.id.numDays)).check(matches(withText("5")));
    }

    @Test
    public void validateNavigation_backButtonNavigates() {
        // Click the back button
        onView(withId(R.id.back_button)).perform(click());
        // Verify navigation to MainActivity
        intended(hasComponent(MainActivity.class.getName()));
    }
}
