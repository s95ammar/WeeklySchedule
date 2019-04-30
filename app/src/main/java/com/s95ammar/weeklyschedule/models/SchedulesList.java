package com.s95ammar.weeklyschedule.models;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;

public class SchedulesList extends ArrayList<ScheduleItem> {
    private static final String TAG = "SchedulesList";
    private static SchedulesList instance;
    private ScheduleItem activeSchedule;

    private SchedulesList() {
    }

    public static synchronized SchedulesList getInstance() {
        if (instance == null) {
            instance = new SchedulesList();
        }
        return instance;
    }


    public ScheduleItem getActiveSchedule() {
        return activeSchedule;
    }

    public void setActiveSchedule(ScheduleItem activeSchedule) {
        this.activeSchedule = activeSchedule;
    }

    public static void createFromJson(String json) {
        Log.d(TAG, "createFromJson: creating from json");
        if (instance == null) {
            instance = new Gson().fromJson(json, SchedulesList.class);
            Log.d(TAG, "createFromJson: " + getInstance().toString());
        } else {
            Log.d(TAG, "createFromJson: User already exists");
        }

    }
}
