package com.example.indoorairqualitymonitoring.API;

import com.example.indoorairqualitymonitoring.module.AirAsset;
import com.example.indoorairqualitymonitoring.module.Asset;
import com.example.indoorairqualitymonitoring.module.PeriodsAsset;
import com.example.indoorairqualitymonitoring.module.WeatherAsset;
import com.example.indoorairqualitymonitoring.module.WeatherAssetReq;
import com.example.indoorairqualitymonitoring.module.login;
import com.example.indoorairqualitymonitoring.module.profile;
import com.example.indoorairqualitymonitoring.module.weatherHTTP;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Service_Interface {
    @FormUrlEncoded
    @POST("token")
    Call<login> login1(
            @Field("client_id") String client_id,
            @Field("username") String username,
            @Field("password") String password,
            @Field("grant_type") String grant_type
    );

    @GET("weather")
    Call<weatherHTTP> getatribute(@Query("units") String units,
                                  @Query("appid") String appid,
                                  @Query("lon") float lon,
                                  @Query("lat") float lat);

    @GET("api/master/asset/{assetID}")
    Call<Asset> getAsset(@Path("assetID") String assetID);

    @GET("api/master/asset/{assetID}")
    Call<AirAsset> getAsset1(@Path("assetID") String assetID);

    @GET("api/master/user/user")
    Call<profile> getProfile();

    @POST("6Wo9Lv1Oa1zQleuRVfADP4/attribute/{category}")
    Call<List<WeatherAsset>> getDataset(@Path("category") String category,
                                        @Body WeatherAssetReq weatherAssetReq);

    @Headers({"accept: application/json"})
    @GET("periods")
    Call<PeriodsAsset> getPeriod(@Query("assetId") String assetId,
                                 @Query("attributeName") String attributeName,
                                 @Header("Authorization") String auth);
}