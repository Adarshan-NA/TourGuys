package com.wlu.tourguys.project;

public class Destination {
    private String name;
    private String dates;
    private String count;
    private String location;
    private String country;

    public Destination() {
        // Required empty constructor for Firebase
    }

    public Destination(String name, String dates, String count, String location, String country) {
        this.name = name;
        this.dates = dates;
        this.count = count;
        this.location = location;
        this.country = country;
    }

    // Getters for Firebase
    public String getName() {
        return name;
    }

    public String getDates() {
        return dates;
    }

    public String getCount() {
        return count;
    }

    public String getLocation() {
        return location;
    }

    public String getCountry() {
        return country;
    }
}
