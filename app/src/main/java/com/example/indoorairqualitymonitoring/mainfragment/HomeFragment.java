package com.example.indoorairqualitymonitoring.mainfragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.indoorairqualitymonitoring.API.RetrofitClientInstance;
import com.example.indoorairqualitymonitoring.API.Service_Interface;
import com.example.indoorairqualitymonitoring.R;
import com.example.indoorairqualitymonitoring.module.AirAsset;
import com.example.indoorairqualitymonitoring.module.Asset;
import com.example.indoorairqualitymonitoring.module.Weather;
import com.example.indoorairqualitymonitoring.module.weatherHTTP;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    public TextView name;
    public TextView temp;
    public TextView humidity1;
    public TextView rainfall;
    public TextView ass_aiq1;
    public TextView ass_Co2;
    public TextView ass_pm25;
    public TextView NO;
    public Button buttonview;
    public Button ass_button;
    public int aqi;
    public LinearLayout defaultWeatherLayout;
    public LinearLayout weatherAssetLayout;
    public String tokenadmin;
    public String token;
    public Service_Interface loginService;
    public Service_Interface serviceInterface;
    public Service_Interface iService;


    private org.osmdroid.views.MapView mapView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        defaultWeatherLayout = view.findViewById(R.id.Defaultweather);
        weatherAssetLayout = view.findViewById(R.id.Assetweather);

        name = view.findViewById(R.id.name);
        temp = view.findViewById(R.id.temp);
        humidity1 = view.findViewById(R.id.humidity);
        rainfall = view.findViewById(R.id.rainfall);
        buttonview = view.findViewById(R.id.view);
        ass_aiq1 = view.findViewById(R.id.ass_AIQ);
        ass_Co2 = view.findViewById(R.id.co2);
        ass_pm25 = view.findViewById(R.id.ass_pm);
        NO = view.findViewById(R.id.NO);
        ass_button = view.findViewById(R.id.ass_button);
        tokenadmin = getArguments().getString("tokenadmin");
        token = getArguments().getString("token");

        getWeather();
        getAsset();
        getAirAsset();


        buttonview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WeatherActivity.class);
                intent.putExtra("tokenadmin", tokenadmin);
                intent.putExtra("token", token);
                startActivity(intent);
            }
        });

        ass_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AirActivity.class);
                intent.putExtra("tokenadmin", tokenadmin);
                intent.putExtra("token", token);
                startActivity(intent);
            }
        });

        ImageView currentLocationImageView = view.findViewById(R.id.current);
        Configuration.getInstance().load(view.getContext(), view.getContext().getSharedPreferences("osmdroid", Context.MODE_PRIVATE));

        // Tạo MapView
        mapView = view.findViewById(R.id.mapVieww);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        // Đặt trung tâm và zoom bản đồ
        GeoPoint centerPoint = new GeoPoint(10.869778736885038, 106.80280655508835);
        mapView.getController().setCenter(centerPoint);
        mapView.getController().setZoom(18);

        // Thêm Marker vào bản đồ
        OverlayItem markerItem = new OverlayItem("Default Weather", "Default Weather in Android", centerPoint);
        Marker marker = new Marker(mapView);
        Drawable drawable = getResources().getDrawable(R.drawable.ic_location);
        marker.setPosition(centerPoint);
        marker.setIcon(drawable);
        marker.setTitle("Default Weather");
        mapView.getOverlayManager().add(marker);


        final boolean[] isDefaultWeatherLayoutVisible = {false};

        marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                if (weatherAssetLayout.getVisibility() == View.VISIBLE) {
                    weatherAssetLayout.setVisibility(View.GONE);
                    defaultWeatherLayout.setVisibility(View.VISIBLE);
                } else {
                    isDefaultWeatherLayoutVisible[0] = !isDefaultWeatherLayoutVisible[0];
                    if (isDefaultWeatherLayoutVisible[0]) {
                        defaultWeatherLayout.setVisibility(View.VISIBLE);
                    } else {
                        defaultWeatherLayout.setVisibility(View.GONE);
                    }
                }
                return true;
            }
        });
        mapView.getOverlayManager().add(marker);


        GeoPoint anotherPoint = new GeoPoint(10.869905172970164, 106.80345028525176);
        OverlayItem anotherMarkerItem = new OverlayItem("Another Location", "Description of Another Location", anotherPoint);

        // Tạo một Marker mới và đặt các thuộc tính cho nó
        Marker anotherMarker = new Marker(mapView);
        Drawable anotherDrawable = getResources().getDrawable(R.drawable.ic_location2);
        anotherMarker.setPosition(anotherPoint);
        anotherMarker.setIcon(anotherDrawable);
        anotherMarker.setTitle("Another Location");


        // Thêm điểm neo và Marker mới vào OverlayManager
        mapView.getOverlayManager().add(anotherMarker);

        // Cập nhật bản đồ để hiển thị điểm neo mới và điểm hiện tại
        mapView.invalidate();

        final boolean[] isDefaultWeatherLayoutVisible1 = {false};

        anotherMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                if (defaultWeatherLayout.getVisibility() == View.VISIBLE) {
                    defaultWeatherLayout.setVisibility(View.GONE);
                    weatherAssetLayout.setVisibility(View.VISIBLE);
                } else {
                    isDefaultWeatherLayoutVisible1[0] = !isDefaultWeatherLayoutVisible1[0];
                    if (isDefaultWeatherLayoutVisible1[0]) {
                        defaultWeatherLayout.setVisibility(View.GONE);
                        weatherAssetLayout.setVisibility(View.VISIBLE);
                    } else {
                        weatherAssetLayout.setVisibility(View.GONE);
                    }
                }
                return true;
            }
        });
        mapView.getOverlayManager().add(marker);

        MyLocationNewOverlay myLocationOverlay = new MyLocationNewOverlay(mapView);

        // Đặt vị trí hiện tại của MyLocationOverlay trên bản đồ
        mapView.getOverlays().add(myLocationOverlay);

        // Lấy vị trí hiện tại
        GeoPoint currentGeoPoint = myLocationOverlay.getMyLocation();
        if (currentGeoPoint != null) {
            // Thêm một Marker tại vị trí hiện tại
            Marker currentLocationMarker = new Marker(mapView);
            currentLocationMarker.setPosition(currentGeoPoint);
            currentLocationMarker.setIcon(getResources().getDrawable(R.drawable.ic_location));
            mapView.getOverlayManager().add(currentLocationMarker);

            // Cập nhật trung tâm của bản đồ đến vị trí hiện tại
            mapView.getController().setCenter(currentGeoPoint);
        }

    }

    public void getWeather(){
        loginService = RetrofitClientInstance.getClient().create(Service_Interface.class);
        Call<weatherHTTP> call = loginService.getatribute("metric","63971ca355e720759dffc1bcedc8edb2", 106.8031F, 10.8698F);

        call.enqueue(new Callback<weatherHTTP>() {
            @Override
            public void onResponse(Call<weatherHTTP> call, Response<weatherHTTP> response) {
                if (response.isSuccessful()) {
                    weatherHTTP weather = response.body();
                    Weather firstWeather = weather.weather.get(0);
                    name.setText(weather.name);

                }else {
                    Toast.makeText(getContext(), "Get Default Weather failt", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<weatherHTTP> call, Throwable t) {
                Toast.makeText(getContext(), "Login not correct:( ", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getAsset(){
        serviceInterface = RetrofitClientInstance.getClient1(tokenadmin).create(Service_Interface.class);
        Call<Asset> calll = serviceInterface.getAsset("5zI6XqkQVSfdgOrZ1MyWEf");
        calll.enqueue(new Callback<Asset>() {
            @Override
            public void onResponse(Call<Asset> call, Response<Asset> response) {
                if (response.isSuccessful()) {
                    Asset asset = response.body();
                    temp.setText(String.valueOf(asset.attributes.temperature.value));
                    humidity1.setText(String.valueOf(asset.attributes.humidity.value));
                    rainfall.setText(String.valueOf(asset.attributes.rainfall.value));

                }else {
                    Toast.makeText(getContext(), "Get Default Weather fail", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Asset> call, Throwable t) {

            }
        });
    }

    public void getAirAsset(){
        iService = RetrofitClientInstance.getClient1(tokenadmin).create(Service_Interface.class);
        Call<AirAsset> call = iService.getAsset1("6Wo9Lv1Oa1zQleuRVfADP4");
        call.enqueue(new Callback<AirAsset>() {
            @Override
            public void onResponse(Call<AirAsset> call, Response<AirAsset> response) {
                if (response.isSuccessful()) {
                    AirAsset asset = response.body();
                    aqi = asset.attributes.AQI.value;
                    ass_aiq1.setText(String.valueOf(aqi));
                    ass_Co2.setText(String.valueOf(asset.attributes.CO2.value));
                    ass_pm25.setText(String.valueOf(asset.attributes.PM25.value));
                    NO.setText(String.valueOf(asset.attributes.NO.value));

                }else {
                    Toast.makeText(getContext(), "Get Default Weather fail", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<AirAsset> call, Throwable t) {
                Log.e("API Error", "onFailure", t);
                Toast.makeText(getContext(), "API Call Failed: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}

