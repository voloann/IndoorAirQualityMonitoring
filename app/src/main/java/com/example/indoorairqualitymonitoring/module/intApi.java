package com.example.indoorairqualitymonitoring.module;

import com.google.gson.annotations.SerializedName;

public class intApi {
    @SerializedName("name")
    public String name;

    @SerializedName("value")
    public int value;

    @SerializedName("timestamp")
    public long timestamp;
}
