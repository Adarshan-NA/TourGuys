package com.wlu.tourguys.project;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GuideActivity extends AppCompatActivity {

    private static final String map_api_key = "AIzaSyARxN4eObMVQ92qm45dsEARH4wBfUIg7dk";

    EditText editTextLocation;
    private TextView editTextDate;
    private TextView textViewResult;
    WeatherService weatherService;

    private TextView locationName;
    TextView weatherMain;
    TextView weatherDescription;
    private TextView temperature;
    private TextView feelsLike;
    private TextView tempMinMax;
    private TextView pressure;
    private TextView humidity;
    private TextView sunrise;
    private TextView sunset;
    private TextView travelAdvice;

    double latitude;

    double longitude;

    BottomNavigationView bottomNavigation;

    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), map_api_key, Locale.getDefault());
        }

        editTextLocation = findViewById(R.id.location_input);
        editTextLocation.setFocusable(false);
        editTextLocation.setOnClickListener(v -> {
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                    .build(GuideActivity.this);
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
        });
        editTextDate = findViewById(R.id.date_input);
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        GuideActivity.this,
                        (view, selectedYear, selectedMonth, selectedDay) -> {
                            // 设置选择的日期
                            String selectedDate = String.format(Locale.getDefault(), "%d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                            editTextDate.setText(selectedDate);
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });

        textViewResult = findViewById(R.id.location_name);
        //
        locationName = findViewById(R.id.location_name);
        weatherMain = findViewById(R.id.weather_main);
        weatherDescription = findViewById(R.id.weather_description);
        temperature = findViewById(R.id.temperature);
        feelsLike = findViewById(R.id.feels_like);
        tempMinMax = findViewById(R.id.temp_min_max);
        pressure = findViewById(R.id.pressure);
        humidity = findViewById(R.id.humidity);
        sunrise = findViewById(R.id.sunrise);
        sunset = findViewById(R.id.sunset);
        travelAdvice = findViewById(R.id.travel_advice);
        Button buttonGetWeather = findViewById(R.id.search_button);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        weatherService = retrofit.create(WeatherService.class);

        buttonGetWeather.setOnClickListener(v -> {
            String location = editTextLocation.getText().toString().trim();
            String date = editTextDate.getText().toString().trim();
            if (validateInput(location, date)) {
                getCoordinatesAndWeather(latitude, longitude, date);
            }
        });

        bottomNavigation = findViewById(R.id.bottom_navigation);

        bottomNavigation.setSelectedItemId(R.id.nav_guide);

        /*bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                // Handle home action
                startActivity(new Intent(this, MainActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_add_trip) {
                // Handle add trip action
                return true;
            } else if (item.getItemId() == R.id.nav_guide) {
                startActivity(new Intent(this, GuideActivity.class));
                // Handle guide action
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                // Handle profile action
                return true;
            }
            return false;
        });*/
        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                // Handle home action
                startActivity(new Intent(this, MainActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_add_trip) {
                // Navigate to AddTripActivity when Add Trip icon is clicked
                startActivity(new Intent(this, AddTripActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_guide) {
                startActivity(new Intent(this, GuideActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                startActivity(new Intent(this, Profile.class));
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                editTextLocation.setText(place.getName());
                LatLng latLng = place.getLatLng();
                if (latLng != null) {
                    double latitude = latLng.latitude;
                    double longitude = latLng.longitude;
                    // 使用 latitude 和 longitude 更新坐标信息
                    this.latitude = latitude;
                    this.longitude = longitude;
                }
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Toast.makeText(this, "Error: " + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCoordinatesAndWeather(double latitude, double longitude, String date) {
        // 使用 Google Maps API 获取坐标（假设已经获得坐标）
        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        }
        LocalDate localDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            localDate = LocalDate.parse(date, formatter);
        }
        // 将LocalDate转换为ZonedDateTime
        ZonedDateTime zonedDateTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        }
        // 将ZonedDateTime转换为时间戳
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            long timestamp = zonedDateTime.toEpochSecond();
            fetchWeatherData(latitude, longitude, timestamp);
        }
    }

    void fetchWeatherData(double lat, double lon, long timestamp) {
        String apiKey = "8cd894b6c63a38fa958cfc7e739f112b";
        Call<WeatherResponse> call = weatherService.getWeather(lat, lon, apiKey, timestamp, "en_us", "metric");
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    displayWeatherInfo(response.body());
                } else {
                    textViewResult.setText("Failed to get weather data");
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                textViewResult.setText("Error: " + t.getMessage());
            }
        });
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void displayWeatherInfo(WeatherResponse weatherResponse) {
        // 根据天气数据生成旅行建议
        locationName.setText(weatherResponse.name);
        weatherMain.setText(weatherResponse.weather.get(0).main);
        weatherDescription.setText(weatherResponse.weather.get(0).description);
        temperature.setText(String.format("Temperature: %.2f°C", weatherResponse.main.temp));
        feelsLike.setText(String.format("Feels Like: %.2f°C", weatherResponse.main.feels_like));
        tempMinMax.setText(String.format("Min/Max: %.2f°C/%.2f°C", weatherResponse.main.temp_min, weatherResponse.main.temp_max));
        pressure.setText(String.format("Pressure: %.0f hPa", weatherResponse.main.pressure));
        humidity.setText(String.format("Humidity: %.0f%%", weatherResponse.main.humidity));
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        sunrise.setText("Sunrise: " + sdf.format(new Date(weatherResponse.sys.sunrise * 1000)));
        sunset.setText("Sunset: " + sdf.format(new Date(weatherResponse.sys.sunset * 1000)));

        travelAdvice.setText("Generating in progress……");

        GeminiHelper geminiHelper = new GeminiHelper();
        geminiHelper.getTravelSuggestion(weatherResponse.name, weatherResponse.weather.get(0).main, new okhttp3.Callback() {
            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    try {
                        String travelAdviceText = TravelAdviceExtractor.extractTravelAdviceText(responseData);
                        runOnUiThread(() -> travelAdvice.setText(travelAdviceText.trim()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                runOnUiThread(() -> travelAdvice.setText("Error: " + e.getMessage()));
            }
        });
    }

    boolean validateInput(String location, String date) {
        if (location.isEmpty()) {
            editTextLocation.setError("Location cannot be empty");
            return false;
        }
        if (!isValidDate(date)) {
            editTextDate.setError("Date must be in YYYY-MM-DD format");
            return false;
        }
        return true;
    }

    boolean isValidDate(String date) {
        String regex = "^\\d{4}-\\d{2}-\\d{2}$";
        return date.matches(regex);
    }
}

