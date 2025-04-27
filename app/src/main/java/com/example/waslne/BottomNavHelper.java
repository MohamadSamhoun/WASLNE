package com.example.waslne;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BottomNavHelper {

    private static final int COLOR_ACTIVE = Color.parseColor("#E9D617");
    private static final int COLOR_INACTIVE = Color.WHITE;

    public static void setupBottomNavigation(Activity activity, String currentTab) {
        // Find all navigation items
        LinearLayout navSchedule = activity.findViewById(R.id.nav_schedule);
        LinearLayout navLive = activity.findViewById(R.id.nav_live);
        LinearLayout navOthers = activity.findViewById(R.id.nav_others);
        LinearLayout navSupport = activity.findViewById(R.id.nav_support);

        ImageView scheduleIcon = activity.findViewById(R.id.nav_schedule_icon);
        TextView scheduleText = activity.findViewById(R.id.nav_schedule_text);
        ImageView liveIcon = activity.findViewById(R.id.nav_live_icon);
        TextView liveText = activity.findViewById(R.id.nav_live_text);
        ImageView othersIcon = activity.findViewById(R.id.nav_others_icon);
        TextView othersText = activity.findViewById(R.id.nav_others_text);
        ImageView supportIcon = activity.findViewById(R.id.nav_chat_icon);
        TextView supportText = activity.findViewById(R.id.nav_chat_text);

        scheduleIcon.setColorFilter(COLOR_INACTIVE);
        scheduleText.setTextColor(COLOR_INACTIVE);
        liveIcon.setColorFilter(COLOR_INACTIVE);
        liveText.setTextColor(COLOR_INACTIVE);
        othersIcon.setColorFilter(COLOR_INACTIVE);
        othersText.setTextColor(COLOR_INACTIVE);
        supportIcon.setColorFilter(COLOR_INACTIVE);
        supportText.setTextColor(COLOR_INACTIVE);

        switch (currentTab) {
            case "schedule":
                scheduleIcon.setColorFilter(COLOR_ACTIVE);
                scheduleText.setTextColor(COLOR_ACTIVE);
                break;
            case "live":
                liveIcon.setColorFilter(COLOR_ACTIVE);
                liveText.setTextColor(COLOR_ACTIVE);
                break;
            case "others":
                othersIcon.setColorFilter(COLOR_ACTIVE);
                othersText.setTextColor(COLOR_ACTIVE);
                break;
            case "support":
                supportIcon.setColorFilter(COLOR_ACTIVE);
                supportText.setTextColor(COLOR_ACTIVE);
                break;
        }

        // Set click listeners
        navSchedule.setOnClickListener(v -> {
            if (!currentTab.equals("schedule")) {
                Intent intent = new Intent(activity, ScheduleActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                activity.startActivity(intent);
            }
        });

        navLive.setOnClickListener(v -> {
            if (!currentTab.equals("live")) {
                Intent intent = new Intent(activity, LiveActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                activity.startActivity(intent);
            }
        });


        navSupport.setOnClickListener(v -> {
            if (!currentTab.equals("support")) {
                Intent intent = new Intent(activity, SupportActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                activity.startActivity(intent);
            }
        });
//
//        navOthers.setOnClickListener(v -> {
//            if (!currentTab.equals("others")) {
//                Intent intent = new Intent(activity, OthersActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                activity.startActivity(intent);
//            }
//        });
    }
}