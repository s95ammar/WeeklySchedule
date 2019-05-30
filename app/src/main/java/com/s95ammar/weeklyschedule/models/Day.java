package com.s95ammar.weeklyschedule.models;

import java.time.DayOfWeek;
import java.util.ArrayList;

public class Day {
    private DayOfWeek dayOfWeek;
    private ArrayList<Event> events;

    public Day(DayOfWeek dayOfWeek, ArrayList<Event> events) {
        this.dayOfWeek = dayOfWeek;
        this.events = events;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }
}
