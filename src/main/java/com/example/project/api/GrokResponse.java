package com.example.project.api;

import com.google.gson.Gson;

public class GrokResponse {

    public String response;
    public String created_at;
    public boolean done;

    public String getResponse() {
        return response;
    }

    public static GrokResponse fromJson(String body) {
        Gson gson = new Gson();
        GrokResponse response = gson.fromJson(body, GrokResponse.class);
        return response;
    }
}