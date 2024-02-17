package com.example.indoorairqualitymonitoring.mainfragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.indoorairqualitymonitoring.API.RetrofitClientInstance;
import com.example.indoorairqualitymonitoring.API.Service_Interface;
import com.example.indoorairqualitymonitoring.R;
import com.example.indoorairqualitymonitoring.module.AirAsset;
import com.example.indoorairqualitymonitoring.module.Asset;
import com.example.indoorairqualitymonitoring.module.Weather;
import com.example.indoorairqualitymonitoring.module.weatherHTTP;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HealthyFragment extends Fragment {
    TextView kk;
    private Service_Interface iService;

    private TextView date;
    private TextView place;


    private TextView AQI_va;
    private TextView pm25_va;
    private TextView co2_va;
    private TextView o3_val;
    private TextView no2_val;

    private TextView aqi_des;
    private TextView aqi_danhgia;
    private TextView pm_des;
    private TextView pm_danhgia;
    private TextView co2_des;
    private TextView co2_danhgia;
    private TextView o3_des;
    private TextView o3_danhgia;
    private TextView no2_des;
    private TextView no2_danhgia;

    private TextView temp_va;
    private TextView rainfall_va;

    private TextView rainfall_des;
    private TextView temp_danhgia;
    private TextView temp_des;
    private TextView raindall_danhgia;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_healthy, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String token = getArguments().getString("tokenadmin");
            date = view.findViewById(R.id.date);
            place = view.findViewById(R.id.place);

//            air
            AQI_va = view.findViewById(R.id.AQI_va);
            pm25_va = view.findViewById(R.id.pm25_va);
            co2_va = view.findViewById(R.id.co2_va);
            o3_val = view.findViewById(R.id.o3_val);
            no2_val = view.findViewById(R.id.no2_val);

            aqi_des = view.findViewById(R.id.aqi_des);
            aqi_danhgia = view.findViewById(R.id.aqi_danhgia);
            pm_des = view.findViewById(R.id.pm_des);
            pm_danhgia = view.findViewById(R.id.pm_danhgia);
            co2_des = view.findViewById(R.id.co2_des);
            co2_danhgia = view.findViewById(R.id.co2_danhgia);
            o3_des = view.findViewById(R.id.o3_des);
            o3_danhgia = view.findViewById(R.id.o3_danhgia);
            no2_des = view.findViewById(R.id.no2_des);
            no2_danhgia = view.findViewById(R.id.no2_danhgia);

            temp_danhgia = view.findViewById(R.id.temp_danhgia);
            temp_des = view.findViewById(R.id.temp_des);
            raindall_danhgia = view.findViewById(R.id.rainfall_danhgia);
            rainfall_des = view.findViewById(R.id.rainfall_des);


//            weather
            temp_va = view.findViewById(R.id.temp_va);
            rainfall_va = view.findViewById(R.id.rainfall_va);



            Date currentDate = Calendar.getInstance().getTime();

            SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, d MMM yy | hh:mm a", Locale.getDefault());
            String formattedDate = dateFormat.format(currentDate);

            // Gán ngày, thứ và giờ vào TextView
            date.setText(formattedDate);

            getAirAsset(token);
            getAsset(token);


    }

    public void getAsset(String token){
        iService = RetrofitClientInstance.getClient1(token).create(Service_Interface.class);
        Call<Asset> calll = iService.getAsset("5zI6XqkQVSfdgOrZ1MyWEf");
        calll.enqueue(new Callback<Asset>() {
            @Override
            public void onResponse(Call<Asset> call, Response<Asset> response) {
                if (response.isSuccessful()) {
                    Asset asset = response.body();
                    float temp = asset.attributes.temperature.value;
                    int humi = asset.attributes.humidity.value;
                    float rainfall = asset.attributes.rainfall.value;
                    place.setText(asset.attributes.place.value);
                    temp_va.setText(String.valueOf(temp) + "°C");
                    rainfall_va.setText(String.valueOf(asset.attributes.rainfall.value)+ "mm");
//
                    if(rainfall == 0){
                        rainfall_des.setText("không mưa");
                        raindall_danhgia.setText("Thời tiết khô ráo, bạn có thể thoải mái hoạt động ngoại ô");
                    }else if (rainfall >= 0.1 && rainfall <= 10) {
                        rainfall_des.setText("Mưa nhẹ");
                        raindall_danhgia.setText("Hãy mang ô khi ra khỏi nhà");
                    }else if (rainfall >= 11 && rainfall <= 25) {
                        rainfall_des.setText("Mưa vừa");
                        raindall_danhgia.setText("Hãy mang ô khi ra khỏi nhà");
                    }else if (rainfall >= 26 && rainfall <= 50) {
                        rainfall_des.setText("Mưa nặng");
                        raindall_danhgia.setText("Tránh ra khỏi nhà nếu có thể");
                    }else if (rainfall >= 50) {
                        rainfall_des.setText("Mưa Rất Nặng hoặc Lũ Lớn");
                        raindall_danhgia.setText("Nguy cơ ngập lụt và sạt lở có thể tăng cao");
                    }

                    if (temp == 0){
                        temp_des.setText("Thời tiết rất lạnh");
                        temp_danhgia.setText("Mặc ấm và trang bị đủ quần áo ấm");
                    }else if (temp >= 1 && temp <= 10) {
                        temp_des.setText("Thời tiết lạnh");
                        temp_danhgia.setText("Mặc ấm và trang bị đủ quần áo ấm");
                    }else if (temp >= 11 && temp <= 20) {
                        temp_des.setText("Thời tiết Mát mẻ");
                        temp_danhgia.setText("Hãy tham gia các hoạt dộng ngoài trời");
                    }else if (temp >= 21 && temp <= 30) {
                        temp_des.setText("Thời tiết ấm áp");
                        temp_danhgia.setText("Bảo vệ da khỏi tác động của tia UV bằng cách sử dụng kem chống nắng");
                    }else if (temp >= 31) {
                        temp_des.setText("Thới tiết khá nóng");
                        temp_danhgia.setText("Tránh ánh nắng trực tiếp và giữ mình mát mẻ bằng cách sử dụng ô dù");
                    }

                }else {
                    Toast.makeText(getContext(), "Get Default Weather fail", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Asset> call, Throwable t) {

            }
        });
    }


    public void getAirAsset(String token){
        iService = RetrofitClientInstance.getClient1(token).create(Service_Interface.class);
        Call<AirAsset> call = iService.getAsset1("6Wo9Lv1Oa1zQleuRVfADP4");
        call.enqueue(new Callback<AirAsset>() {
            @Override
            public void onResponse(Call<AirAsset> call, Response<AirAsset> response) {
                if (response.isSuccessful()) {
                    AirAsset asset = response.body();

                    int AQI = asset.attributes.AQI.value;
                    float pm25 = asset.attributes.PM25.value;
                    int co2 = asset.attributes.CO2.value;
                    float o3 = asset.attributes.O3.value;
                    float no2 = asset.attributes.NO2.value;


                    AQI_va.setText(String.valueOf(AQI));
                    pm25_va.setText(String.valueOf(pm25));
                    co2_va.setText(String.valueOf(co2));
                    o3_val.setText(String.valueOf(o3));
                    no2_val.setText(String.valueOf(no2));

                    if (AQI >= 0 && AQI <= 50) {
                        aqi_des.setText("Không khí Tốt");
                    } else if (AQI >= 51 && AQI <= 100) {
                        aqi_des.setText("Không khí Trung bình");
                        aqi_danhgia.setText("Người nhạy cảm nên hạn chế tập thể dục ngoài trời");
                    } else if (AQI >= 101 && AQI <= 150) {
                        aqi_des.setText("Không khí Xấu");
                        aqi_danhgia.setText("Hạn chế mở cửa để tránh không khí bẩn từ bên ngoài vào");
                    } else if (AQI >= 151 && AQI <= 200) {
                        aqi_des.setText("Không khí Kém");
                        aqi_danhgia.setText("Nếu có thể hãy sử dụng máy lọc không khí để giảm bụi bẩn");
                    } else if (AQI >= 201 && AQI <= 300) {
                        aqi_des.setText("Không khí Rất kém");
                        aqi_danhgia.setText("Hạn chế tối đa việc ra khỏi nhà, đặc biệt là nếu bạn thuộc vào nhóm nhạy cảm như người già, trẻ em");
                    } else if(AQI >= 301) {
                        aqi_des.setText("Không khí Nguy hiểm");
                        aqi_danhgia.setText("Hạn chế tối đa việc ra khỏi nhà không khí đang rất nguy hiểm");
                    }

                    if(pm25 <= 12){
                        pm_des.setText("Không khí tốt");
                        pm_danhgia.setText("Chất lượng không khí trong khoảng này được coi là an toàn");
                    }else if(pm25 >= 13.1 && pm25 <= 35.4 ){
                        pm_des.setText("không khí trung bình");
                        pm_danhgia.setText("Không khí ở mức này có thể ảnh hưởng đến những người nhạy cảm như trẻ em, người già");
                    }else if(pm25 >= 35.5){
                        pm_des.setText("Không khí kém");
                        pm_danhgia.setText("Nên hạn chế thời gian ở ngoại ô và thực hiện biện pháp bảo vệ sức khỏe");
                    }

                    if(co2 >= 0 && co2 <= 400 ){
                        co2_des.setText("Lượng CO2 tốt");
                        co2_danhgia.setText("Chất lượng CO2 trong khoảng này được coi là an toàn");
                    }else if(co2 >= 401 && co2 <= 800 ){
                        co2_des.setText("Lượng CO2 trung bình");
                        co2_danhgia.setText("Chất lượng CO2 ở mức trung bình không gây nguy cơ đối với sức khỏe.");
                    }else if(co2 >= 801){
                        co2_des.setText("Lượng CO2 kém");
                        co2_danhgia.setText("Có thể ảnh hưởng đến sức khỏe, đặc biệt là các môi trường đóng cửa và không có đủ thông gió");
                    }

                    if(o3 >= 0 && o3 <= 50){
                        o3_des.setText("O3 ở mức tốt");
                        o3_danhgia.setText("Thực hiện các hoạt động ngoại ô một cách bình thường");
                    }else if(o3 >= 51 && o3 <= 100 ){
                        o3_des.setText("O3 ở mức trung bình");
                        o3_danhgia.setText("Nhóm nhạy cảm nên hạn chế thời gian ở ngoại ô, đặc biệt là vào buổi chiều");
                    }else if(o3 >= 101){
                        o3_des.setText("O3 ở mức kém");
                        o3_danhgia.setText("Thực hiện các biện pháp bảo vệ sức khỏe như duy trì không gian sạch sẽ");
                    }

                    if(no2 >= 0 && no2 <= 40){
                        no2_des.setText("NO2 ở mức tốt");
                        no2_danhgia.setText("Hãy ra ngoài và tập thể dục sẽ rất tốt cho sức khỏe");
                    }else if(no2 >= 41 && no2 <= 100 ){
                        no2_des.setText("O3 ở mức trung bình");
                        no2_danhgia.setText("Nhóm nhạy cảm nên hạn chế thời gian ở ngoại ô, đặc biệt là vào buổi chiều");
                    }else if(no2 >= 101){
                        no2_des.setText("O3 ở mức kém");
                        no2_danhgia.setText("Thực hiện các biện pháp bảo vệ sức khỏe như duy trì không gian sạch sẽ");
                    }

                }
            }
            @Override
            public void onFailure(Call<AirAsset> call, Throwable t) {
            }
        });
    }
}