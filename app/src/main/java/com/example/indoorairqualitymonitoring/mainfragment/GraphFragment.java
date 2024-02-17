package com.example.indoorairqualitymonitoring.mainfragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.indoorairqualitymonitoring.API.RetrofitClientInstance;
import com.example.indoorairqualitymonitoring.API.Service_Interface;
import com.example.indoorairqualitymonitoring.R;
import com.example.indoorairqualitymonitoring.module.PeriodsAsset;
import com.example.indoorairqualitymonitoring.module.WeatherAsset;
import com.example.indoorairqualitymonitoring.module.WeatherAssetReq;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GraphFragment extends Fragment {
    private String[] arrayAtribute = {"AQI_predict", "AQI", "CO2_average", "CO2", "NO", "NO2", "O3", "PM10", "PM25", "SO2"};
    private String[] arrayTime = {"Hour", "Day", "Week", "Month", "Year"};
    private ArrayAdapter arrayAdapterAtribute, arrayAdapterTime;
    private AutoCompleteTextView autoCompleteAtribute, autoCompleteTime;
    private LineChart lineChart;
    private Service_Interface iService;
    private TextView calendarTV;
    long currentTimeMillis;
    private String datetimecurrent = "";
    private List<String> xValues;
    WeatherAssetReq weatherAssetReq = new WeatherAssetReq();
    private int year, month, date, hour, minute;
    private AppCompatButton appCompatButton;
    String atribute;
    String selectedTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_graph, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String token = getArguments().getString("tokenadmin");

        iService = RetrofitClientInstance.GetClientWeather(token).create(Service_Interface.class);
        autoCompleteAtribute = view.findViewById(R.id.dropdown_field);
        autoCompleteTime = view.findViewById(R.id.dropdown_field_time);
        appCompatButton = view.findViewById(R.id.appCompatButton);
        lineChart = view.findViewById(R.id.linechart);
        calendarTV = view.findViewById(R.id.text);

        arrayAdapterAtribute = new ArrayAdapter<>(requireContext(), R.layout.item_dropdown, arrayAtribute);
        autoCompleteAtribute.setAdapter(arrayAdapterAtribute);

        arrayAdapterTime = new ArrayAdapter<>(requireContext(), R.layout.item_dropdown, arrayTime);
        autoCompleteTime.setAdapter(arrayAdapterTime);

        lineChart.getAxisRight().setDrawLabels(false);


        String date1 = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        currentTimeMillis = convertDateStr2Millis(date1);
        calendarTV.setText(new SimpleDateFormat("dd-MM-yyyy hh:mm").format(new Date()));

        calendarTV.setOnClickListener(v -> {
            openTimePicker();
            openDatePicker();
        });

        autoCompleteTime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedTime = parent.getItemAtPosition(position).toString();
            }
        });


        appCompatButton.setOnClickListener(v -> {
            if (atribute == null) {
                Toast.makeText(getContext(), "Vui lòng trọng thuộc tính trước khi chọn thời gian", Toast.LENGTH_SHORT).show();
                return;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, date, hour, minute);
            long endingtime = calendar.getTimeInMillis();
            autoCompleteTime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String selectedTimeframe = adapterView.getItemAtPosition(i).toString();

                    if
                    (selectedTimeframe.equals("Hour")) {
                        weatherAssetReq.setFromTimestamp(endingtime - (3600000L));
                        weatherAssetReq.setToTimestamp(endingtime);
                    } else if (selectedTimeframe.equals("Day")) {
                        weatherAssetReq.setFromTimestamp(endingtime - (86400000L));
                        weatherAssetReq.setToTimestamp(endingtime);
                    } else if (selectedTimeframe.equals("Week")) {
                        weatherAssetReq.setFromTimestamp(endingtime - (86400000L * 7));
                        weatherAssetReq.setToTimestamp(endingtime);
                    } else if (selectedTimeframe.equals("Month")) {
                        weatherAssetReq.setFromTimestamp(endingtime - (86400000L * 30));
                        weatherAssetReq.setToTimestamp(endingtime);
                    } else if (selectedTimeframe.equals("Year")) {
                        weatherAssetReq.setFromTimestamp(endingtime - (86400000L * 365));
                        weatherAssetReq.setToTimestamp(endingtime);
                    }
                }
            });


            Call<List<WeatherAsset>> call = iService.getDataset(atribute, weatherAssetReq);
            call.enqueue(new Callback<List<WeatherAsset>>() {
                @Override
                public void onResponse(Call<List<WeatherAsset>> call, Response<List<WeatherAsset>> response) {
                    //Call api thành công thực hiện các bước setdata cho linechart

                    List<Entry> entries = new ArrayList<>();
                    xValues = new ArrayList<>();
                    for (int index = 0; index < response.body().size(); index++) {

                        entries.add(new Entry(index, (float) response.body().get(index).getY()));

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(response.body().get(index).getX());

                        int mYear = calendar.get(Calendar.YEAR);
                        int mMonth = calendar.get(Calendar.MONTH);
                        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
                        int mhouse = calendar.get(Calendar.HOUR_OF_DAY);
                        int minute = calendar.get(Calendar.MINUTE);

                        //Insert Label
                        xValues.add(mDay + "/" + (mMonth + 1) + "/" + mYear + "/" + mhouse + ":" + minute);
                    }

                    //set các thuộc tính cho lable
                    XAxis xAxis = lineChart.getXAxis();
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setLabelRotationAngle(45);
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(xValues));
                    xAxis.setLabelCount(xValues.size());
                    xAxis.setGranularity(1F);


                    //Setcacscs thuộc tính cột y
                    YAxis yAxis = lineChart.getAxisLeft();
                    yAxis.setAxisLineWidth(2f);
                    yAxis.setAxisLineColor(Color.BLACK);
                    yAxis.setLabelCount(xValues.size());

                    LineDataSet dataSet = new LineDataSet(entries, atribute);
                    dataSet.setColor(Color.GREEN);
                    dataSet.setCircleColor(Color.MAGENTA);

                    LineData lineData = new LineData(dataSet);
                    lineChart.setData(lineData);

                    lineChart.invalidate();
                }

                @Override
                public void onFailure(Call<List<WeatherAsset>> call, Throwable t) {
                    Log.d("sdsd", t.toString());
                }
            });

        });

        //Event khi chọn selected item atribute
        autoCompleteAtribute.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                atribute = adapterView.getItemAtPosition(i).toString();

                weatherAssetReq.setAmountOfPoints(100); // 1
                weatherAssetReq.setToTimestamp(currentTimeMillis);//3
                weatherAssetReq.setType("lttb");//4

                Call<PeriodsAsset> callperiods = iService.getPeriod("6Wo9Lv1Oa1zQleuRVfADP4", atribute, "Bearer " + token);

                callperiods.enqueue(new Callback<PeriodsAsset>() {
                    @Override
                    public void onResponse(Call<PeriodsAsset> call, Response<PeriodsAsset> response) {
                        long fromtime = response.body().getOldestTimestamp();
                        weatherAssetReq.setFromTimestamp(fromtime);//2
                        Call<List<WeatherAsset>> calldata = iService.getDataset(atribute, weatherAssetReq);

                        calldata.enqueue(new Callback<List<WeatherAsset>>() {
                            @Override
                            public void onResponse(Call<List<WeatherAsset>> call, Response<List<WeatherAsset>> response) {
                                //Call api thành công thực hiện các bước setdata cho linechart

                                List<Entry> entries = new ArrayList<>();
                                xValues = new ArrayList<>();
                                for (int index = 0; index < response.body().size(); index++) {

                                    entries.add(new Entry(index, (float) response.body().get(index).getY()));

                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTimeInMillis(response.body().get(index).getX());

//                            int mYear = calendar.get(Calendar.YEAR);
//                            int mMonth = calendar.get(Calendar.MONTH);
//                            int mDay = calendar.get(Calendar.DAY_OF_MONTH);
                                    int mhouse = calendar.get(Calendar.HOUR_OF_DAY);
                                    int minute = calendar.get(Calendar.MINUTE);

                                    //Insert Label
                                    xValues.add(mhouse + ":" + minute);
                                }

                                //set các thuộc tính cho lable
                                XAxis xAxis = lineChart.getXAxis();
                                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                                xAxis.setLabelRotationAngle(45);
                                xAxis.setValueFormatter(new IndexAxisValueFormatter(xValues));
                                xAxis.setLabelCount(xValues.size());
                                xAxis.setGranularity(1F);


                                //Setcacscs thuộc tính cột y
                                YAxis yAxis = lineChart.getAxisLeft();
                                yAxis.setAxisLineWidth(2f);
                                yAxis.setAxisLineColor(Color.BLACK);
                                yAxis.setLabelCount(xValues.size());

                                LineDataSet dataSet = new LineDataSet(entries, atribute);
                                dataSet.setColor(Color.CYAN);


                                LineData lineData = new LineData(dataSet);
                                lineChart.setData(lineData);

                                lineChart.invalidate();
                            }

                            @Override
                            public void onFailure(Call<List<WeatherAsset>> call, Throwable t) {
                                Log.d("sdsd", t.toString());
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<PeriodsAsset> call, Throwable t) {

                    }
                });


            }
        });
        autoCompleteAtribute.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                atribute = adapterView.getItemAtPosition(i).toString();
                weatherAssetReq.setAmountOfPoints(100); // 1
                weatherAssetReq.setToTimestamp(currentTimeMillis);//3
                weatherAssetReq.setType("lttb");//4
                Call<PeriodsAsset> callperiods = iService.getPeriod("6Wo9Lv1Oa1zQleuRVfADP4", atribute, "Bearer " + token);
                callperiods.enqueue(new Callback<PeriodsAsset>() {
                    @Override
                    public void onResponse(Call<PeriodsAsset> call, Response<PeriodsAsset> response) {
                        long fromtime = response.body().getOldestTimestamp();
                        weatherAssetReq.setFromTimestamp(fromtime);//2
                        Call<List<WeatherAsset>> calldata = iService.getDataset(atribute, weatherAssetReq);
                        calldata.enqueue(new Callback<List<WeatherAsset>>() {
                            @Override
                            public void onResponse(Call<List<WeatherAsset>> call, Response<List<WeatherAsset>> response) {
                                List<Entry> entries = new ArrayList<>();
                                xValues = new ArrayList<>();
                                for (int index = 0; index < response.body().size(); index++) {

                                    entries.add(new Entry(index, (float) response.body().get(index).getY()));

                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTimeInMillis(response.body().get(index).getX());
                                    int mhouse = calendar.get(Calendar.HOUR_OF_DAY);
                                    int minute = calendar.get(Calendar.MINUTE);
                                    xValues.add(mhouse + ":" + minute);
                                }
                                XAxis xAxis = lineChart.getXAxis();
                                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                                xAxis.setLabelRotationAngle(45);
                                xAxis.setValueFormatter(new IndexAxisValueFormatter(xValues));
                                xAxis.setLabelCount(xValues.size());
                                xAxis.setGranularity(1F);

                                YAxis yAxis = lineChart.getAxisLeft();
                                yAxis.setAxisLineWidth(2f);
                                yAxis.setAxisLineColor(Color.BLACK);
                                yAxis.setLabelCount(xValues.size());

                                LineDataSet dataSet = new LineDataSet(entries, atribute);
                                dataSet.setColor(Color.GREEN);
                                dataSet.setCircleColor(Color.RED);

                                LineData lineData = new LineData(dataSet);
                                lineChart.setData(lineData);

                                lineChart.invalidate();
                            }

                            @Override
                            public void onFailure(Call<List<WeatherAsset>> call, Throwable t) {
                                Log.d("sdsd", t.toString());
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<PeriodsAsset> call, Throwable t) {
                    }
                });
            }
        });
    }

    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                //Showing the picked value in the textView
                datetimecurrent = String.valueOf(y) + "." + String.valueOf(m + 1) + "." + String.valueOf(d);
                //calendarTV.setText(String.valueOf(year)+ "."+String.valueOf(month)+ "."+String.valueOf(day));
                year = y;
                month = m;
                date = d;
            }
        }, currentYear, currentMonth, currentDay);

        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFC0CB")));
        datePickerDialog.show();
    }

    //Open dialog chọn time
    private void openTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int h, int m) {
                datetimecurrent += "  " + String.valueOf(h) + ":" + String.valueOf(m);
                calendarTV.setText(datetimecurrent);
                hour = h;
                minute = m;
                //Showing the picked value in the textView
                //textView.setText(String.valueOf(hour)+ ":"+String.valueOf(minute));
            }
        }, currentHour, currentMinute, false);

        timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFC0CB")));
        timePickerDialog.show();
    }

    //Convert to miliseconds
    public static long convertDateStr2Millis(String date) {
        if (date == null || date.trim().length() == 0)
            return -1;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            return simpleDateFormat.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }
}