package com.example.indoorairqualitymonitoring.module;

import com.google.gson.annotations.SerializedName;

public class login {

    @SerializedName("access_token")
    public String access_token;

    @SerializedName("expires_in")
    public Integer expires_in;

    @SerializedName("token_type")
    public String token_type;

    public login(String access_token, Integer expires_in, String token_type) {
        this.access_token = access_token;
        this.expires_in = expires_in;
        this.token_type = token_type;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public Integer getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Integer expires_in) {
        this.expires_in = expires_in;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }
}
