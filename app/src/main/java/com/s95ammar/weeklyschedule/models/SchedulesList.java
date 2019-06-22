package com.s95ammar.weeklyschedule.models;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import org.joda.time.LocalTime;

import java.util.ArrayList;

public class SchedulesList extends ArrayList<Schedule> {
    private static final String TAG = "SchedulesList";
    private static SchedulesList instance;
    @Expose
    private Schedule activeSchedule;

    private SchedulesList() {
    }

    public static synchronized SchedulesList getInstance() {
        if (instance == null) {
            instance = new SchedulesList();
        }
        return instance;
    }

    public Schedule getActiveSchedule() {
        return activeSchedule;
    }

    public void setActiveSchedule(Schedule activeSchedule) {
        this.activeSchedule = activeSchedule;
    }

    public static void createFromJson(String json) {
        Log.d(TAG, "createFromJson: " + json);
        if (instance == null) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalTime.class, new LocalTimeSerializer())
                    .create();
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
