package com.example.waslne;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LiveActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_live);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        BottomNavHelper.setupBottomNavigation(this, "live");

        int[] stationIds = { R.id.station1, R.id.station2, R.id.station3, R.id.station4, R.id.station5 , R.id.station6, R.id.station7};


        for (int id : stationIds) {
            View station = findViewById(id);
            station.setOnClickListener(v -> {
                Intent intent = new Intent(LiveActivity.this, StopActivity.class);
                startActivity(intent);
            });
        }


    }
}