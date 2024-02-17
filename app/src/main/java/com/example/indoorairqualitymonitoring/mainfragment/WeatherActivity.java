package com.example.indoorairqualitymonitoring.mainfragment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.indoorairqualitymonitoring.API.RetrofitClientInstance;
import com.example.indoorairqualitymonitoring.API.Service_Interface;
import com.example.indoorairqualitymonitoring.R;
import com.example.indoorairqualitymonitoring.module.Asset;
import com.example.indoorairqualitymonitoring.module.Weather;
import com.example.indoorairqualitymonitoring.module.weatherHTTP;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity extends AppCompatActivity {
    private ImageView ic_back;
    private Service_Interface iService;
    public String token;
    private TextView des;
    private TextView tempt;
    private TextView winspeed;
    private TextView widir;
    private TextView humid;
    private TextView rainn;
    private TextView place1;
    private TextView windir;

    private ImageView ic_ac1;
    private ImageView ic_ac2;

    private TextView ac1;
    private TextView ac2;

    private ImageView ic_weather;

    private TextView date;


    private org.osmdroid.views.MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        Intent intent = getIntent();
        String tokenadmin = intent.getStringExtra("tokenadmin");
        token = intent.getStringExtra("token");

        ic_back = findViewById(R.id.ic_back);
        des = findViewById(R.id.des);
        tempt = findViewById(R.id.tempt);
        winspeed = findViewById(R.id.winspeed);
        humid = findViewById(R.id.humid);
        rainn = findViewById(R.id.rainn);
        place1 = findViewById(R.id.place_weather);
        ic_ac1 = findViewById(R.id.ic_activity1);
        ic_ac2 = findViewById(R.id.ic_activity2);
        ac1 = findViewById(R.id.actvity1);
        ac2 = findViewById(R.id.activity2);
        date = findViewById(R.id.date);
        ic_weather = findViewById(R.id.ic_weather);

        windir = findViewById(R.id.widir);

        Date currentDate = Calendar.getInstance().getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, d MMM yy | hh:mm a", Locale.getDefault());
        String formattedDate = dateFormat.format(currentDate);

        // Gán ngày, thứ và giờ vào TextView
        date.setText(formattedDate);

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherActivity.this, MainActivity.class);
                intent.putExtra("tokenadmin", tokenadmin);
                intent.putExtra("token", token);
                startActivity(intent);
                overridePendingTransition(R.drawable.slide_in_right, R.drawable.slide_out_right);
            }
        });
        getAsset(tokenadmin);
        getWeather();

        Configuration.getInstance().load(WeatherActivity.this, WeatherActivity.this.getSharedPreferences("osmdroid", Context.MODE_PRIVATE));
        // Tạo MapView
        mapView = findViewById(R.id.mapVieww1);
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
    }

    public void getAsset(String token){
        iService = RetrofitClientInstance.getClient1(token).create(Service_Interface.class);
        Call<Asset> calll = iService.getAsset("5zI6XqkQVSfdgOrZ1MyWEf");
        calll.enqueue(new Callback<Asset>() {
            @Override
            public void onResponse(Call<Asset> call, Response<Asset> response) {
                if (response.isSuccessful()) {
                    Asset asset = response.body();
                    float rainfall = asset.attributes.rainfall.value;
                    float weather1 = asset.attributes.temperature.value;
                    tempt.setText(String.valueOf(asset.attributes.temperature.value) + "°C");
                    float wind = asset.attributes.windSpeed.value;
                    winspeed.setText(String.valueOf(wind)+" km/h");
                    humid.setText(String.valueOf(asset.attributes.humidity.value) + "%");
                    rainn.setText(String.valueOf(asset.attributes.rainfall.value) + " mm");
                    place1.setText(String.valueOf(asset.attributes.place.value));
                    windir.setText(String.valueOf(asset.attributes.windDirection.value));


                    if(rainfall == 0){
                        ic_ac2.setImageResource(R.drawable.clear_sky);
                        ac2.setText("Thời tiết khô ráo, bạn nên tham gia hoạt động ngoài trời.");
                    }else if (rainfall >= 0.1 && rainfall <= 10) {
                        ic_ac2.setImageResource(R.drawable.heavy_rain);
                        ac2.setText("Có thể có mưa nhẹ");
                    }else if (rainfall >= 11 && rainfall <= 25) {
                        ic_ac2.setImageResource(R.drawable.rainy_we);
                        ic_weather.setImageResource(R.drawable.rainy);
                        ac2.setText("Mưa vừa hãy mang ô khi ra khỏi nhà");
                    }else if (rainfall >= 26 && rainfall <= 50) {
                        ic_ac2.setImageResource(R.drawable.rainy_we);
                        ic_weather.setImageResource(R.drawable.rainy);
                        ac2.setText("Mưa khá nặng, tránh ra khỏi nhà và có thể có nguy cơ lũ lụt");
                    }else if (rainfall >= 50) {
                        ic_ac2.setImageResource(R.drawable.heavy_rain);
                        ic_weather.setImageResource(R.drawable.rainy);
                        ac2.setText("Mưa Rất Nặng bạn tuyệt đối tránh ra khỏi nhà, nguy cơ ngập lục có thể tăng cao. ");
                    }

                    if (weather1 == 0){
                        ac1.setText("Mặc ấm và trang bị đủ quần áo ấm");
                        ic_ac1.setImageResource(R.drawable.sun);
                    }else if (weather1 >= 1 && weather1 <= 10) {
                        ic_ac1.setImageResource(R.drawable.sun);
                        ac1.setText("Mặc ấm với áo khoác và giữ ấm bằng cách sử dụng nón và găng tay");
                    }else if (weather1 >= 11 && weather1 <= 20) {
                        ic_ac1.setImageResource(R.drawable.sun);
                        ac1.setText("Thời tiết Mát mẻ, nếu bạn làm hoạt động ngoại ô, hãy sử dụng kem chống nắng");
                    }else if (weather1 >= 21 && weather1 <= 30) {
                        ac1.setText("Thời tiết ấm áp, Thích hợp với các họạt động ngoại ô");
                        ic_ac1.setImageResource(R.drawable.sun);
                    }else if (weather1 >= 31) {
                        ac1.setText("Thới tiết khá nóng, uống nước nhiều để tránh bị mất nước.");
                        ic_ac1.setImageResource(R.drawable.sun);
                    }

                }else {
                    Toast.makeText(WeatherActivity.this, "Get Default Weather fail", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Asset> call, Throwable t) {

            }
        });
    }

    public void getWeather(){
        Service_Interface loginService = RetrofitClientInstance.getClient().create(Service_Interface.class);
        Call<weatherHTTP> call = loginService.getatribute("metric","63971ca355e720759dffc1bcedc8edb2", 106.8031F, 10.8698F);

        call.enqueue(new Callback<weatherHTTP>() {
            @Override
            public void onResponse(Call<weatherHTTP> call, Response<weatherHTTP> response) {
                if (response.isSuccessful()) {
                    weatherHTTP weather = response.body();
                    Weather firstWeather = weather.weather.get(0);
                    des.setText(firstWeather.description);
                }
            }

            @Override
            public void onFailure(Call<weatherHTTP> call, Throwable t) {
            }
        });
    }
}