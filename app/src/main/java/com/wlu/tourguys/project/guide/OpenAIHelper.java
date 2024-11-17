package com.wlu.tourguys.project.guide;

import okhttp3.*;

public class OpenAIHelper {

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String API_KEY = "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";

    public void getTravelSuggestion(String location, String weather, Callback callback) {
        OkHttpClient client = new OkHttpClient();

        String prompt = "Based on the current weather in " + location + ", suggest some travel activities.";

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"),
                "{ \"prompt\": \"" + prompt + "\", \"max_tokens\": 150 }"
        );

        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .build();

        client.newCall(request).enqueue(callback);
    }

}
