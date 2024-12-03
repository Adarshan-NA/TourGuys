package com.wlu.tourguys.project;

import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import java.util.ArrayList;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 34, manifest = Config.NONE)
public class MainActivityTest {

    private MainActivity activity;

    @Before
    public void setUp() {
        // Enable logging for better debugging
        ShadowLog.stream = System.out;

        // Create activity
        activity = Robolectric.setupActivity(MainActivity.class);
    }

    @Test
    public void testActivityCreation() {
        assertNotNull(activity);
    }



    @Test
    public void testFilterTrips() {
        // Ensure destinationList is initialized before use
        if (activity.destinationList == null) {
            activity.destinationList = new ArrayList<>();
        }

        // Prepare test data
        Destination dest1 = new Destination();
        dest1.setDestinationCountry("Canada");
        dest1.setDestinationCity("Toronto");

        Destination dest2 = new Destination();
        dest2.setDestinationCountry("USA");
        dest2.setDestinationCity("New York");

        // Add destinations to the list
        activity.destinationList.add(dest1);
        activity.destinationList.add(dest2);

        // Perform filtering
        activity.filterTrips("Toronto");


    }

    @Test
    public void testEmptySearchQuery() {
        // Ensure destinationList is initialized before use
        if (activity.destinationList == null) {
            activity.destinationList = new ArrayList<>();
        }

        // Prepare test data
        Destination dest1 = new Destination();
        dest1.setDestinationCountry("Canada");
        dest1.setDestinationCity("Toronto");

        Destination dest2 = new Destination();
        dest2.setDestinationCountry("USA");
        dest2.setDestinationCity("New York");

        // Add destinations to the list
        activity.destinationList.add(dest1);
        activity.destinationList.add(dest2);

        // Perform empty search
        activity.filterTrips("");


    }

    @Test
    public void testBottomNavigation() {
        // This test checks if bottom navigation view is present
        assertNotNull(activity.findViewById(R.id.bottom_navigation));
    }
}