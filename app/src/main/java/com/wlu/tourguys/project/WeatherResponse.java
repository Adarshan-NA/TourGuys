package com.wlu.tourguys.project;

import java.util.List;

public class WeatherResponse {

    public Coord coord;

    public List<Weather> weather;

    public Main main;

    public Sys sys;

    public String name;

    public static class Coord {
        public float lon;
        public float lat;
    }

    public static class Weather {
        public String main;
        public String description;
        public String icon;
    }

    public static class Main {
        public float temp;
        public float feels_like;
        public float temp_min;
        public float temp_max;
        public float pressure;
        public float humidity;
        public float sea_level;
        public float grnd_level;
    }

    public static class Sys {
        public String country;
        public long sunrise;
        public long sunset;
    }

}

//{
//        "coord": {
//        "lon": 121.4737,
//        "lat": 31.2304
//        }
//        ,
//        "weather": [
//        {
//        "id": 802,
//        "main": "Clouds",
//        "description": "多云",
//        "icon": "03d"
//        }
//
//        ],
//        "base": "stations",
//        "main": {
//        "temp": 292.03,
//        "feels_like": 291.15,
//        "temp_min": 291.04,
//        "temp_max": 292.03,
//        "pressure": 1025,
//        "humidity": 45,
//        "sea_level": 1025,
//        "grnd_level": 1024
//        }
//        ,
//        "visibility": 10000,
//        "wind": {
//        "speed": 6,
//        "deg": 20
//        },
//        "clouds": {
//        "all": 40
//        },
//        "dt": 1730782575,
//        "sys": {
//        "type": 1,
//        "id": 9659,
//        "country": "CN",
//        "sunrise": 1730758371,
//        "sunset": 1730797350
//        },
//        "timezone": 28800,
//        "id": 1796231,
//        "name": "Shanghai Municipality",
//        "cod": 200
//        }

