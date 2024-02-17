package com.example.indoorairqualitymonitoring.module;

import android.os.ParcelUuid;

import com.google.gson.annotations.SerializedName;

public class AirAsset {

    @SerializedName("id")
    public String id;
    @SerializedName("version")
    public String version;

    @SerializedName("attributes")
    public AtributeAir attributes;
}
