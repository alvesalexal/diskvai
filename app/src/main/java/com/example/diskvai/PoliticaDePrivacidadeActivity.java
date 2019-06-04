package com.example.diskvai;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class PoliticaDePrivacidadeActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_politica_de_privacidade);

        webView = (WebView) findViewById(R.id.webpage);
        webView.loadUrl("file:///android_asset/privacypolicy.html");
    }
}
