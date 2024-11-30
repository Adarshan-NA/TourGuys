package com.wlu.tourguys.project;

import java.io.Serializable;

public class Destination implements Serializable {
    private String name;
    private String location;
    private String country;
    private String travelDates;
    private int duration;
    private String source;

    public Destination(String name, String location, String country, String travelDates, int duration, String source, int count, int maleCount, int femaleCount, long budget) {
        this.name = name;
        this.location = location;
        this.country = country;
        this.travelDates = travelDates;
        this.duration = duration;
        this.source = source;
        this.count = count;
        this.maleCount = maleCount;
        this.femaleCount = femaleCount;
        this.budget = budget;
    }

    private int count;
    private int maleCount;
    private int femaleCount;
    private long budget;

    public long getBudget() {
        return budget;
    }

    public void setBudget(long budget) {
        this.budget = budget;
    }

    public int getFemaleCount() {
        return femaleCount;
    }

    public void setFemaleCount(int femaleCount) {
        this.femaleCount = femaleCount;
    }

    public int getMaleCount() {
        return maleCount;
    }

    public void setMaleCount(int maleCount) {
        this.maleCount = maleCount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getTravelDates() {
        return travelDates;
    }

    public void setTravelDates(String travelDates) {
        this.travelDates = travelDates;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public Destination() {
        // Required empty constructor for Firebase
    }


//    // Getters for Firebase
//    public String getName() {
//        return name;
//    }
//
//    public String getDates() {
//        return dates;
//    }
//
//    public String getCount() {
//        return count;
//    }
//
//    public String getLocation() {
//        return location;
//    }
//
//    public String getCountry() {
//        return country;
//    }
}
