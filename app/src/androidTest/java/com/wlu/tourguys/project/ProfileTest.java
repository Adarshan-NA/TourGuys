package com.wlu.tourguys.project;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

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
public class ProfileTest {

    @Rule
    public ActivityScenarioRule<Splash> mActivityScenarioRule =
            new ActivityScenarioRule<>(Splash.class);

    @Test
    public void profileTest() {
        ViewInteraction materialButton = onView(
                allOf(withId(R.id.signInButton), withText("Sign In"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.welcome_screen),
                                        1),
                                1),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.nav_profile), withContentDescription("Profile"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottom_navigation),
                                        0),
                                3),
                        isDisplayed()));
        bottomNavigationItemView.perform(click());

        ViewInteraction linearLayout = onView(
                allOf(withId(R.id.profile_settings_button),
                        childAtPosition(
                                allOf(withId(R.id.linear_layout_profile),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        linearLayout.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.name_field), withText("Aabiyah Zehra"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                0)));
        appCompatEditText.perform(scrollTo(), replaceText("Aabiyah "));

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.name_field), withText("Aabiyah "),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText2.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.password_field),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                2)));
        appCompatEditText3.perform(scrollTo(), replaceText("Aabiyah123"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.mobile_field), withText("2267513492"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        appCompatEditText4.perform(scrollTo(), click());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.mobile_field), withText("2267513492"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        appCompatEditText5.perform(scrollTo(), click());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.mobile_field), withText("2267513492"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        appCompatEditText6.perform(scrollTo(), click());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.mobile_field), withText("2267513492"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        appCompatEditText7.perform(scrollTo(), click());

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.mobile_field), withText("2267513492"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        appCompatEditText8.perform(scrollTo(), replaceText("9310823898"));

        ViewInteraction appCompatEditText9 = onView(
                allOf(withId(R.id.mobile_field), withText("9310823898"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText9.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText10 = onView(
                allOf(withId(R.id.city_field), withText("Toronto"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                4)));
        appCompatEditText10.perform(scrollTo(), replaceText("Waterloo"));

        ViewInteraction appCompatEditText11 = onView(
                allOf(withId(R.id.city_field), withText("Waterloo"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText11.perform(closeSoftKeyboard());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.save_button), withText("Save"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.back_button), withContentDescription("Back"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatImageView.perform(click());

        ViewInteraction linearLayout2 = onView(
                allOf(withId(R.id.logout_button),
                        childAtPosition(
                                allOf(withId(R.id.linear_layout_profile),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                0)),
                                2),
                        isDisplayed()));
        linearLayout2.perform(click());

        ViewInteraction appCompatEditText12 = onView(
                allOf(withId(R.id.loginEmailEditText), withText("email@domain.com"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.login_screen),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText12.perform(click());

        ViewInteraction appCompatEditText13 = onView(
                allOf(withId(R.id.loginEmailEditText), withText("email@domain.com"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.login_screen),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText13.perform(click());

        ViewInteraction appCompatEditText14 = onView(
                allOf(withId(R.id.loginEmailEditText), withText("email@domain.com"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.login_screen),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText14.perform(click());

        ViewInteraction appCompatEditText15 = onView(
                allOf(withId(R.id.loginEmailEditText), withText("email@domain.com"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.login_screen),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText15.perform(click());

        ViewInteraction appCompatEditText16 = onView(
                allOf(withId(R.id.loginEmailEditText), withText("email@domain.com"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.login_screen),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText16.perform(click());

        ViewInteraction appCompatEditText17 = onView(
                allOf(withId(R.id.loginEmailEditText), withText("email@domain.com"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.login_screen),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText17.perform(click());

        ViewInteraction appCompatEditText18 = onView(
                allOf(withId(R.id.loginEmailEditText), withText("email@domain.com"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.login_screen),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText18.perform(click());

        ViewInteraction appCompatEditText19 = onView(
                allOf(withId(R.id.loginEmailEditText), withText("email@domain.com"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.login_screen),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText19.perform(replaceText("aabiyah@gmail.com"));

        ViewInteraction appCompatEditText20 = onView(
                allOf(withId(R.id.loginEmailEditText), withText("aabiyah@gmail.com"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.login_screen),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText20.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText21 = onView(
                allOf(withId(R.id.loginPasswordEditText),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.login_screen),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText21.perform(replaceText("Aabiyah123"), closeSoftKeyboard());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.loginButton), withText("Login"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.login_screen),
                                        0),
                                5),
                        isDisplayed()));
        materialButton3.perform(click());
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
