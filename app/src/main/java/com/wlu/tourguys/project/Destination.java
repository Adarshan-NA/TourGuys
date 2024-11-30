package com.wlu.tourguys.project;

import java.io.Serializable;

public class Destination implements Serializable {
    private String name;
    private String destinationCity;
    private String destinationCountry;
    private String startDate;
    private String endDate;
    private int numDays;
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
    public Destination(String name, String destinationCity, String destinationCountry, String startDate, String endDate,
                       int numDays, int numPeople, int maleCount, int femaleCount, String sourceCity, String sourceCountry, double budget) {
        this.name = name;
        this.destinationCity = destinationCity;
        this.destinationCountry = destinationCountry;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numDays = numDays;
        this.numPeople = numPeople;
        this.maleCount = maleCount;
        this.femaleCount = femaleCount;
        this.sourceCity = sourceCity;
        this.sourceCountry = sourceCountry;
        this.budget = budget;
    }

    public Destination(String sourceCountry, String s, String numPeople, String destinationCity, String destinationCountry) {
    }

    // Getters and Setters for each field
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public void setBudget(double budget) {
        this.budget = budget;
    }
}