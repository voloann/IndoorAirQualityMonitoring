package com.example.indoorairqualitymonitoring;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.webkit.CookieManager;


public class EmbeddedWebView extends AppCompatActivity {
    private static final int CHECK_INTERVAL_MS = 500; // Every half a second
    private static final int TIMEOUT_MS = 60000; // 10 seconds
    private WebView webview;
    private ProgressBar progress;
    private boolean hasFormSubmitted = false;
    private String username;
    private String email;
    private String password;
    private String rePassword;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.embedded_web_view);
        progress = findViewById(R.id.progress);

        //Get data
        username = getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        rePassword = getIntent().getStringExtra("rePassword");

        //Configure webview setting
        webview = findViewById(R.id.webView);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.getSettings().setDomStorageEnabled(true);

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.i("WebView", "Page loaded: " + url);
                super.onPageFinished(view, url);

                if (url.contains("swagger")) {
                    clickButtonWhenAvailable(view, ".btn.authorize.unlocked");
                }

                if (url.contains("openid-connect/auth") || url.contains("login-actions/authenticate")) {
                    view.evaluateJavascript("document.querySelector('a.btn.waves-effect.waves-light').click();", null);
                }

                if (hasFormSubmitted) {
                    view.evaluateJavascript(
                            "(function() { \n" +
                                    " let emailError = document.querySelector('[data-error=\"Email already exists.\"]'); \n" +
                                    " let invalidEmail = document.querySelector('[data-error=\"Invalid email address.\"]'); \n" +
                                    " let passwordError = document.querySelector('[data-error=\"Password confirmation doesn\\'t match.\"]'); \n" +
                                    " let usernameError = document.querySelector('span.red-text');\n" +
                                    " if (emailError) return 'emailError';\n" +
                                    " else if (invalidEmail) return 'invalidEmail';\n" +
                                    " else if (usernameError) return 'usernameError';\n" +
                                    " else if (passwordError) return 'passwordError';\n" +
                                    " else return null\n" +
                                    " })();",
                            value -> {
                                Log.i("WebView", "hasFormSubmitted: " + url);
                                if (value != null && !value.equals("null")) {
                                    switch (value) {
                                        case "\"emailError\"":
                                            String emailExists = getString(R.string.emailExists);
                                            Toast.makeText(EmbeddedWebView.this, emailExists, Toast.LENGTH_SHORT).show();
                                            break;
                                        case "\"invalidEmail\"":
                                            String emailInvalid = getString(R.string.emailInvalid);
                                            Toast.makeText(EmbeddedWebView.this, emailInvalid, Toast.LENGTH_SHORT).show();
                                            break;
                                        case "\"usernameError\"":
                                            String usernameExists = getString(R.string.usernameExists);
                                            Toast.makeText(EmbeddedWebView.this, usernameExists, Toast.LENGTH_SHORT).show();
                                            break;
                                    }
                                } else {
                                    String signUpSuccessful = getString(R.string.signUpSuccessful);
                                    Toast.makeText(EmbeddedWebView.this, signUpSuccessful, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(EmbeddedWebView.this, LoginActivity.class);
                                    startActivity(intent);
                                }
                                finish();
                            });
                }

                if (url.contains("login-actions/registration")) {
                    Log.i("Webview", "Register: " + url);
                    String userScript = "document.getElementById('username').value = '" + username + "';";
                    String emailScript = "document.getElementById('email').value = '" + email + "';";
                    String pwdScript = "document.getElementById('password').value = '" + password + "';";
                    String rePwdScript = "document.getElementById('password-confirm').value = '" + rePassword + "';";

                    view.evaluateJavascript(userScript, null);
                    view.evaluateJavascript(emailScript, null);
                    view.evaluateJavascript(pwdScript, null);
                    view.evaluateJavascript(rePwdScript, null);
                    if (!hasFormSubmitted) {
                        view.evaluateJavascript("document.querySelector('button[name=\"register\"]').click();", null);
                        hasFormSubmitted = true;
                    }
                }
            }
        });

        // Rest the WebView
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookies(null);
        hasFormSubmitted = false;
        webview.clearHistory();
        webview.clearFormData();
        webview.clearCache(true);

        webview.loadUrl("https://uiot.ixxc.dev/swagger/#/");
    }

    private void clickButtonWhenAvailable(WebView webView, String selector) {
        final long startTime = System.currentTimeMillis();

        final Runnable checkInitialButtonExistence = new Runnable() {
            @Override
            public void run() {
                webView.evaluateJavascript(
                        "(function(selector) { return !!document.querySelector(selector); })('" + selector + "');",
                        value -> {
                            // If button exists or we've reached timeout, try clicking or exit
                            if ("true".equals(value) || System.currentTimeMillis() - startTime > TIMEOUT_MS) {
                                webView.evaluateJavascript("document.querySelector('" + selector + "').click();", null);
                                webView.evaluateJavascript("document.querySelector('.btn.modal-btn.auth.authorize.button').click();", null);
                            } else {
                                // Otherwise, keep checking
                                new Handler().postDelayed(this, CHECK_INTERVAL_MS);
                            }
                        }
                );
            }
        };
        // Start the checking
        new Handler().postDelayed(checkInitialButtonExistence, CHECK_INTERVAL_MS);
    }
}