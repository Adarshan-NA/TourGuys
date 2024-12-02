package com.wlu.tourguys.project;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Destination implements Serializable {
    private String destinationCity;
    private String destinationCountry;
    private String startDate;
    private String endDate;
    private int numPeople;
    private int maleCount;
    private int femaleCount;
    private String sourceCity;
    private String sourceCountry;
    private double budget;

    // Default constructor (required for Firebase)
    public Destination() {
    }

    // Constructor with all fields
    public Destination(String destinationCity, String destinationCountry, String startDate, String endDate,
                       int numPeople, int maleCount, int femaleCount, String sourceCity, String sourceCountry, double budget) {
        this.destinationCity = destinationCity;
        this.destinationCountry = destinationCountry;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numPeople = numPeople;
        this.maleCount = maleCount;
        this.femaleCount = femaleCount;
        this.sourceCity = sourceCity;
        this.sourceCountry = sourceCountry;
        this.budget = budget;
    }

    // Getters and Setters
    public String getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }

    public String getDestinationCountry() {
        return destinationCountry;
    }

    public void setDestinationCountry(String destinationCountry) {
        this.destinationCountry = destinationCountry;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getNumPeople() {
        return numPeople;
    }

    public void setNumPeople(Object numPeople) {
        this.numPeople = parseIntSafely(numPeople);
    }

    public int getMaleCount() {
        return maleCount;
    }

    public void setMaleCount(Object maleCount) {
        this.maleCount = parseIntSafely(maleCount);
    }

    public int getFemaleCount() {
        return femaleCount;
    }

    public void setFemaleCount(Object femaleCount) {
        this.femaleCount = parseIntSafely(femaleCount);
    }

    public String getSourceCity() {
        return sourceCity;
    }

    public void setSourceCity(String sourceCity) {
        this.sourceCity = sourceCity;
    }

    public String getSourceCountry() {
        return sourceCountry;
    }

    public void setSourceCountry(String sourceCountry) {
        this.sourceCountry = sourceCountry;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(Object budget) {
        this.budget = parseDoubleSafely(budget);
    }

    // Method to calculate the number of days between startDate and endDate
    public int getNumDays() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Use your date format
        try {
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);
            long difference = end.getTime() - start.getTime();
            return (int) (difference / (1000 * 60 * 60 * 24)); // Convert milliseconds to days
        } catch (ParseException e) {
            e.printStackTrace();
            return 0; // Return 0 if there's an error in parsing
        }
    }

    // Method to return a meaningful name (combine city and country)
    public String getName() {
        return destinationCity + ", " + destinationCountry;
    }

    // Helper method to parse integers safely
    private int parseIntSafely(Object value) {
        if (value instanceof Integer) {
            return (int) value;
        } else if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return 0; // Default to 0 if the type is unexpected or parsing fails
    }

    // Helper method to parse doubles safely
    private double parseDoubleSafely(Object value) {
        if (value instanceof Double) {
            return (double) value;
        } else if (value instanceof String) {
            try {
                return Double.parseDouble((String) value);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return 0.0; // Default to 0.0 if the type is unexpected or parsing fails
    }
}
