package com.s95ammar.weeklyschedule.models;


import org.joda.time.LocalTime;

public class Event {
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;

    public Event(String name, String startTime, String endTime) { // TODO: test
        this.name = name;
        this.startTime = LocalTime.parse(startTime);
        this.endTime = LocalTime.parse(endTime);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public boolean overlapsWith(Event other) {
        return (startTime.isAfter(other.startTime) && startTime.isBefore(other.endTime)) ||
                (endTime.isAfter(other.startTime) && endTime.isBefore(other.endTime));
    }
}
