package com.example.indoorairqualitymonitoring.mainfragment;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.indoorairqualitymonitoring.API.RetrofitClientInstance;
import com.example.indoorairqualitymonitoring.API.Service_Interface;
import com.example.indoorairqualitymonitoring.LoginActivity;
import com.example.indoorairqualitymonitoring.R;
import com.example.indoorairqualitymonitoring.module.login;
import com.example.indoorairqualitymonitoring.module.profile;
import com.example.indoorairqualitymonitoring.module.weatherHTTP;
import com.google.android.material.imageview.ShapeableImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragment extends Fragment {

    private EditText email;
    private EditText createdon;
    private Date date;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textview = view.findViewById(R.id.usernamemain);
        Button logout = view.findViewById(R.id.logout);

        Bundle bundle = getArguments();
        String token = getArguments().getString("token");
        String username = getArguments().getString("username");
        email = view.findViewById(R.id.editTextEmail);
        createdon = view.findViewById(R.id.etdate);

        Service_Interface serviceInterface = RetrofitClientInstance.getClient1(token).create(Service_Interface.class);
        Call<profile> call = serviceInterface.getProfile();

        call.enqueue(new Callback<profile>() {
            @Override
            public void onResponse(Call<profile> call, Response<profile> response) {
                if(response.isSuccessful()){
                    profile profile = response.body();
                    date = new Date(profile.createdOn);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    String formattedDate = dateFormat.format(date);
                    createdon.setText(formattedDate);
                    email.setText(profile.email);
                    textview.setText(profile.username);
                }
            }

            @Override
            public void onFailure(Call<profile> call, Throwable t) {

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                String logoutSuccessful = getString(R.string.logoutSuccessful);
                Toast.makeText(getActivity(), logoutSuccessful, Toast.LENGTH_SHORT).show();
            }
        });

    }
}