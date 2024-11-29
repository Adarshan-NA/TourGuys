package com.wlu.tourguys.project;

public class Trip {
    private String sourceCountry;
    private String sourceCity;
    private String destinationCountry;
    private String destinationCity;
    private String startDate;
    private String endDate;
    private int numDays;
    private int numPeople;
    private int maleCount;
    private int femaleCount;
    private double budget;

    public Trip() {
        // Default constructor required for Firebase
    }

    public Trip(String sourceCountry, String sourceCity, String destinationCountry, String destinationCity,
                String startDate, String endDate, int numDays, int numPeople, int maleCount, int femaleCount, double budget) {
        this.sourceCountry = sourceCountry;
        this.sourceCity = sourceCity;
        this.destinationCountry = destinationCountry;
        this.destinationCity = destinationCity;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numDays = numDays;
        this.numPeople = numPeople;
        this.maleCount = maleCount;
        this.femaleCount = femaleCount;
        this.budget = budget;
    }

    // Getters and setters
    public String getSourceCountry() {
        return sourceCountry;
    }

    public void setSourceCountry(String sourceCountry) {
        this.sourceCountry = sourceCountry;
    }

    public String getSourceCity() {
        return sourceCity;
    }

    public void setSourceCity(String sourceCity) {
        this.sourceCity = sourceCity;
    }

    public String getDestinationCountry() {
        return destinationCountry;
    }

    public void setDestinationCountry(String destinationCountry) {
        this.destinationCountry = destinationCountry;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
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

    public int getNumDays() {
        return numDays;
    }

    public void setNumDays(int numDays) {
        this.numDays = numDays;
    }

    public int getNumPeople() {
        return numPeople;
    }

    public void setNumPeople(int numPeople) {
        this.numPeople = numPeople;
    }

    public int getMaleCount() {
        return maleCount;
    }

    public void setMaleCount(int maleCount) {
        this.maleCount = maleCount;
    }

    public int getFemaleCount() {
        return femaleCount;
    }

    public void setFemaleCount(int femaleCount) {
        this.femaleCount = femaleCount;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }
}