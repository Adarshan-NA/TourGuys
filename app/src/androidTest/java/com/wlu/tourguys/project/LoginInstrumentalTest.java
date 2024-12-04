package com.wlu.tourguys.project;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginInstrumentalTest {

    @Rule
    public ActivityScenarioRule<Splash> mActivityScenarioRule =
            new ActivityScenarioRule<>(Splash.class);

    @Test
    public void loginInstrumentalTest() {
        ViewInteraction materialButton = onView(
                allOf(withId(R.id.signInButton), withText("Sign In"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.welcome_screen),
                                        1),
                                1),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.loginEmailEditText), withText("email@domain.com"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.login_screen),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("adarshan@gmail.com"));

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.loginEmailEditText), withText("adarshan@gmail.com"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.login_screen),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText2.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.loginPasswordEditText),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.login_screen),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("Adarsh"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.loginPasswordEditText), withText("Adarsh"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.login_screen),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText4.perform(longClick());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.loginPasswordEditText), withText("Adarsh"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.login_screen),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText5.perform(pressImeActionButton());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.loginButton), withText("Login"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.login_screen),
                                        0),
                                5),
                        isDisplayed()));
        materialButton2.perform(click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
