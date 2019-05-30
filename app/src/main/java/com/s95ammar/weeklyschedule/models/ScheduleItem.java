package com.s95ammar.weeklyschedule.models;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.ArrayList;

public class ScheduleItem implements Serializable {
    private String name;
    private boolean isActive;
    private ArrayList<Day> days;

    public ScheduleItem(String name) {
        this.name = name;
        days = new ArrayList<>();
/*
        for (int i = 0; i < 7; i++) {
            days.add(new Day(DayOfWeek.SATURDAY.plus(i)), new ArrayList<Event>());
        }
*/
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

    @Override
    public String toString() {
        return '{' + name + ':' + isActive + '}';
    }
}
