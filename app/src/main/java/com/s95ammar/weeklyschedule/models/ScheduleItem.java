package com.s95ammar.weeklyschedule.models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;

public class ScheduleItem implements Serializable {
    @Expose
    private String name;
    @Expose
    private boolean isActive;
    @Expose
    private ArrayList<Day> days;
    public static final String [] WEEK_DAYS = {"Sunday" , "Monday" , "Tuesday" , "Wednesday" , "Thursday" , "Friday" , "Saturday"};
    public static String timePattern;

    public ScheduleItem(String name, ArrayList<Day> days) {
        this.name = name;
        this.days = days;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public ArrayList<Day> getDays() {
        return days;
    }

    public void setDays(ArrayList<Day> days) {
        this.days = days;
    }

    public int getIndexOfDay(Day day) {
        for (int i = 0; i < WEEK_DAYS.length; i++) {
            if (WEEK_DAYS[i].equals(day.getDayOfWeek()))
                return i;
        }
        return -1;
    }

    @Override
    public String toString() {
        return '{' + name + ':' + isActive + ", days: " + days + '}';
    }
}
