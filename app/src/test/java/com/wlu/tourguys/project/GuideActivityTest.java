package com.wlu.tourguys.project;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import retrofit2.Call;

public class GuideActivityTest {

    private GuideActivity guideActivity;

    @Mock
    private WeatherService mockWeatherService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        guideActivity = new GuideActivity();
        guideActivity.weatherService = mockWeatherService;
    }

    // Test: validateInput method
    @Test
    public void testValidateInput_ValidInput() {
        String validLocation = "Waterloo";
        String validDate = "2024-12-04";
        boolean result = guideActivity.validateInput(validLocation, validDate);
        assertTrue(result);
    }

    @Test
    public void testValidateInput_EmptyLocation() {
        String invalidLocation = "";
        String validDate = "2024-12-04";
        boolean result = guideActivity.validateInput(invalidLocation, validDate);
        assertFalse(result);
    }

    @Test
    public void testValidateInput_InvalidDate() {
        String validLocation = "Waterloo";
        String invalidDate = "04-12-2024";
        boolean result = guideActivity.validateInput(validLocation, invalidDate);
        assertFalse(result);
    }

    // Test: isValidDate method
    @Test
    public void testIsValidDate_ValidDate() {
        assertTrue(guideActivity.isValidDate("2024-12-04"));
    }

    @Test
    public void testIsValidDate_InvalidDate() {
        assertFalse(guideActivity.isValidDate("04-12-2024"));
        assertFalse(guideActivity.isValidDate("2024/12/04"));
        assertFalse(guideActivity.isValidDate(""));
    }

    // Test: fetchWeatherData method
    @Test
    public void testFetchWeatherData_ValidCall() {
        double lat = 43.4643;
        double lon = -80.5204;
        long timestamp = 1701686400; // Example timestamp for 2024-12-04
        String apiKey = "8cd894b6c63a38fa958cfc7e739f112b";

        Call<WeatherResponse> mockCall = mock(Call.class);
        when(mockWeatherService.getWeather(lat, lon, apiKey, timestamp, "en_us", "metric"))
                .thenReturn(mockCall);

        guideActivity.fetchWeatherData(lat, lon, timestamp);

        verify(mockWeatherService).getWeather(lat, lon, apiKey, timestamp, "en_us", "metric");
    }

    @Test
    public void testFetchWeatherData_InvalidCoordinates() {
        double lat = -999; // Invalid latitude
        double lon = -999; // Invalid longitude
        long timestamp = 1701686400;

        Call<WeatherResponse> mockCall = mock(Call.class);
        when(mockWeatherService.getWeather(lat, lon, "8cd894b6c63a38fa958cfc7e739f112b", timestamp, "en_us", "metric"))
                .thenReturn(mockCall);

        guideActivity.fetchWeatherData(lat, lon, timestamp);

        verify(mockWeatherService).getWeather(lat, lon, "8cd894b6c63a38fa958cfc7e739f112b", timestamp, "en_us", "metric");
    }
}

