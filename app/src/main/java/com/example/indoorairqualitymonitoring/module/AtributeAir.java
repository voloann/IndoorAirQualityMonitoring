package com.example.indoorairqualitymonitoring.module;

import com.google.gson.annotations.SerializedName;

public class AtributeAir {

    @SerializedName("NO")
    public floatApi NO;

    @SerializedName("O3")
    public floatApi O3;

    @SerializedName("PM25")
    public floatApi PM25;

    @SerializedName("CO2")
    public intApi CO2;

    @SerializedName("CO2_average")
    public floatApi CO2_average;

    @SerializedName("NO2")
    public floatApi NO2;

    @SerializedName("AQI_Predict")
    public intApi AQI_Predict;

    @SerializedName("SO2")
    public floatApi SO2;

    @SerializedName("humidity")
    public intApi humidity;

    @SerializedName("AQI")
    public intApi AQI;

    @SerializedName("PM10")
    public floatApi PM10;

}
