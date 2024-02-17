package com.example.indoorairqualitymonitoring;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

public class HomepageActivity extends ChangeLanguage {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        setupLanguageButton(R.id.ic_language);

        // Xu ly nhan nut Dang Nhap
        Button btnSignin = findViewById(R.id.singin);
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomepageActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // Xu ly nhan nut Dang Ki
        Button btnSignup = findViewById(R.id.singup);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomepageActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        TextView resetPasswordTextView = findViewById(R.id.reset_your);
        resetPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomepageActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });

    }
}