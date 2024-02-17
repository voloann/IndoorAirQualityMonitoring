package com.example.indoorairqualitymonitoring.module;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class weatherHTTP {

    @SerializedName("name")
    public String name;

    @SerializedName("weather")
    public List<Weather> weather;
}
