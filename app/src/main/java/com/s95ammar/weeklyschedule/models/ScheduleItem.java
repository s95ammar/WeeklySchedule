package com.s95ammar.weeklyschedule.models;

import java.io.Serializable;
import java.util.ArrayList;

public class ScheduleItem implements Serializable {
    private String name;
    private boolean isActive;
    private ArrayList<ArrayList<Event>> days;

    public ScheduleItem(String name) {
        this.name = name;
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
