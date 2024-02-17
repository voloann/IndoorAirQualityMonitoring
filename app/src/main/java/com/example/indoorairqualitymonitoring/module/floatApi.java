package com.example.indoorairqualitymonitoring.module;

import com.google.gson.annotations.SerializedName;

public class floatApi {
    @SerializedName("name")
    public String name;

    @SerializedName("value")
    public float value;

    @SerializedName("timestamp")
    public long timestamp;
}
