package com.example.indoorairqualitymonitoring.mainfragment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.indoorairqualitymonitoring.API.RetrofitClientInstance;
import com.example.indoorairqualitymonitoring.API.Service_Interface;
import com.example.indoorairqualitymonitoring.R;
import com.example.indoorairqualitymonitoring.module.AirAsset;
import com.example.indoorairqualitymonitoring.module.Asset;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AirActivity extends AppCompatActivity {

    private ImageView ic_back;
    private TextView assaqi;
    private Service_Interface iService;
    private TextView date;
    private org.osmdroid.views.MapView mapView;

    private TextView kk;
    private TextView pm25;
    private TextView pm10;
    private TextView o3;
    private TextView NO;
    private TextView co2;
    private TextView co2_aver;
    private TextView NO2;
    private TextView SO2;
    private TextView place_country;

    private ImageView ic_ac1;
    private ImageView ic_ac2;
    private ImageView ic_ac3;
    private TextView ac1;
    private TextView ac2;
    private TextView ac3;

    private TextView loadtime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air);

        assaqi = findViewById(R.id.aqi);
        pm25 = findViewById(R.id.pm25);
        pm10 = findViewById(R.id.pm10);
        o3 = findViewById(R.id.o3);
        NO = findViewById(R.id.NO);
        co2 = findViewById(R.id.co2);
        co2_aver = findViewById(R.id.co2_aver);
        NO2 = findViewById(R.id.NO2);
        SO2 = findViewById(R.id.SO2);
        kk = findViewById(R.id.kk);
        place_country = findViewById(R.id.place_country);

        ic_ac1 = findViewById(R.id.ic_activity1);
        ic_ac2 = findViewById(R.id.ic_activity2);
        ic_ac3 = findViewById(R.id.ic_activity3);
        ac1 = findViewById(R.id.actvity1);
        ac2 = findViewById(R.id.activity2);
        ac3 = findViewById(R.id.activity3);
        date = findViewById(R.id.date);
        ic_back = findViewById(R.id.ic_back);
        loadtime = findViewById(R.id.loadtime);
        Intent intent = getIntent();

        Date currentDate = Calendar.getInstance().getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, d MMM yy | hh:mm a", Locale.getDefault());
        String formattedDate = dateFormat.format(currentDate);

        // Gán ngày, thứ và giờ vào TextView
        date.setText(formattedDate);

        String tokenadmin = intent.getStringExtra("tokenadmin");
        String token = intent.getStringExtra("token");

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AirActivity.this, MainActivity.class);
                intent.putExtra("tokenadmin", tokenadmin);
                intent.putExtra("token",token);
                startActivity(intent);
                overridePendingTransition(R.drawable.slide_in_right, R.drawable.slide_out_right);
            }
        });

        getAirAsset(tokenadmin);
        getAsset(tokenadmin);

        Configuration.getInstance().load(AirActivity.this, AirActivity.this.getSharedPreferences("osmdroid", Context.MODE_PRIVATE));
        // Tạo MapView
        mapView = findViewById(R.id.mapVieww2);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        // Đặt trung tâm và zoom bản đồ
        GeoPoint centerPoint = new GeoPoint(10.869905172970164, 106.80345028525176);
        mapView.getController().setCenter(centerPoint);
        mapView.getController().setZoom(18);

        // Thêm Marker vào bản đồ
        OverlayItem markerItem = new OverlayItem("Default Weather", "Default Weather in Android", centerPoint);
        Marker marker = new Marker(mapView);
        Drawable drawable = getResources().getDrawable(R.drawable.ic_location2);
        marker.setPosition(centerPoint);
        marker.setIcon(drawable);
        marker.setTitle("Default Weather");
        mapView.getOverlayManager().add(marker);

    }

    public void getAirAsset(String token){
        iService = RetrofitClientInstance.getClient1(token).create(Service_Interface.class);
        Call<AirAsset> call = iService.getAsset1("6Wo9Lv1Oa1zQleuRVfADP4");
        call.enqueue(new Callback<AirAsset>() {
            @Override
            public void onResponse(Call<AirAsset> call, Response<AirAsset> response) {
                if (response.isSuccessful()) {
                    AirAsset asset = response.body();
                    int aqi = asset.attributes.AQI.value;

                    if (aqi >= 0 && aqi <= 50) {
                        kk.setText("Tốt");
                        ic_ac1.setImageResource(R.drawable.window);
                        ac1.setText("Mở cửa sổ đón nhận không khí tốt vào nhà");
                        ac2.setText("Tập thể dục, rèn luyện sức khỏe");
                        ic_ac2.setImageResource(R.drawable.mandoingpushups);
                        ac3.setText("Tham gia các hoạt động ngoài trời");
                    } else if (aqi >= 51 && aqi <= 100) {
                        ac1.setText("Người nhạy cảm hạn chế các hoạt đông ngoại ô");
                        ic_ac1.setImageResource(R.drawable.park);
                        ac2.setText("Người nhạy cảm nên đeo khẩu trang khi ra ngoài");
                        ic_ac2.setImageResource(R.drawable.mask);
                        ac3.setText("Người nhạy cảm nên hạn chế tập thể dục ngoài trời");
                        ic_ac3.setImageResource(R.drawable.mandoingpushups);
                        kk.setText("Trung bình");
                    } else if (aqi >= 101 && aqi <= 150) {
                        ac1.setText("Hạn chế mở cửa sổ");
                        ic_ac1.setImageResource(R.drawable.window);
                        ac2.setText("Nên đeo khẩu trang khi ra ngoài");
                        ic_ac2.setImageResource(R.drawable.mask);
                        ac3.setText("Hạn chế các hoạt động ngoại ô");
                        ic_ac3.setImageResource(R.drawable.park);
                        kk.setText("Xấu");
                    } else if (aqi >= 151 && aqi <= 200) {
                        ac1.setText("Tránh các hoạt động ngoại ô");
                        ic_ac1.setImageResource(R.drawable.park);
                        ac2.setText("Nên đeo khẩu trang khi ra ngoài");
                        ic_ac2.setImageResource(R.drawable.mask);
                        ac3.setText("Không tập thể dục ngoài trời");
                        ic_ac3.setImageResource(R.drawable.gym);
                        kk.setText("Kém");
                    } else if (aqi > 200) {
                        ac1.setText("Hạn chế tối đa việc ra khởi nhà");
                        ic_ac1.setImageResource(R.drawable.park);
                        ac2.setText("Sử dụng máy lọc không khí");
                        ic_ac2.setImageResource(R.drawable.air_filter);
                        ac3.setText("Không tập thể dục ngoài trời");
                        ic_ac3.setImageResource(R.drawable.gym);
                        kk.setText("Trung bình");
                        kk.setText("Rất kém");
                    }else if(aqi >= 301) {
                        kk.setText("Nguy Hiểm");
                    }
                    assaqi.setText(String.valueOf(aqi));
                    pm25.setText(String.valueOf(asset.attributes.PM25.value) + " µg/m³");
                    pm10.setText(String.valueOf(asset.attributes.PM10.value) + " µg/m³");
                    co2.setText(String.valueOf(asset.attributes.CO2.value) + " µg/m³");
                    co2_aver.setText(String.valueOf(asset.attributes.CO2_average.value) + " µg/m³");
                    NO.setText(String.valueOf(asset.attributes.NO.value) + " µg/m³");
                    NO2.setText(String.valueOf(asset.attributes.NO2.value) + " µg/m³");
                    SO2.setText(String.valueOf(asset.attributes.SO2.value) + " µg/m³");
                    o3.setText(String.valueOf(asset.attributes.O3.value) + " µg/m³");


                    Date date = new Date(asset.attributes.AQI.timestamp);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    String formattedDate = dateFormat.format(date);
                    loadtime.setText(formattedDate);


                }else {
                    Toast.makeText(AirActivity.this, "Get Default Weather fail", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<AirAsset> call, Throwable t) {
                Log.e("API Error", "onFailure", t);
                Toast.makeText(AirActivity.this, "API Call Failed: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getAsset(String token){
        iService = RetrofitClientInstance.getClient1(token).create(Service_Interface.class);
        Call<Asset> calll = iService.getAsset("5zI6XqkQVSfdgOrZ1MyWEf");
        calll.enqueue(new Callback<Asset>() {
            @Override
            public void onResponse(Call<Asset> call, Response<Asset> response) {
                if (response.isSuccessful()) {
                    Asset asset = response.body();
                    place_country.setText(asset.attributes.place.value);

                }else {
                    Toast.makeText(AirActivity.this, "Get Default Weather fail", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Asset> call, Throwable t) {

            }
        });
    }


}