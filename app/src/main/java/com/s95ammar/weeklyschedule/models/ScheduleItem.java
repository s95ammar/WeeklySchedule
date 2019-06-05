package com.s95ammar.weeklyschedule.models;

import java.io.Serializable;
import java.util.ArrayList;

public class ScheduleItem implements Serializable {
    private String name;
    private boolean isActive;
    private ArrayList<Day> days;


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

    @Override
    public String toString() {
        return '{' + name + ':' + isActive + '}';
    }
}
