package com.s95ammar.weeklyschedule.models;

public class ScheduleItem {
    private String name;
    private boolean isActive;


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
