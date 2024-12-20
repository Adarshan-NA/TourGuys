package com.wlu.tourguys.project;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

// 新建 GeminiHelper 类
public class GeminiHelper {

    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=AIzaSyDlso-v3F-9pB9SySV72rbP5NrGU0GMW-I";

    public void getTravelSuggestion(String location, String weather, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();


        String requestBodyJsonLocation = "location: " + location;
        String requestBodyJsonWeather = "weather: " + weather;

        String content = "{ \"contents\": [{ \"parts\":[{\"text\": \"Generate a touring itinerary for travelling to：" + requestBodyJsonLocation + "given that the weather conditions for the day are:" + requestBodyJsonWeather + "remove all the astrix from the text" + " \"}] }] }";

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), content);

        System.out.println(content);

        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .build();

        client.newCall(request).enqueue(callback);
    }

    public static void main(String[] args) {
        GeminiHelper geminiHelper = new GeminiHelper();
        geminiHelper.getTravelSuggestion("", "", new okhttp3.Callback() {
            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    try {
                        JSONObject json = new JSONObject(responseData);
                        System.out.println(json);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
            }
        });

    }
}
