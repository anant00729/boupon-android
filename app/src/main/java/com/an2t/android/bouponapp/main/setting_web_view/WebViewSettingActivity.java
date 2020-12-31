package com.an2t.android.bouponapp.main.setting_web_view;

import android.os.Bundle;

import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.an2t.android.bouponapp.R;

public class WebViewSettingActivity extends AppCompatActivity {

    private WebView web_view_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_setting);

//        initcomponent
        initcomponent();
    }

    private void initcomponent() {
        web_view_settings = (WebView)findViewById(R.id.web_view_settings);

        web_view_settings.loadUrl("http://boupon.com.bd/faq");
    }
}
