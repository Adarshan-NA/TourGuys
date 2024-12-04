package com.wlu.tourguys.project;

import android.widget.EditText;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

@RunWith(AndroidJUnit4.class)
public class AddTripActivityInstrumentationTest {

    @Rule
    public ActivityScenarioRule<AddTripActivity> activityRule =
            new ActivityScenarioRule<>(AddTripActivity.class);

    @Before
    public void setUp() {
        // Any setup logic before each test
    }

    @Test
    public void testMissingFields_ShowError() {
        // Act - Click Add Trip Button without filling any fields
        onView(withId(R.id.addTripButton)).perform(ViewActions.click());

        // Assert - Check that all required fields show error
        onView(withId(R.id.sourceCountry)).check(matches(hasErrorText("This field is required")));
        onView(withId(R.id.sourceCity)).check(matches(hasErrorText("This field is required")));
        // ... Repeat for other fields
    }

    @Test
    public void testValidInput_AddTrip() {
        // Act - Fill the form with valid data
        onView(withId(R.id.sourceCountry)).perform(ViewActions.typeText("USA"));
        onView(withId(R.id.sourceCity)).perform(ViewActions.typeText("New York"));
        onView(withId(R.id.destinationCountry)).perform(ViewActions.typeText("Canada"));
        onView(withId(R.id.destinationCity)).perform(ViewActions.typeText("Toronto"));
        onView(withId(R.id.startDate)).perform(ViewActions.typeText("2024-12-15"));
        onView(withId(R.id.endDate)).perform(ViewActions.typeText("2024-12-20"));
        onView(withId(R.id.numPeople)).perform(ViewActions.typeText("5"));
        onView(withId(R.id.maleCount)).perform(ViewActions.typeText("3"));
        onView(withId(R.id.femaleCount)).perform(ViewActions.typeText("2"));
        onView(withId(R.id.budget)).perform(ViewActions.typeText("1000"));

        // Perform the button click to add the trip
        onView(withId(R.id.addTripButton)).perform(ViewActions.click());

        // Assert - Verify if the trip was added (this can be verified by checking for Toast or activity redirection)
        // For example, checking if MainActivity is launched:
        onView(withId(R.id.recyclerView_trips)).check(matches(isDisplayed()));
    }

    @Test
    public void testInvalidDateRange_ShowError() {
        // Fill valid start date but invalid end date (end date < start date)
        onView(withId(R.id.startDate)).perform(ViewActions.typeText("2024-12-15"));
        onView(withId(R.id.endDate)).perform(ViewActions.typeText("2024-12-10"));
        onView(withId(R.id.addTripButton)).perform(ViewActions.click());

        // Assert - Check if appropriate error is shown for invalid date range
        onView(withId(R.id.endDate)).check(matches(hasErrorText("End date cannot be less than start date")));
    }
}
