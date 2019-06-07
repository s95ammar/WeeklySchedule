package com.s95ammar.weeklyschedule.models;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Iterator;

public class SchedulesList extends ArrayList<ScheduleItem> {
    private static final String TAG = "SchedulesList";
    private static SchedulesList instance;
    @Expose
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
        Log.d(TAG, "createFromJson: " + json);
        if (instance == null) {
            Gson gson = LocalTimeSerializer.getGsonLocalTimeSerializer();
            instance = gson.fromJson(json, SchedulesList.class);
        }
    }

    public void loadActiveSchedule(int activeScheduleIndex) {
        if (activeScheduleIndex != -1) {
            activeSchedule = instance.get(activeScheduleIndex);
        }
    }

    public boolean hasActiveSchedule() {
        return activeSchedule != null;
    }

}
