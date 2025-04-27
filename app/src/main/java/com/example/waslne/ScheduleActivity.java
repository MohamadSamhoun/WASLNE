package com.example.waslne;

import android.content.Intent;
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


        int[] viewStopButtonIds = {
                R.id.btnToScheduleStop1AGPS,
                R.id.btnToScheduleStop2AGPS,
                R.id.btnToScheduleStop3AGPS,
                R.id.btnToScheduleStop4AGPS,
                R.id.btnToScheduleStop5AGPS,
                R.id.btnToScheduleStop6AGPS,
                R.id.btnToScheduleStop7AGPS,

                R.id.btnToScheduleStop1BGPS,
                R.id.btnToScheduleStop2BGPS,
                R.id.btnToScheduleStop3BGPS,
                R.id.btnToScheduleStop4BGPS,
                R.id.btnToScheduleStop5BGPS,
                R.id.btnToScheduleStop6BGPS,
                R.id.btnToScheduleStop7BGPS,

                R.id.btnFromScheduleStop1AGPS,
                R.id.btnFromScheduleStop2AGPS,
                R.id.btnFromScheduleStop3AGPS,
                R.id.btnFromScheduleStop4AGPS,
                R.id.btnFromScheduleStop5AGPS,
                R.id.btnFromScheduleStop6GPS,
                R.id.btnFromScheduleStop7AGPS,

                R.id.btnFromScheduleStop1BGPS,
                R.id.btnFromScheduleStop2BGPS,
                R.id.btnFromScheduleStop3BGPS,
                R.id.btnFromScheduleStop4BGPS,
                R.id.btnFromScheduleStop5BGPS,
                R.id.btnFromScheduleStop6BGPS,
                R.id.btnFromScheduleStop7BGPS
        };



        BottomNavHelper.setupBottomNavigation(this, "schedule");


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

        for (int id : viewStopButtonIds) {
            Button button = findViewById(id);
            button.setOnClickListener(v -> {
                Intent intent = new Intent(ScheduleActivity.this, StopActivity.class);
                startActivity(intent);
            });
        }

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