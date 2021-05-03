package com.ashishbharam.remotebindingdemoserviceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnStartMyService, btnStopMyService;
    Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("TAG", "if MainActivity thread ID is same as service: " + Thread.currentThread().getId());
        Log.i("TAG", "that means MainActivity & MyService is running on same thread ");

        btnStartMyService = findViewById(R.id.btnStartService);
        btnStopMyService = findViewById(R.id.btnStopService);

        serviceIntent = new Intent(getApplicationContext(), MyService.class);

        btnStartMyService.setOnClickListener(v -> {
            Log.i("TAG", "Service Started: ");
            startService(serviceIntent);
        });

        btnStopMyService.setOnClickListener(v -> {
            Log.i("TAG", "Service Stopped: ");
            stopService(serviceIntent);
        });
    }
}