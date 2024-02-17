package com.example.indoorairqualitymonitoring.mainfragment;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.indoorairqualitymonitoring.API.RetrofitClientInstance;
import com.example.indoorairqualitymonitoring.API.Service_Interface;
import com.example.indoorairqualitymonitoring.ChangeLanguage;
import com.example.indoorairqualitymonitoring.LoginActivity;
import com.example.indoorairqualitymonitoring.R;
import com.example.indoorairqualitymonitoring.module.Asset;
import com.example.indoorairqualitymonitoring.module.Weather;
import com.example.indoorairqualitymonitoring.module.login;
import com.example.indoorairqualitymonitoring.module.weatherHTTP;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends ChangeLanguage {

    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    AccountFragment accountFragment = new AccountFragment();
    GraphFragment graphFragment = new GraphFragment();
    HealthyFragment healthyFragment = new HealthyFragment();
    String tokenadmin;
    String place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        String token = intent.getStringExtra("token");
        String username = intent.getStringExtra("username");
        String tokenadmin = intent.getStringExtra("tokenadmin");

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        Bundle bundle = new Bundle();
        bundle.putString("tokenadmin", tokenadmin);
        bundle.putString("token", token);
        homeFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, homeFragment)
                            .addToBackStack(null)
                            .commit();
                    return true;
                } else if (item.getItemId() == R.id.graph) {
                    getSupportFragmentManager().beginTransaction().remove(graphFragment).commit();
                    graphFragment = new GraphFragment();
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("tokenadmin", tokenadmin);
                    graphFragment.setArguments(bundle1);
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, graphFragment).commit();
                    return true;
                }else if (item.getItemId() == R.id.Account) {
                    Bundle bundle = new Bundle();
                    bundle.putString("token", token);
                    bundle.putString("username", username);
                    accountFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, accountFragment)
                            .addToBackStack(null)
                            .commit();
                    return true;
                } else if (item.getItemId() == R.id.health) {
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("tokenadmin", tokenadmin);
                    healthyFragment.setArguments(bundle1);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, healthyFragment)
                            .addToBackStack(null)
                            .commit();
                    return true;
                }
                return true;
            }
        });
    }
}