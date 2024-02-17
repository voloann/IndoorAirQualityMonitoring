package com.example.indoorairqualitymonitoring;

import androidx.appcompat.app.AppCompatActivity;

import android.text.InputType;
import android.widget.Button;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Patterns;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

import com.google.gson.annotations.SerializedName;

public class RegisterActivity extends ChangeLanguage {
    private EditText user;
    private EditText email;
    private EditText password;
    private EditText password2;
    private Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupLanguageButton(R.id.ic_language);

        user = findViewById(R.id.user);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        password2 = findViewById(R.id.password2);
        signUp = findViewById(R.id.singup);

        visibility(password);
        visibility(password2);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = user.getText().toString().trim();
                String emailAddress = email.getText().toString().trim();
                String passwordValue = password.getText().toString().trim();
                String confirmPassword = password2.getText().toString().trim();

                if (username.isEmpty() || emailAddress.isEmpty() || passwordValue.isEmpty() || confirmPassword.isEmpty()) {
                    String fullInformation = getString(R.string.fullInformation);
                    Toast.makeText(RegisterActivity.this, fullInformation, Toast.LENGTH_SHORT).show();
                    if (username.length() == 0) {
                        String enterUsername = getString(R.string.enterUsername);
                        user.setError(enterUsername);
                    }
                    if (emailAddress.length() == 0) {
                        String enterEmail = getString(R.string.enterEmail);
                        email.setError(enterEmail );
                    }
                    if (passwordValue.length() == 0) {
                        String enterPassword = getString(R.string.enterPassword);
                        password.setError(enterPassword);
                    }
                    if (confirmPassword.length() == 0) {
                        String enterRePassword = getString(R.string.enterRePassword);
                        password2.setError(enterRePassword);
                    }
                    return;
                }else if (passwordValue.length() < 8) {
                    String passWordLeast = getString(R.string.passWordLeast);
                    password.setError(passWordLeast);
                    Toast.makeText(RegisterActivity.this, passWordLeast, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(username.length() < 5){
                    String usernameLeast = getString(R.string.usernameLeast);
                    user.setError(usernameLeast);
                    Toast.makeText(RegisterActivity.this, usernameLeast, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
                    String emailInvalid = getString(R.string.emailInvalid);
                    Toast.makeText(RegisterActivity.this, emailInvalid, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!passwordValue.equals(confirmPassword)) {
                    String passwordFail = getString(R.string.passwordFail);
                    Toast.makeText(RegisterActivity.this, passwordFail, Toast.LENGTH_SHORT).show();
                    return;
                }



                Intent intent = new Intent();
                intent.setClass(RegisterActivity.this, EmbeddedWebView.class);

                intent.putExtra("username", user.getText().toString().trim());
                intent.putExtra("email", email.getText().toString().trim());
                intent.putExtra("password", password.getText().toString().trim());
                intent.putExtra("rePassword", password2.getText().toString().trim());

                startActivity(intent);
            }
        });

        Button btnBack = findViewById(R.id.back);

        // Xu ly button Back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, HomepageActivity.class);
                startActivity(intent);
            }
        });
    }

    private void visibility(EditText epassword1){
        epassword1.setOnClickListener(new View.OnClickListener() {
            boolean passwordVisible = false;

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