package com.example.indoorairqualitymonitoring.module;

import com.google.gson.annotations.SerializedName;

public class Place {
    @SerializedName("name")
    public String name;

    @SerializedName("value")
    public String value;
}
