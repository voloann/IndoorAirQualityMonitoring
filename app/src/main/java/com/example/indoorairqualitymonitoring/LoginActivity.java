package com.example.indoorairqualitymonitoring;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.example.indoorairqualitymonitoring.API.RetrofitClientInstance;
import com.example.indoorairqualitymonitoring.API.Service_Interface;
import com.example.indoorairqualitymonitoring.mainfragment.MainActivity;
import com.example.indoorairqualitymonitoring.module.login;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends ChangeLanguage {


    EditText eusername;
    EditText epassword;
    Button signin;


    public  static String tokenadmin;
    public static String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupLanguageButton(R.id.ic_language);

        eusername = findViewById(R.id.userlogin);
        epassword = findViewById(R.id.passwordlogin);
        signin = findViewById(R.id.singin1);

        Button btnBack = findViewById(R.id.back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, HomepageActivity.class);
                startActivity(intent);
            }
        });
        visibility(epassword);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textuser = eusername.getText().toString();
                String textpass = epassword.getText().toString();

                if (checkValidate(textuser, textpass) == true) {
                    loginRemote(textuser, textpass);
                }
            }
        });
    }


    public void loginRemote(String username, String password) {
        Service_Interface loginService = RetrofitClientInstance.getInstance().create(Service_Interface.class);
        Call<login> call = loginService.login1("openremote", username, password, "password");

        call.enqueue(new Callback<login>() {
            @Override
            public void onResponse(Call<login> call, Response<login> response) {
                if (response.isSuccessful()) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    String loginSuccessful = getString(R.string.loginSuccessful);
                    Toast.makeText(LoginActivity.this, loginSuccessful, Toast.LENGTH_SHORT).show();
                    token = response.body().getAccess_token();

                    Service_Interface loginService1 = RetrofitClientInstance.getInstance().create(Service_Interface.class);
                    Call<login> call1 = loginService1.login1("openremote", "user", "123", "password");

                    call1.enqueue(new Callback<login>() {
                        @Override
                        public void onResponse(Call<login> call, Response<login> response) {
                            if (response.isSuccessful()) {
                                tokenadmin = response.body().getAccess_token();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("tokenadmin",tokenadmin);
                                intent.putExtra("token", token);
                                intent.putExtra("username", username);
                                startActivity(intent);
                            } else {
                                String loginFail = getString(R.string.loginFail);
                                Toast.makeText(LoginActivity.this, loginFail, Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<login> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "Login not correct:( ", Toast.LENGTH_LONG).show();
                        }
                    });

                } else {
                    String loginFail = getString(R.string.loginFail);
                    Toast.makeText(LoginActivity.this, loginFail, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<login> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Login not correct:( ", Toast.LENGTH_LONG).show();
            }
        });
    }

    private Boolean checkValidate(String name, String pass) {
        if (name.length() == 0 || pass.length() == 0) {
            String fullInformation = getString(R.string.fullInformation);
            Toast.makeText(LoginActivity.this, fullInformation, Toast.LENGTH_SHORT).show();
            if (name.length() == 0) {
                String enterUsername = getString(R.string.enterUsername);
                eusername.setError(enterUsername);
            }
            if (pass.length() == 0) {
                String enterPassword = getString(R.string.enterPassword);
                epassword.setError(enterPassword);
            }
        } else {
            return true;
        }
        return false;
    }

    private void visibility(EditText epassword1){
        epassword1.setOnClickListener(new View.OnClickListener() {
            boolean passwordVisible = true;

            @Override
            public void onClick(View view) {
                passwordVisible = !passwordVisible;
                int inputType = passwordVisible
                        ? InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                        : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;

                epassword1.setInputType(inputType);

                // Move the cursor to the end of the text
                epassword1.setSelection(epassword1.getText().length());

            }
        });
    }
}