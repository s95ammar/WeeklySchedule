package com.s95ammar.weeklyschedule.models;

import android.support.annotation.StringDef;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Comparator;

public class Day implements Serializable {

    private String dayOfWeek;

    private ArrayList<Event> events;

    public Day(String dayOfWeek, ArrayList<Event> events) {
        this.dayOfWeek = dayOfWeek;
        this.events = events;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }


    @Override
    public boolean equals(Object that) {
        if (that instanceof Day) {
            return dayOfWeek.equals(((Day) that).dayOfWeek);
        }
        return false;
    }

    @Override
    public String toString() {
        return dayOfWeek;

    }
}
