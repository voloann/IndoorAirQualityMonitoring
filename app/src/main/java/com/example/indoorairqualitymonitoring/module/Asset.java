package com.example.indoorairqualitymonitoring.module;

import com.google.gson.annotations.SerializedName;

public class Asset {
    @SerializedName("id")
    public String id;
    @SerializedName("version")
    public String version;
    @SerializedName("createdOn")
    public String createdOn;
    @SerializedName("name")
    public String name;

    @SerializedName("attributes")
    public Atribute attributes;
}
