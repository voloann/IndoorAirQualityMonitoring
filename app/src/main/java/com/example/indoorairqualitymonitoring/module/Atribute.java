package com.example.indoorairqualitymonitoring.module;

import com.google.gson.annotations.SerializedName;

public class Atribute {

    @SerializedName("rainfall")
    public floatApi rainfall;

    @SerializedName("temperature")
    public floatApi temperature;

    @SerializedName("humidity")
    public intApi humidity;

    @SerializedName("windDirection")
    public intApi windDirection;

    @SerializedName("windSpeed")
    public floatApi windSpeed;

    @SerializedName("place")
    public Place place;

}
