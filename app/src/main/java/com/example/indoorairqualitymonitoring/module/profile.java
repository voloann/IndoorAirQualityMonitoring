package com.example.indoorairqualitymonitoring.module;

import com.google.gson.annotations.SerializedName;

public class profile {

    @SerializedName("real")
    public String realm;

    @SerializedName("realmId")
    public String realmId;

    @SerializedName("id")
    public String id;

    @SerializedName("email")
    public String email;

    @SerializedName("enabled")
    public boolean enabled;

    @SerializedName("createdOn")
    public long createdOn;

    @SerializedName("serviceAccount")
    public boolean serviceAccount;

    @SerializedName("username")
    public String username;
}
