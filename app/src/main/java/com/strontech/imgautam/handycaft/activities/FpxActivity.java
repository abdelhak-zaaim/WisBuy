package com.strontech.imgautam.handycaft.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.strontech.imgautam.handycaft.R;

public class FpxActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fpx_activity);
        new Handler().postDelayed(new Runnable() { // from class: com.ashish.mymall.FPXLoading.1
            @Override // java.lang.Runnable
            public void run() {
                String name = getIntent().getStringExtra("name");
                String url = getIntent().getStringExtra("url");

                Intent intent = new Intent(getApplicationContext(), WebView.class);

                intent.putExtra("name", name);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        }, 3000L);

    }
}
