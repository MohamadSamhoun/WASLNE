package com.example.waslne;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ScheduleActivity extends AppCompatActivity {

    private Button buttonToLau;
    private Button buttonFromLau;
    private ScrollView toLauSchedule;
    private ScrollView fromLauSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_schedule);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BottomNavHelper.setupBottomNavigation(this, "schedule");

        // Initialize views
        buttonToLau = findViewById(R.id.button_to_lau);
        buttonFromLau = findViewById(R.id.button_from_lau);
        toLauSchedule = findViewById(R.id.to_lau_schedule);
        fromLauSchedule = findViewById(R.id.from_lau_schedule);

        setSelectedButton(buttonToLau);
        setUnselectedButton(buttonFromLau);

        buttonToLau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedButton(buttonToLau);
                setUnselectedButton(buttonFromLau);

                toLauSchedule.setVisibility(View.VISIBLE);
                fromLauSchedule.setVisibility(View.GONE);
            }
        });

        buttonFromLau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedButton(buttonFromLau);
                setUnselectedButton(buttonToLau);

                toLauSchedule.setVisibility(View.GONE);
                fromLauSchedule.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setSelectedButton(Button button) {
        button.setBackgroundTintList(getColorStateList(android.R.color.transparent));
        button.setBackgroundColor(getResources().getColor(R.color.navyBlue, null));
        button.setTextColor(getResources().getColor(R.color.white, null));
    }

    private void setUnselectedButton(Button button) {
        button.setBackgroundTintList(getColorStateList(R.color.lightGray));
        button.setTextColor(getResources().getColor(R.color.darkGray, null));
    }
}