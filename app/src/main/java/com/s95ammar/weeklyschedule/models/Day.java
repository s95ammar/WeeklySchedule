package com.s95ammar.weeklyschedule.models;

import com.google.gson.annotations.Expose;

import org.joda.time.LocalTime;

import java.io.Serializable;
import java.util.ArrayList;

public class Day implements Serializable {
    public static final int TOTAL_HOURS = 24;
    public static final int MINUTES_IN_HOUR = 60;
    @Expose
    private String dayOfWeek;
    @Expose
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

    public static String[] getHoursStringArray() {
        String[] hours = new String[TOTAL_HOURS];
        for (int i = 0; i < hours.length; i++) {
            hours[i] = LocalTime.MIDNIGHT.plusHours(i).toString(ScheduleItem.timePattern);
        }
        return hours;
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
        return '{' + dayOfWeek + ", events: " + events + '}';

    }
}
