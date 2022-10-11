package com.example.kirshokerbondhu.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.kirshokerbondhu.R;

public class Weather extends AppCompatActivity {

    private WebView web_weather;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        web_weather = findViewById(R.id.web_weather);


        WebSettings webSettings=web_weather.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUserAgentString(
                "Mozilla/5.0 (Windows NT 6.1; rv:13.0) Gecko/20100101 Firefox/12");

        web_weather.canGoBack();
        web_weather.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        web_weather.canGoForward();
        web_weather.loadUrl("https://www.accuweather.com/bn/bd/chittagong/27822/weather-forecast/27822");
//        web_weather.loadUrl("https://www-ventusky-com.translate.goog/22.331;91.841?_x_tr_sl=en&_x_tr_tl=bn&_x_tr_hl=en&_x_tr_pto=wapp");
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class).putExtra("type", "usual"));
        finish();
    }
}