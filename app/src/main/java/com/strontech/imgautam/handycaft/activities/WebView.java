package com.strontech.imgautam.handycaft.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.strontech.imgautam.handycaft.R;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class WebView extends AppCompatActivity {
private android.webkit.WebView web_view;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        web_view=  findViewById(R.id.web);
String url = getIntent().getStringExtra("url");
web_view.getSettings().setJavaScriptEnabled(true);
web_view.loadUrl(url);
        web_view.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(android.webkit.WebView view, String url, Bitmap favicon)
            {
        if (url.contains("google.com")){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
                super.onPageStarted(view, url, favicon);
            }

        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), pyActivity.class));
    }


}
