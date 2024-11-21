package com.wlu.tourguys.project;

public class Destination {
    private final String name;
    private final String dates;
    private final String count;
    private final String location;
    private final String country;

    public Destination(String name, String dates, String count, String location, String country) {
        this.name = name;
        this.dates = dates;
        this.count = count;
        this.location = location;
        this.country = country;
    }

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